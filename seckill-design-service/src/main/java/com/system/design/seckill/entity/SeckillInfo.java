package com.system.design.seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import static com.baomidou.mybatisplus.annotation.FieldFill.*;

/**
 * @author chengzhengzheng
 * @date 2021/9/19
 */
@SuppressWarnings("all")
@TableName("seckill_info")
@Data
public class SeckillInfo {
    /**
     * 秒杀id
     */
    @Id
    @TableId(value = "seckill_id", type = IdType.AUTO)
    private Long seckillId;

    @TableField(value = "ctime", fill = INSERT, jdbcType = JdbcType.DATE)
    private Long ctime;

    @TableField(value = "utime", fill = INSERT_UPDATE, jdbcType = JdbcType.DATE)
    private Long utime;
    /**
     * 秒杀名称
     */
    @TableField(value = "seckill_name", jdbcType = JdbcType.VARCHAR)
    private String        seckillName;
    /**
     * 商品id
     */
    @TableField(value = "product_id", jdbcType = JdbcType.BIGINT)
    private String        productId;
    /**
     * 价格
     */
    @TableField(value = "price", jdbcType = JdbcType.DECIMAL)
    private BigDecimal    price;
    /**
     * 库存
     */
    @TableField(value = "count", jdbcType = JdbcType.BIGINT)
    private Long          count;
    /**
     * 锁定的数量
     */
    @TableField(value = "lock_count", jdbcType = JdbcType.BIGINT)
    private Long       lockCount;
    /**
     * 库存版本信息、用来保持和数据库的一致性使用的。
     * <p>
     * 考虑一种情况、假如两个线程都从redis中查询的库存是1，那么其中一个线程使用乐观锁机制、将数据库中的version改为+1了
     * 那么另一个线程就肯定无法扣减库存了。 防止了超卖问题。
     * <p>
     * 可以保证缓存和数据库的一致性 参考： https://juejin.cn/post/6844903604998914055#heading-11
     */
    @TableField(value = "version", jdbcType = JdbcType.BIGINT)
    private Long       version;

}
