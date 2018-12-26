package com.ptteng.water.system.pojo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * department
 * @author joe
 */
@Data
public class Department {

    // 上下架状态 0：下架  1：上架
    public static final Integer SHELF_OFF = 0;
    public static final Integer SHELF_ON = 1;


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
     * 部门名
     */
    @NotBlank(message = "部门名不能为空")
    private String name;

    /**
     * 负责人
     */
    private String principal;

    /**
     * 预算
     */
    private Double budget;

    /**
     * 父部门id
     */
    @NotNull(message = "父部门不能为空")
    private Long parentId;

    /**
     * 父部门名
     */
    private String parentName;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 上下架状态
     */
    private Integer status;

    /**
     * 地址
     */
    private String address;

    /**
     * 简介
     */
    private String intro;

    /**
     * 项目情况
     */
    private String condition;

    /**
     * 绩效分值
     */
    private Double grade;

    /**
     * 当前模板Id
     */
    private Long templateId;

    /**
     * 当前模板名
     */
    private String templateName;

    /**
     * 绩效年份
     */
    private Integer year;

}