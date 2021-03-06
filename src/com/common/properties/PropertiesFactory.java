package com.common.properties;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.common.utils.G4Constants;


/**
 * Properties文件静态工厂
 * 
 * @author XiongChun
 * @since 2009-08-2
 */
public class PropertiesFactory {
    private static Log log = LogFactory.getLog(PropertiesFactory.class);
    /**
     * 属性文件实例容器
     */
    private static Map container = new HashMap();

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = PropertiesFactory.class.getClassLoader();
        }
        // 加载属性文件global.g4.properties
        try {
            InputStream is = classLoader.getResourceAsStream("global.g4.properties");
            PropertiesHelper ph = new PropertiesHelper(is);
            container.put(PropertiesFile.G4, ph);
        } catch (Exception e1) {
            log.error(G4Constants.Exception_Head + "加载属性文件global.g4.properties出错!");
            e1.printStackTrace();
        }


    }


    /**
     * 获取属性文件实例
     * 
     * @param pFile
     *            文件类型
     * @return 返回属性文件实例
     */
    public static PropertiesHelper getPropertiesHelper(String pFile) {
        PropertiesHelper ph = (PropertiesHelper) container.get(pFile);
        return ph;
    }
}
