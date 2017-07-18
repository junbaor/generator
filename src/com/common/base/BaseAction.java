package com.common.base;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSON;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;


/**
 * Created by IntelliJ IDEA. User: ritchrs Date: 11-5-17 Time: 下午3:35 desc:
 */
public class BaseAction extends ActionSupport {
    private static Logger log = LoggerFactory.getLogger(BaseAction.class);
    private static final long serialVersionUID = -967109465085675300L;
    private String titles;
    // 对应字段的key
    private String fields;
    // 参数
    private String params;

    private String fileName;

 
    protected JSON resultJson;


    public JSON getResultJson() {
        return resultJson;
    }


    public void setResultJson(JSON resultJson) {
        this.resultJson = resultJson;
    }






    /**
     * 获取一个Session属性对象
     *
     * @param request
     * @param sessionName
     * @return
     */
    protected Object getSessionAttribute(HttpServletRequest request, String sessionKey) {
        Object objSessionAttribute = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            objSessionAttribute = session.getAttribute(sessionKey);
        }
        return objSessionAttribute;
    }


    /**
     * 设置一个Session属性对象
     *
     * @param request
     * @param sessionName
     * @return
     */
    protected void setSessionAttribute(HttpServletRequest request, String sessionKey,
                                       Object objSessionAttribute) {
        HttpSession session = request.getSession();
        if (session != null)
            session.setAttribute(sessionKey, objSessionAttribute);
    }


    /**
     * 移除Session对象属性值
     *
     * @param request
     * @param sessionName
     * @return
     */
    protected void removeSessionAttribute(HttpServletRequest request, String sessionKey) {
        HttpSession session = request.getSession();
        if (session != null)
            session.removeAttribute(sessionKey);
    }


    /**
     * 获得HttpServletResponse。</br>
     *
     * @return HttpServletResponse
     */
    public HttpServletResponse getResponse() {
        HttpServletResponse response = ServletActionContext.getResponse();
        return response;
    }




    protected Map getKeyMethodMap() {
        return null;
    }


    public Date getDateParameter(String s, String farmat) {
        HttpServletRequest request = ServletActionContext.getRequest();
        farmat = farmat != null ? farmat : "yyyy-MM-dd";
        String result = request.getParameter(s);
        if (StringUtils.isBlank(result))
            return null;
        else {
            SimpleDateFormat df = new SimpleDateFormat(farmat);
            Date digdate = new Date();
            try {
                digdate = df.parse(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return digdate;
        }
    }





 


    public void generalSmsSendService(String smsAddress, String smsMessage) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> mapMsgentry1 = new HashMap<String, String>();
        mapMsgentry1.put("smsAddress", smsAddress);
        mapMsgentry1.put("smsMessage", smsMessage);
        list.add(mapMsgentry1);
//        if (QueryRelevanceSysUtil.generalSmsSendService(list)) {
//            LOG.info("短信发送成功");
//        } else {
//            LOG.info("短信发送失败");
//        }
    }


    public void setTitles(String titles) {
        this.titles = titles;
    }


    public void setFields(String fields) {
        this.fields = fields;
    }


    public void setParams(String params) {
        this.params = params;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


   


   







   

}
