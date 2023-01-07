package com.xingdong.seckill.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingdong.seckill.common.po.SeckillOrder;
import org.apache.ibatis.annotations.Update;

/**
 * @author chengzhengzheng
 * @date 2021/10/28
 */
public interface OrderMapper extends BaseMapper<SeckillOrder> {
    @Update("update t_order set status=#{status} where order_id=#{orderId}")
    int updateStatus(long orderId, String status);
}
