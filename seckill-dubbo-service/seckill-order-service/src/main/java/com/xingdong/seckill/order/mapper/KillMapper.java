package com.xingdong.seckill.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingdong.seckill.common.po.Seckill;
import org.apache.ibatis.annotations.Update;

/**
 * @author chengzhengzheng
 * @date 2021/11/1
 */
@SuppressWarnings("all")
public interface KillMapper extends BaseMapper<Seckill> {
    /**
     * 库存-1
     *
     * @param killId
     * @return
     */
    @Update("update t_seckill set count = count - 1 where seckill_id = #{killId} and count > 0")
    int decreaseStorage(long killId);

    /**
     * 库存+1
     *
     * @param orderId
     */
    @Update("update t_seckill set count = count + 1 where seckill_id = #{killId}")
    void incStorage(Long orderId);
}
