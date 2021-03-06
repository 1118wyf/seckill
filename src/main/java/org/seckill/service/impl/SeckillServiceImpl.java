package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeateSeckillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by wyf on 2021/12/18 10:13
 */
//@Component @Service  @Dao  @Controller
    @Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入Service依赖   @Autowired @Resource @ Inject
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    //md5的盐值字符串，用于混淆MD5
    private final String slate = "shggbae8i&*）（……￥%%￥#3239uf";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,5);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化; 超市的基础上维护一致性
        /**
         * get from cache
         * if null
         *   get db
         *  else
         *     put cache
         *  locgoin
         */
//        Seckill seckill = seckillDao.queryById(seckillId);
//        if(seckill == null){
//            return new Exposer(false, seckillId);
//        }

        //优化后
        //1、访问Redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            //2、访问数据库
            seckill = seckillDao.queryById(seckillId);
            if(seckill == null){
                return new Exposer(false, seckillId);
            }else{
                //3、放入redis
                redisDao.putSeckill(seckill);
            }

        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统的当前时间
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false, seckillId, nowTime, startTime,endTime);
        }
        //转化特定字符串的过程，这个过程是不可逆的
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slate;
        //DigestUtils.md5DigestAsHex  spring提供的工具类产生MD5
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点
     * 1：开发团队达成一致约定，明确标注事务方法的编程风格
     * 2；保证事务方法的执行时间尽可能的短，不要穿插其他网络操作，RPC/http请求/剥离到事务方法外部
     * 3：不是所有的方法都需要事务， 如 只有一条修改操作，只读操作，不需要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeateSeckillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("Seckill date rewrite!");
        }
        //执行秒杀逻辑
        Date nowTime = new Date();
        try {
            /**
            //减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新记录
                throw new SeckillException("Seckill is closed!");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                //唯一的seckillId userPhone
                if (insertCount <= 0) {
                    //重复秒杀
                    throw new RepeateSeckillException("Seckill repeate!");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
           */

            //优化
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //唯一的seckillId userPhone
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeateSeckillException("Seckill repeate!");
            } else {
                //减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新记录
                    throw new SeckillException("Seckill is closed!");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeateSeckillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有的编译期异常转化为运行期异常
            throw new SeckillException("Seckill inner error! " + e.getMessage());
        }

    }
}
