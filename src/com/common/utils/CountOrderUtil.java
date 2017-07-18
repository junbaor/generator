package com.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;


/**
 * Created by IntelliJ IDEA. User: ritchrs Date: 11-5-16 desc:
 */
public class CountOrderUtil {

    public static CountOrder getCountOrder() {

        HttpServletRequest request = ServletActionContext.getRequest();
        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String order = request.getParameter("sort");
        String dir = request.getParameter("dir");

        CountOrder countOrder = new CountOrder();
        int startInt = 0;
        if (G4Utils.isNotEmpty(start)) {
            startInt = Integer.parseInt(start);
            countOrder.setStart(startInt);
        }
        if (G4Utils.isNotEmpty(limit)) {
            int limitInt = Integer.parseInt(limit);
            countOrder.setLimit(limitInt);
            countOrder.setEnd(startInt + limitInt);
        }
        countOrder.setOrderby(order);
        countOrder.setDir(dir);
        return countOrder;
    }
}
