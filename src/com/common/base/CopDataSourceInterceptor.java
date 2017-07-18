package com.common.base;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

/**
 * Created by IntelliJ IDEA.
 * User: weizhao.dong
 * Date: 12-6-14
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */
public class CopDataSourceInterceptor extends AbstractInterceptor{

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        DataSourceContextHolder.setDataSource("cop");
        String result=actionInvocation.invoke();
        DataSourceContextHolder.setDataSource("default");
        return result;
    }
}
