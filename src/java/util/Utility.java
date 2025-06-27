/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Do Duan
 */
public class Utility {

    public List<Object> paging(List<Object> listAll, int itemPerPage, int pageNumber) {
        List<Object> listPerPage = new ArrayList<>();
        int start, end;
        start = (pageNumber - 1) * itemPerPage;
        end = Math.min(pageNumber * itemPerPage, listAll.size());
        listPerPage = getListByPage(listAll, start, end);
        return listPerPage;
    }

    private static List<Object> getListByPage(List<Object> list,
            int start, int end) {
        List<Object> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(list.get(i));
        }
        return arr;
    }

}
