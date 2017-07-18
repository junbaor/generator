package com.common.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;


/**
 * 系统启动监听器
 *
 * @author XiongChun
 * @since 2010-04-13
 */
public class SystemInitListener implements ServletContextListener {
    private static Log log = LogFactory.getLog(SystemInitListener.class);
    private boolean success = true;
    private ApplicationContext wac = null;


    public void contextDestroyed(ServletContextEvent sce) {

    }


    public void contextInitialized(ServletContextEvent sce) {
        systemStartup(sce.getServletContext());
    }


    /**
     * 应用平台启动
     */
    private void systemStartup(ServletContext servletContext) {
        long start = System.currentTimeMillis();
            log.info("*******************************************************");
            log.info("代码生成器平台->开始启动...");
            log.info("*******************************************************");
        try {
            SpringBeanLoader.setApplicationContext(servletContext);
            ///  wac = SpringBeanLoader.getApplicationContext();
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

       
        long timeSec = (System.currentTimeMillis() - start) / 1000;
        log.info("********************************************");
        if (success) {
            log.info("代码生成器平台->启动成功[" + G4Utils.getCurrentTime() + "]");
            log.info("启动总耗时: " + timeSec / 60 + "分 " + timeSec % 60 + "秒 ");
        } else {
            log.error("代码生成器平台->启动失败[" + G4Utils.getCurrentTime() + "]");
            log.error("启动总耗时: " + timeSec / 60 + "分" + timeSec % 60 + "秒");
        }
        log.info("********************************************");
    }

  
}
