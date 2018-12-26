package com.ptteng.water.system.pojo;

import lombok.Data;

import java.util.ArrayList;

/**
 * template
 * @author joe
 */
@Data
public class Template {

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
     * 模板名
     */
    private String name;

    /**
     * 上架状态
     */
    private Integer status;

    /**
     * 四个一级指标
     */
    private ArrayList<Element> elements;

    /**
     * 已绑定部门数
     */
    private Integer usedNums;
}