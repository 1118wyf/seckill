package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeateSeckillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by wyf on 2021/12/18 21:11
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
   private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list = {}", list);
    }

    @Test
    public void getById() {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill ={}", seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        logger.info("exposer={}", exposer);
    }

    @Test
    public void executeSeckill() {
        long seckillId = 1000;
        long userPhone = 15516275936L;
        String md5 = "db5ea8fe22a658667a6d45562f0f6d7c";
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
            logger.info("result = {}", seckillExecution);
        }catch (SeckillCloseException e){
            logger.info(e.getMessage());
        }catch(RepeateSeckillException e1){
            logger.info(e1.getMessage());
        }
    }


    //测试过程不应该先找到md5再复制
    //集成测试测试代码完整逻辑，注意测试的可重复执行
    @Test
    public void testSeckill() throws Exception{
        long seckillId = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            //秒杀开启
            logger.info("exposer={}", exposer);
            long userPhone = 15516275935L;
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
                logger.info("result = {}", seckillExecution);
            }catch (SeckillCloseException e){
                logger.info(e.getMessage());
            }catch(RepeateSeckillException e1){
                logger.info(e1.getMessage());
            }
        }else{
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void testSeckillLogic(){

    }
}