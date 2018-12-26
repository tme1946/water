package com.ptteng.water.system.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * performance
 * @author 
 */
@Data
public class Performance {

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
     * 绩效表名
     */
    @NotNull(message = "名字不能为空")
    private String name;

    /**
     * 模板id
     */
    @NotNull(message = "模板id不能为空")
    private Long templateId;

    /**
     * 年份
     */
    @NotNull(message = "年份不能为空")
    private Integer year;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空")
    private Long departmentId;

    /**
     * 父部门名
     */
    private String parentName;

    /**
     * 绩效分值 from 部门表
     */
    private Double grade;

    /**
     * 上下架状态
     */
    private Integer status;

    /**
     * 模板元素
     */
    private ArrayList<Element> elements;

}