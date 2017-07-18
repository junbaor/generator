package com.common.json;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;
import net.sf.json.util.JSONUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.functions.T;

import com.common.utils.G4Utils;


/**
 * Json处理器<br>
 * 
 * @author XiongChun
 * @since 2009-07-07
 */
public class JsonHelper {

    private static Log log = LogFactory.getLog(JsonHelper.class);


    /**
     * 将不含日期时间格式的Java对象系列化为Json资料格式
     * 
     * @param pObject
     *            传入的Java对象
     * @return
     */
    public static final String encodeObject2Json(Object pObject) {
        String jsonString = "[]";
        if (G4Utils.isEmpty(pObject)) {
            // log.warn("传入的Java对象为空,不能将其序列化为Json资料格式.请检查!");
        } else {
            if (pObject instanceof ArrayList) {
                JSONArray jsonArray = JSONArray.fromObject(pObject);
                jsonString = jsonArray.toString();
            } else {
                JSONObject jsonObject = JSONObject.fromObject(pObject);
                jsonString = jsonObject.toString();
            }
        }
        if (log.isInfoEnabled()) {
            log.info("序列化后的JSON资料输出:\n" + jsonString);
        }
        return jsonString;
    }


    /**
     * 将含有日期时间格式的Java对象系列化为Json资料格式<br>
     * Json-Lib在处理日期时间格式是需要实现其JsonValueProcessor接口,所以在这里提供一个重载的方法对含有<br>
     * 日期时间格式的Java对象进行序列化
     * 
     * @param pObject
     *            传入的Java对象
     * @return
     */
    public static final String encodeObject2Json(Object pObject, String pFormatString) {
        String jsonString = "[]";
        if (G4Utils.isEmpty(pObject)) {
            // log.warn("传入的Java对象为空,不能将其序列化为Json资料格式.请检查!");
        } else {
            JsonConfig cfg = new JsonConfig();
            cfg.registerJsonValueProcessor(java.sql.Timestamp.class,
                new JsonValueProcessorImpl(pFormatString));
            cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl(pFormatString));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(pFormatString));
            if (pObject instanceof ArrayList) {
                JSONArray jsonArray = JSONArray.fromObject(pObject, cfg);
                jsonString = jsonArray.toString();
            } else {
                JSONObject jsonObject = JSONObject.fromObject(pObject, cfg);
                jsonString = jsonObject.toString();
            }
        }
        if (log.isInfoEnabled()) {
            log.info("序列化后的JSON资料输出:\n" + jsonString);
        }
        return jsonString;
    }


    /**
     * 将分页信息压入JSON字符串 此类内部使用,不对外暴露
     * 
     * @param JSON字符串
     * @param totalCount
     * @return 返回合并后的字符串
     */
    private static String encodeJson2PageJson(String jsonString, Integer totalCount) {
        jsonString = "{TOTALCOUNT:" + totalCount + ", ROOT:" + jsonString + "}";
        if (log.isInfoEnabled()) {
            log.info("合并后的JSON资料输出:\n" + jsonString);
        }
        return jsonString;
    }


    /**
     * 直接将List转为分页所需要的Json资料格式
     * 
     * @param list
     *            需要编码的List对象
     * @param totalCount
     *            记录总数
     * @param pDataFormat
     *            时间日期格式化,传null则表明List不包含日期时间属性
     */
    public static final String encodeList2PageJson(List list, Integer totalCount, String dataFormat) {
        String subJsonString = "";
        if (G4Utils.isEmpty(dataFormat)) {
            subJsonString = encodeObject2Json(list);
        } else {
            subJsonString = encodeObject2Json(list, dataFormat);
        }
        String jsonString = "{TOTALCOUNT:" + totalCount + ", ROOT:" + subJsonString + "}";
        if (log.isInfoEnabled()) {
            log.info("序列化后的JSON资料输出:\n" + jsonString);
        }
        return jsonString;
    }


