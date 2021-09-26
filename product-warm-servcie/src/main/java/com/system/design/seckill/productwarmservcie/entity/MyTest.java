package com.system.design.seckill.productwarmservcie.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

import static com.baomidou.mybatisplus.annotation.FieldFill.INSERT;

/**
 * @description:
 * @author: 贾凯
 * @create: 2021-09-24 14:51
 */
@ApiModel("测试对象")
@TableName("t_my_test")
@Data@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyTest {
    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "my_name", jdbcType = JdbcType.VARCHAR)
    private String myName;

    @TableField(value = "age", jdbcType = JdbcType.INTEGER)
    private Integer age;

    //创建时间，只在插入时自动填充
    @TableField(value = "create_time", fill = FieldFill.INSERT, jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;

    //更新时间，插入或者更新时自动填充
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;

    /**
     * 版本号（用于乐观锁， 默认为 1）
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 使用 @TableField(exist = false) ，表示该字段在数据库中不存在 ，所以不会插入数据库中
     * 使用 transient 、 static 修饰属性也不会插入数据库中
     */
    @TableField(exist = false)
    private String phone;
}
