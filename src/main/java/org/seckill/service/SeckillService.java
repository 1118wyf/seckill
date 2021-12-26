package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeateSeckillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口站在“使用者”角度设计接口
 * 三个方面：方法定义、粒度（具体的操作、行为）
 * 参数
 * 返回类型（return 类型/异常 dto entity exception）
 * Created by wyf on 2021/12/18 8:58
 */
public interface SeckillService {

    /**
     * 查询所有的秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 根据id查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);


    /**
     * 秒杀开启时输出秒杀接口地址
     *否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param uerPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long uerPhone, String md5)
      throws SeckillException, RepeateSeckillException, SeckillCloseException;
}
