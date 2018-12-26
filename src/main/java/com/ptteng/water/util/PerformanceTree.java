package com.ptteng.water.util;

import com.ptteng.water.system.pojo.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe
 */
public class PerformanceTree {

    public static ArrayList<Element> getTree(ArrayList<Element> elements) {
        ArrayList<Element> elementTree = new ArrayList<>(elements);
        // 添加子节点
        for (Element item : elements) {
            if (item != null) {
                item.setChrildren(getChildren(item, elementTree));
            }
        }
        return elementTree;
    }

    /* 获取指定节点的子节点 */
    private static ArrayList<Element> getChildren(Element element, ArrayList<Element> elements) {
        ArrayList<Element> childrenList = new ArrayList<>();
        for (Element child : elements) {
            if (child != null && child.getParentId().equals(element.getId())) {
                childrenList.add(child);
            }
        }
        return childrenList;
    }


    /* 获取根节点 */
    public static ArrayList<Element> getRoots(ArrayList<Element> elements) {
        ArrayList<Element> roots = new ArrayList<>(4);
        for (Element element : elements) {
            if (element != null && element.getParentId() == 0) {
                roots.add(element);
            }
        }
        return roots;
    }

    /* 获取指定节点下的全部叶子节点 */
    public static void getTreeList(Element element, ArrayList treeList) {
        List<Element> chrildren = element.getChrildren();
        for (Element aChrildren : chrildren) {
            if (aChrildren.getChrildren().size() == 0) {
                treeList.add(aChrildren);
            } else {
                getTreeList(aChrildren, treeList);
            }
        }
    }

    public static void getList(ArrayList<Element> templateTree, ArrayList<Long> treeList) {
        for (Element element : templateTree) {
            treeList.add(element.getId());
            getList(element.getChrildren(), treeList);
        }
    }

}
