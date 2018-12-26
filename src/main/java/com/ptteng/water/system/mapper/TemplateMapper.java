package com.ptteng.water.system.mapper;

import com.ptteng.water.system.pojo.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface TemplateMapper {
    Template findById(Long id);

    ArrayList<Template> listTemplate(@Param("name") String name);

    void addTemplate(Template template);

    Long deleteTemplate(Long id);

    Long updateTemplate(Template template);
}