//    /**
//     * 将数据系列化为表单数据填充所需的Json格式
//     * 
//     * @param pObject
//     *            待系列化的对象
//     * @param pFormatString
//     *            日期时间格式化,如果为null则认为没有日期时间型字段
//     * @return
//     */
//    public static String encodeDto2FormLoadJson(Dto pDto, String pFormatString) {
//        String jsonString = "";
//        String sunJsonString = "";
//        if (G4Utils.isEmpty(pFormatString)) {
//            sunJsonString = encodeObject2Json(pDto);
//        } else {
//            sunJsonString = encodeObject2Json(pDto, pFormatString);
//        }
//        jsonString =
//                "{success:"
//                        + (G4Utils.isEmpty(pDto.getAsString("success")) ? "true" : pDto
//                            .getAsString("success")) + ",data:" + sunJsonString + "}";
//        if (log.isInfoEnabled()) {
//            log.info("序列化后的JSON资料输出:\n" + jsonString);
//        }
//        return jsonString;
//    }


//    /**
//     * 将单一Json对象解析为DTOJava对象
//     * 
//     * @param jsonString
//     *            简单的Json对象
//     * @return dto
//     */
//    public static Dto parseSingleJson2Dto(String jsonString) {
//        Dto dto = new BaseDto();
//        if (G4Utils.isEmpty(jsonString)) {
//            return dto;
//        }
//        JSONObject jb = JSONObject.fromObject(jsonString);
//        dto = (BaseDto) JSONObject.toBean(jb, BaseDto.class);
//        return dto;
//    }


    public static JSONObject encodeList2JSONArray(List list, Integer totalCount, String dataFormat) {
        JSONArray jsonArray;
        JsonConfig cfg = new JsonConfig();
        cfg.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Long.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Double.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(BigDecimal.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Boolean.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });

        if (G4Utils.isEmpty(dataFormat)) {
            cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessorImpl("HH:mm:ss"));
            cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));

            jsonArray = JSONArray.fromObject(list, cfg);
        } else {
            cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessorImpl(dataFormat));
            cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl(dataFormat));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(dataFormat));

            jsonArray = JSONArray.fromObject(list, cfg);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.element("root", jsonArray);
        jsonObject.element("total", totalCount);
        return jsonObject;
    }


    public static JSONArray encodeList2JSONArray(List list) {
        JsonConfig cfg = new JsonConfig();
        cfg.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Long.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Double.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(BigDecimal.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Boolean.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });

        cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessorImpl("HH:mm:ss"));
        cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));
        cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));

        return JSONArray.fromObject(list, cfg);
    }


    public static JSONObject encodeObject2JSONObject(Object object, String dataFormat) {
        JSONObject jsonObject;
        JsonConfig cfg = new JsonConfig();
        cfg.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Long.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Double.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(BigDecimal.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Boolean.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });

        if (G4Utils.isEmpty(dataFormat)) {

            cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessorImpl("HH:mm:ss"));
            cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));

            jsonObject = JSONObject.fromObject(object, cfg);
        } else {
            cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessorImpl(dataFormat));
            cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl(dataFormat));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(dataFormat));

            jsonObject = JSONObject.fromObject(object, cfg);
        }

        return jsonObject;
    }


    /**
     * 将复杂Json资料格式转换为List对象
     * 
     * @param jsonString
     *            复杂Json对象,格式必须符合如下契约<br>
     *            {"1":{"name":"托尼.贾","age":"27"},
     *            "2":{"name":"甄子丹","age":"72"}}
     * @return List
     */
