package com.ptteng.water.system.pojo;

import lombok.Data;

/**
 * grade
 * @author 
 */
@Data
public class Grade {
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Long createAt;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新时间
     */
    private Long updateAt;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 绩效id
     */
    private Long performanceId;

    /**
     * 指标id
     */
    private Long elementId;

    /**
     * 分数
     */
    private Double grade;

    /**
     * 完成情况
     */
    private String actual;
}