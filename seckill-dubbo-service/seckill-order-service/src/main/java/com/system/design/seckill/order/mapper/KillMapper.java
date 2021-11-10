package com.system.design.seckill.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.system.design.seckill.common.po.Seckill;
import org.apache.ibatis.annotations.Update;

/**
 * @author chengzhengzheng
 * @date 2021/11/1
 */
@SuppressWarnings("all")
public interface KillMapper extends BaseMapper<Seckill> {
    /**
     *
     * @param killId
     * @return
     */
    @Update("update t_seckill set count = count - 1 where seckill_id = #{killId} and count > 0")
    int decreaseStorage(long killId);
}
