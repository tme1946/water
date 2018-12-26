package com.ptteng.water.system.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * element
 * @author joe
 */
@Data
    public class Element implements Serializable {

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
     * 指标名称
     */
    private String name;

    /**
     * 期望分数
     */
    private Double expectGrade;

    /**
     * 期望完成度
     */
    private String expect;

    /**
     * 评分标准
     */
    private String standard;

    /**
     * 解释
     */
    private String explain;

    /**
     * 父元素id
     */
    private Long parentId;

    /**
     * 子元素列表
     */
    private ArrayList<Element> chrildren;

    /**
     * 得分
     */
    private Double grade;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 完成情况
     */
    private String actual;

    /**
     * 差异
     */
    private Double diff;

    private Double percent;

    public void setDiff() {
        this.diff = this.expectGrade - this.grade;
    }

    public void setPercent() {
        this.percent = this.grade / this.expectGrade;
    }
}