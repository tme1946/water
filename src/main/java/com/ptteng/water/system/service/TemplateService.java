package com.ptteng.water.system.service;

import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Template;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface TemplateService {

    ArrayList<Template> findAllTemplate(String name);

    Template findOneTemplate(Long id);

    Long saveTemplate(Template template, ArrayList<Element> elements);

    /**
     * 结构化的元素列表拆成单个元素列表
     * @param templateTree 模板树
     * @param parentId 父节点id
     * @param templateId 模板id
     */
    void setElementsFromTree(ArrayList<Element> templateTree, Long parentId, Long templateId);

    Long updateTemplate(Long id, Template template);

    Long deleteTemplete(Long id);

    void deleteElements(ArrayList<Element> elements, ArrayList<Element> templates);

    Boolean beUsed(Long id);
}
