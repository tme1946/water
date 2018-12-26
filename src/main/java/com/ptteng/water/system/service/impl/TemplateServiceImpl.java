package com.ptteng.water.system.service.impl;

import com.ptteng.water.system.mapper.DepartmentMapper;
import com.ptteng.water.system.mapper.ElementMapper;
import com.ptteng.water.system.mapper.PerformanceMapper;
import com.ptteng.water.system.mapper.TemplateMapper;
import com.ptteng.water.system.pojo.Department;
import com.ptteng.water.system.pojo.Element;
import com.ptteng.water.system.pojo.Template;
import com.ptteng.water.system.service.TemplateService;
import com.ptteng.water.util.PerformanceTree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TemplateServiceImpl implements TemplateService {
    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private TemplateService templateService;

    @Resource
    private ElementMapper elementMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private PerformanceMapper performanceMapper;

    @Override
    public ArrayList<Template> findAllTemplate(String name) {
        ArrayList<Template> templates = templateMapper.listTemplate(name);
        log.info("部门数量 size = {}", templates.size());
        // 新增已绑定部门数
        for (Template template : templates) {
            ArrayList<Department> departments = departmentMapper.findByTemplateId(template.getId());
            template.setUsedNums(departments.size());
        }
        return templates;
    }

    @Override
    public Template findOneTemplate(Long id) {
        log.info("查找模板 id = {}", id);
        Template template = templateMapper.findById(id);
        if(template != null) {
            ArrayList<Element> elements = elementMapper.findElements(id);
            for(Element element : elements) {
                if (element.getChrildren() == null) {
                    element.setChrildren(new ArrayList<>(0));
                }
            }
            template.setElements(elements);
        }
        return template;
    }

    /**
     * 新增节点，保存数据
     * @param template 模板信息
     * @param templateTree 节点树
     * @return 新增元素id列表
     */
    @Override
    public Long saveTemplate(Template template, ArrayList<Element> templateTree) {
        log.info("新增模板");
        // 保存模板表
        Long currentTime = System.currentTimeMillis();
        template.setCreateAt(currentTime);
        template.setUpdateAt(currentTime);
        templateMapper.addTemplate(template);
        Long id = template.getId();
        setElementsFromTree(templateTree, 0L, template.getId());

        return id;
    }

    /**
     * 结构化的元素列表拆成单个元素列表
     * @param templateTree 模板树
     * @param parentId 父节点id
     * @param templateId 模板id
     */
    @Override
    public void setElementsFromTree(ArrayList<Element> templateTree, Long parentId, Long templateId) {
        for (Element element : templateTree) {
            Long id = element.getId();
            if(id == null) {
                element.setParentId(parentId);
                element.setTemplateId(templateId);
                // 保存元素表
                elementMapper.addElement(element);
                // 获取id
                id = element.getId();
                log.info("新增模板节点成功 id = {}", id);
                // 获取节点的子节点
            }else {
                log.info("更新模板节点 elementId = {}", element.getId());
                elementMapper.updateElement(element);
            }
            setElementsFromTree(element.getChrildren(), id, templateId);
        }
    }

    @Override
    public Long updateTemplate(Long id, Template template) {
        log.info("更新模板 id = {}", id);
        template.setId(id);
        template.setUpdateAt(System.currentTimeMillis());
        Long result = templateMapper.updateTemplate(template);
        deleteElements(templateService.findOneTemplate(id).getElements(), template.getElements());
        setElementsFromTree(template.getElements(), 0L, id);

        return result;
    }

    @Override
    public Long deleteTemplete(Long id) {
        // 部门绑定的模板不能删除
        log.info("删除模板和其下的指标元素 id = {}", id);
        ArrayList<Element> elements = elementMapper.findElements(id);
        log.info("开始删除模板下的元素");
        elements.forEach(item -> elementMapper.deleteElement(item.getId()));
        return templateMapper.deleteTemplate(id);
    }

    /**
     * 更新模板，删除元素
     * @param elements 更新前的元素
     * @param templates 更新后的元素
     */
    @Override
    public void deleteElements(ArrayList<Element> elements, ArrayList<Element> templates) {
        log.info("更新模板，判断是否有删除节点");
        ArrayList<Long> old = (ArrayList<Long>) elements.stream().map(Element::getId).collect(Collectors.toList());
        ArrayList<Long> result = new ArrayList<>();
        PerformanceTree.getList(templates, result);
        log.info("更新之前的长度 length = {}, 更新之后的长度 length = {}", old.size(), result.size());
        for (Long id : old) {
            if(!(result.contains(id))) {
                log.info("删除节点 id = {}", id);
                elementMapper.deleteElement(id);
            }
        }
    }

    @Override
    public Boolean beUsed(Long id) {
        ArrayList<Long> departIds = departmentMapper.findTemplateId();
        ArrayList<Long> perforIds = performanceMapper.findTemplateId();
        return departIds.contains(id) || perforIds.contains(id);
    }
}
