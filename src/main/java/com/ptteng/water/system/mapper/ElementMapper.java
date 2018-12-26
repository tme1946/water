package com.ptteng.water.system.mapper;

import com.ptteng.water.system.pojo.Element;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ElementMapper {
    void addElement(Element element);

    ArrayList<Element> findElements(Long templateId);

    void deleteElement(Long id);

    void updateElement(Element element);

    Element findById(Long id);
}