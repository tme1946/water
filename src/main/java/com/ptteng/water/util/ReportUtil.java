package com.ptteng.water.util;

import com.ptteng.water.system.pojo.Element;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportUtil {

    public static HashMap elementPercent(ArrayList<Element> elements) {
        HashMap<Long, Double> out = new HashMap<>();
        HashMap<Long, Integer> num = new HashMap<>();
        for(Element element : elements) {
            if(!out.containsKey(element.getId())){
                num.put(element.getId(), 1);
                out.put(element.getId(), element.getPercent());
            }
            num.put(element.getId(), num.get(element.getId()) + 1);
            out.put(element.getId(), out.get(element.getId()) + element.getPercent());
        }
        for(Long key : out.keySet()){
            out.put(key, out.get(key) / num.get(key));
        }
        return out;
    }

    public static ArrayList<Element> sort(ArrayList<Element> elements) {
        qSort(elements, 0, elements.size() - 1);
        return elements;
    }

    private static void qSort(ArrayList<Element> arr, int head, int tail) {
        if (head >= tail || arr == null || arr.size() <= 1) {
            return;
        }
        int i = head, j = tail;
        Double pivot = arr.get((head + tail) / 2).getPercent();
        while (i <= j) {
            while (arr.get(i).getPercent() < pivot) {
                ++i;
            }
            while (arr.get(j).getPercent() > pivot) {
                --j;
            }
            if (i < j) {
                Element t = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, t);
                ++i;
                --j;
            } else if (i == j) {
                ++i;
            }
        }
        qSort(arr, head, j);
        qSort(arr, i, tail);
    }
}