//    public static List parseJson2List(String jsonString) {
//        List list = new ArrayList();
//        JSONObject jbJsonObject = JSONObject.fromObject(jsonString);
//        Iterator iterator = jbJsonObject.keySet().iterator();
//        while (iterator.hasNext()) {
//            Dto dto = parseSingleJson2Dto(jbJsonObject.getString(iterator.next().toString()));
//            list.add(dto);
//        }
//        return list;
//    }


    public static JSONObject encodeList2JSONArray(List list, Integer totalCount, String[] ignoreKeys,
            String dataFormat) {
        JSONArray jsonArray;
        JsonConfig cfg = new JsonConfig();
        cfg.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Long.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Double.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(BigDecimal.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });
        cfg.registerDefaultValueProcessor(Boolean.class, new DefaultValueProcessor() {
            public Object getDefaultValue(Class type) {
                return null;
            }
        });

        if (ignoreKeys != null && ignoreKeys.length != 0) {
            cfg.setIgnoreDefaultExcludes(false);
            cfg.setExcludes(ignoreKeys);
        }
        if (G4Utils.isEmpty(dataFormat)) {
            cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessorImpl());
            cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl("yyyy-MM-dd"));

            jsonArray = JSONArray.fromObject(list, cfg);
        } else {
            cfg.registerJsonValueProcessor(java.sql.Timestamp.class, new JsonValueProcessorImpl(dataFormat));
            cfg.registerJsonValueProcessor(java.util.Date.class, new JsonValueProcessorImpl(dataFormat));
            cfg.registerJsonValueProcessor(java.sql.Date.class, new JsonValueProcessorImpl(dataFormat));

            jsonArray = JSONArray.fromObject(list, cfg);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.element("root", jsonArray);
        if (totalCount != null)
            jsonObject.element("total", totalCount);
        return jsonObject;
    }


    // 从json格式的字符串转换为某类对象的数组
    public static Object getArray(String s, Class cls) {
        JSONArray ja = JSONArray.fromObject(s);
        return JSONArray.toArray(ja, cls);
    }


    public static Object getBean(String s, Class beanClass) {
        JSONObject ja = JSONObject.fromObject(s);
        return JSONObject.toBean(ja, beanClass);
    }


    public static Map convertJsonParamsToMap(String jsonStr) {
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        Map map = (Map) jsonObject;
        return map;
    }


    /**
     * 把数据对象转换成json字符串 DTO对象形如：{"id" : idValue, "name" : nameValue, ...}
     * 数组对象形如：[{}, {}, {}, ...] map对象形如：{key1 : {"id" : idValue, "name" :
     * nameValue, ...}, key2 : {}, ...}
     * 
     * @param object
     * @return
     */
    public static String getJSONString(Object object) throws Exception {
        String jsonString = null;
        // 日期值处理器
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsonDateValueProcessor());
        if (object != null) {
            if (object instanceof Collection || object instanceof Object[]) {
                jsonString = JSONArray.fromObject(object, jsonConfig).toString();
            } else {
                jsonString = JSONObject.fromObject(object, jsonConfig).toString();
            }
        }
        return jsonString == null ? "{}" : jsonString;
    }


    /**
     * 将json转换为FormatMap
     * 
     * @param formatMapStr
     * @return
     */
    public static Map getFormatMapFromJson(String formatMapStr) {
        JSONObject jsonObj = JSONObject.fromObject(formatMapStr);
        return (Map) JSONObject.toBean(jsonObj, Map.class);
    }


    /**
     * 将json转换为list
     * 
     * @param jsonString
     * @return
     */
    public static List getListFromJson(String jsonString) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        List list = new ArrayList(Arrays.asList(jsonArray.toArray()));
        int i = 0;
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Object object = (Object) iterator.next();
            if (object instanceof JSONArray) {
                object = new ArrayList(Arrays.asList(((JSONArray) object).toArray()));
                list.set(i, object);
            }
            i++;
        }
        return list;
    }


    public static <T> List<T> getObjList(String jsonStr, Class<? extends T> clazz) {
        List<T> resultList = new ArrayList<T>();
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        Object tmpObj = null;
        JSONObject jsonObject = null;

        for (int i = 0; i < jsonArray.size(); i++) {
            tmpObj = jsonArray.get(i);
            jsonObject = JSONObject.fromObject(tmpObj);
            resultList.add((T) jsonObject.toBean(jsonObject, clazz));
        }

        return resultList;
    }


    /** */
    /**
     * 从json HASH表达式中获取一个map，改map支持嵌套功能
     * 
     * @param jsonString
     * @return
     */
    public static Map getMap4Json(String jsonString) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Iterator keyIter = jsonObject.keys();
        String key;
        Object value;
        Map valueMap = new HashMap();

        while (keyIter.hasNext()) {
            key = (String) keyIter.next();
            value = jsonObject.get(key);
            valueMap.put(key, value);
        }

        return valueMap;
    }


    public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException,
            InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }


    public static Map jsonToMap(String jsonString, Class beanClass) {

        return jsonToMap(jsonString, beanClass, "yyyy-MM-dd");

    }


    public static Map jsonToMap(String jsonString, Class beanClass, String pFormatString) {
        BeanInfo beanInfo = null;
        Map returnMap = new HashMap();
        try {
            beanInfo = Introspector.getBeanInfo(beanClass);
            JSONObject ja = JSONObject.fromObject(jsonString);
            String[] dateFormats = new String[] { "yyyy-MM-dd" };
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));

            Object bean = JSONObject.toBean(ja, beanClass);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();

                if (!propertyName.equals("class")) {

                    if (List.class.equals(descriptor.getPropertyType())) {// 为list时，对其做特殊处理
                        ParameterizedType pt = null;
                        try {
                            Field field = beanClass.getField(propertyName);
                            field.setAccessible(true);
                            pt = (ParameterizedType) field.getGenericType();
                            Method readMethod = descriptor.getReadMethod();
                            Object result = readMethod.invoke(bean, new Object[0]);
                            if (result != null) {
                                Type type = pt.getActualTypeArguments()[0];
                                Class<T> entityClass = (Class<T>) type;
                                JSONArray jsonA = JSONArray.fromObject(result);
                                returnMap.put(propertyName, JSONArray.toList(jsonA, entityClass));

                            } else {
                                returnMap.put(propertyName, "");
                            }

                        } catch (NoSuchFieldException | SecurityException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Method readMethod = descriptor.getReadMethod();
                        Object result = readMethod.invoke(bean, new Object[0]);
                        if (result != null) {
                            returnMap.put(propertyName, result);
                        } else {
                            returnMap.put(propertyName, "");
                        }
                    }
                }

            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();

        }

        return returnMap;
    }


    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * 
     * @param beanClass
     *            要转化的类型
     * @param map
     *            包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object mapToObject(Class beanClass, Map map) throws IntrospectionException,
            IllegalAccessException, InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass); // 获取类属性
        Object obj = beanClass.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }
    
//	public static JSONObject parseObjToJSONObject(Object obj, SelectVo selectVo)
//			throws IllegalArgumentException, IllegalAccessException {
//		JSONObject jsonObject = new JSONObject();
//		Field[] fields = obj.getClass().getDeclaredFields();
//		for (Field field : fields) {
//			field.setAccessible(true); // 设置些属性是可以访问的
//			if (field.getName().equals(selectVo.getCode())) {
//				jsonObject.element("code", String.valueOf(field.get(obj)));
//			} else if (field.getName().equals(selectVo.getCodedesc())) {
//			    if(selectVo.getValueflag().equals("2")){
//			    	jsonObject.element("codedesc", jsonObject.getString("code")+selectVo.getValueflagvalue()+field.get(obj));
//			    }else if(selectVo.getValueflag().equals("3")){
//			    	jsonObject.element("codedesc",String.valueOf(field.get(obj))+ selectVo.getValueflagvalue()+jsonObject.getString("code"));
//			    }else{
//			    	jsonObject.element("codedesc", String.valueOf(field.get(obj)));
//			    }
//				
//			}
//		}
//		return jsonObject;
//	}

}
