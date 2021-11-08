package com.system.design.seckill.product.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.design.seckill.product.common.entity.Seckill;
import org.apache.ibatis.annotations.Update;

/**
 * @author chengzhengzheng
 * @date 2021/11/1
 */
public interface KillMapper extends BaseMapper<Seckill> {
    /**
     *
     * @param killId
     * @return
     */
    @Update("update t_seckill set ")
    int decreaseStorage(long killId);
}
