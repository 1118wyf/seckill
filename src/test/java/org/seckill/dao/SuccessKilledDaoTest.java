package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)  //自动加载spring对应的容器
//告诉junit spring配置文件的位置

@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SuccessKilledDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        long seckillId = 1001;
        long phone = 12345678901L;
        int insertCount = successKilledDao.insertSuccessKilled(seckillId, phone);
        System.out.println("insertCount = " + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        long seckilled = 1001;
        long phone = 12345678901L;

        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckilled, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}