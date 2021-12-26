package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test，junit依赖
 */
@RunWith(SpringJUnit4ClassRunner.class)  //自动加载spring对应的容器
//告诉junit spring配置文件的位置

@ContextConfiguration({"classpath:spring/spring-dao.xml"})


public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao  seckillDao;

    @Test
    public void queryById() {
        int id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        /**
         * SeckillDao.java文件中的List<Seckill> queryAll (int offset, int limit);接口的参数不加@Parametr注解时会报以下错误
         * org.apache.ibatis.binding.BindingException:
         * Parameter 'offset' not found.
         * Available parameters are [0, 1, param1, param2]
         * 原因：Java语言中没有保存形参的记录，queryAll (int offset, int limit) -> queryAll(arg0, arg1)
         */
        int offset = 0;
        int limit = 3;
        List<Seckill> seckillList = seckillDao.queryAll(offset, limit);
        for(Seckill  sec :seckillList ){
            System.out.println(sec);
        }
    }

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        long seckillId = 1000;
        int count = seckillDao.reduceNumber(seckillId,killTime);
        System.out.println("count = " + count);
    }


}