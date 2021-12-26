package org.seckill.dao;

/**
 * Created by wyf on 2021/12/16
 */

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

//idea 自动创建Test的快捷键 CTRL + shift + t
public interface SeckillDao {

    /**
     * 减库存，
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1， 表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据商品的id查找秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll (@Param("offset") int offset,  @Param("limit") int limit);
}
