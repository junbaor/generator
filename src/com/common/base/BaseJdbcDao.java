package com.common.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codegen.po.EadtSysDatabaseSource;
import com.common.utils.G4Utils;


/**
 * Created by IntelliJ IDEA. User: weizhao.dong Date: 12-5-16 Time: 下午3:24 To
 * change this template use File | Settings | File Templates.
 */
public class BaseJdbcDao {
    private static Log log = LogFactory.getLog(BaseJdbcDao.class);
    public static String key;
    public static Connection con;
    private static PreparedStatement ps;
    private static ResultSet rs;


    /**
     * 封装数据库的查操作
     * 
     * @param c
     *            反射类的对象
     * @param sql
     *            操作的查询SQL语句
     * @param parameters
     *            参数集，调用时无则写null
     * @return list 集合
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SQLException
     * @throws InstantiationException
     */
    public static List list(Class c, String sql, List<Object> parameters) {
        List list = new ArrayList();
        try {
            Object o = null;
            ps = con.prepareStatement(sql);
            setParameter(parameters);
            rs = ps.executeQuery();
            // 得到列信息ResultSetMetaDate对象
            ResultSetMetaData rsmd = rs.getMetaData();

            // 创建一个String的数组,用来保存所有的列名
            // rsmd.getColumnCount()为当前结果集中的列的总数,所以定义为长度
            String[] cName = new String[rsmd.getColumnCount()];

            for (int i = 0; i < cName.length; i++) {
                cName[i] = rsmd.getColumnName(i + 1);
            }
            // 得到反射类中的所有的方法
            Method[] methods = c.getMethods();

            while (rs.next()) {
                // 如果结果集得到了数据，则实例一个对象
                o = c.newInstance();
                for (int i = 0; i < cName.length; i++) {
                    for (Method m : methods) {
                        // 把从结果集中得到列名前面加上"set",并把第一位设置为大写，加上后面的，成为一个set的名称，
                        // 然后用反射得到的方法名与之比较，相同的话则激活此方法
                        if (m.getName().equals(
                            "set" + cName[i].substring(0, 1).toUpperCase()
                                    + cName[i].substring(1).toLowerCase())) {
                            String columnLabel = rsmd.getColumnName(i + 1).toLowerCase();
                            // 激活得到方法,并设置值
                            typeConver(m, o, columnLabel);
                        }
                    }
                }
                // 添加到lismethodst集合中
                list.add(o);
            }
        } catch (SQLException e) {
            log.debug(e);
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } catch (InstantiationException e) {
            log.debug(e);
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } catch (IllegalAccessException e) {
            log.debug(e);
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } finally {
            dataPsClose();
        }
        return list;
    }


    /**
     * 原生sql查询
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List queryForList(String sql) throws SQLException {
        List list = null;
        try {
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(sql);
            list = new ArrayList();
            ResultSetMetaData md = result.getMetaData();
            while (result.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    list.add(result.getString(i));
                }
            }
        } catch (SQLException e) {
            log.error("错误sql:" + sql);
            log.error(e);
            e.printStackTrace();
            throw e;
        } finally {
            dataClose();
        }

        return (list);
    }


    public static int getMaxId(String sql) throws SQLException {
        int count = 0;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug(e);
            e.printStackTrace();
            throw e;
        } finally {
            dataPsClose();
        }
        return count;
    }


    /**
     * 封装数据库的增，删，改操作
     * 
     * @param sql
     *            操作的SQL语句
     * @param parameters
     *            参数集
     * @return 影响行数
     */
    public static int update(String sql, List<Object> parameters) throws SQLException {
        int result = 0;
        try {
            ps = con.prepareStatement(sql);
            setParameter(parameters);
            result = ps.executeUpdate();
            log.info("执行sql:" + sql);

        } catch (SQLException e) {
            Rollback();
            log.debug(e);
            throw e;
        } finally {
            closePs(ps);
        }
        return result;
    }


    /**
     * 设置参数
     * 
     * @param parameters
     *            参数集
     * @throws SQLException
     *             抛出SQL异常
     */
    private static void setParameter(List<Object> parameters) throws SQLException {
        if (parameters != null && parameters.size() > 0) {
            for (int i = 0; i < parameters.size(); i++) {
                converse(ps, i, parameters);
            }
        }
    }


    /**
     * 得到Connection连接
     * 
     * @return Connection连接
     * @throws SQLException
     */
    public static Connection getCon() throws SQLException {
        return con;
    }


    /**
     * 关闭程序中的Connectin 连接
     * 
     * @param
     */
    public static void closeCon() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭程序中PreparedStatement对象
     * 
     * @param ps
     *            PreparedStatement对象
     */
    public static void closePs(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭程序中ResultSet 对象
     * 
     * @param rs
     *            ResultSet对象
     */
    public static void closeRs(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 开始事务
     * 
     * @param
     */
    public static void beginTransaction() {
        try {
            if (con != null) {
                con.setAutoCommit(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 回滚
     */
    public static void Rollback() {
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * //提交
     * <p/>
     * conn
     */
    public static void commit() {
        if (con != null) {
            try {
                con.commit();
                con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void dataPsClose() {
        try {
            if (ps != null) {
                ps.close();
                ps = null;
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        }
    }


    /**
     * 关闭
     * 
     * @param
     * @param
     * @param
     * @throws SQLException
     */
    public static void dataClose() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 声明基本类型
     */
    public static final String[] types = new String[] { "java.lang.Integer", "java.lang.Float",
                                                       "java.lang.Boolean", "java.lang.Short",
                                                       "java.lang.Byte", "java.lang.Long",
                                                       "java.lang.Double", "java.util.Date",
                                                       "java.lang.String" };


    /**
     * 动态赋值
     * 
     * @param ps
     * @param i
     * @param parameters
     * @throws NumberFormatException
     * @throws SQLException
     */
    public static void converse(PreparedStatement ps, int i, List<Object> parameters)
            throws NumberFormatException, SQLException {
        String value = parameters.get(i).toString();
        String classname = parameters.get(i).getClass().getName();
        if (types[0].equals(classname)) {
            ps.setInt(i + 1, Integer.parseInt(value));
        } else if (types[1].equals(classname)) {
            ps.setFloat(i + 1, Float.parseFloat(value));
        } else if (types[2].equals(classname)) {
            ps.setBoolean(i + 1, Boolean.parseBoolean(value));
        } else if (types[3].equals(classname)) {
            ps.setShort(i + 1, Short.parseShort(value));
        } else if (types[4].equals(classname)) {
            ps.setByte(i + 1, Byte.parseByte(value));
        } else if (types[5].equals(classname)) {
            ps.setLong(i + 1, Long.parseLong(value));
        } else if (types[6].equals(classname)) {
            ps.setDouble(i + 1, Double.parseDouble(value));
        } else {
            ps.setString(i + 1, value);
        }
    }


    /**
     * 类型转换器
     * 
     * @param m
     * @param o
     * @param clumnName
     */
    public static void typeConver(Method m, Object o, String clumnName) {
        String cname = m.toString();
        int startIndex = cname.lastIndexOf(".");
        int endIndex = cname.indexOf(")");

        String value = cname.substring(startIndex + 1, endIndex);
        try {
            if (G4Utils.isEmpty(rs.getString(clumnName))) {
                return;
            }
            if (value.equals("Integer")) {
                m.invoke(o, Integer.valueOf(rs.getString(clumnName)));
            }
            if (value.equals("Long")) {
                m.invoke(o, Long.valueOf(rs.getString(clumnName)));
            }
            if (value.equals("Short")) {
                m.invoke(o, Short.valueOf(rs.getString(clumnName)));
            }
            if (value.equals("Double")) {
                m.invoke(o, Double.valueOf(rs.getString(clumnName)));
            }
            if (value.equals("Float")) {
                m.invoke(o, Float.valueOf(rs.getString(clumnName)));
            }
            if (value.equals("String")) {
                m.invoke(o, rs.getString(clumnName));
            }
//            if (value.equals("Date")) {
//                m.invoke(o, DateUtil.parseStrTime(rs.getString(clumnName), "yyyy-MM-dd"));
//            }
            if (value.equals("Byte")) {
                m.invoke(o, Byte.valueOf(rs.getString(clumnName)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* *//**
     * 反射生成sql1 例句：insert into cdjg (id,name) values(1,'222');
     * 
     * @param c
     *            对应的类
     * @return
     */
    /*
     * public static String createInsertSql(Class<?> c) { StringBuffer sql = new
     * StringBuffer(); String sqlString = ""; String valuesString = "";
     * StringBuffer values = new StringBuffer(); try { Object o =
     * c.newInstance(); int lastIndex = c.getName().lastIndexOf("."); String
     * tableName = c.getName().substring(lastIndex + 1).toLowerCase();
     * sql.append("insert into " + tableName + "("); Field[] fiels =
     * c.getDeclaredFields(); Method m = null; Object obj = null; for (Field f :
     * fiels) { if (f.toString().contains("private")) { String str =
     * f.getName(); //私有属性名 m = o.getClass().getMethod("get" + str.substring(0,
     * 1).toUpperCase() + str.substring(1)); obj = m.invoke(o); if
     * (G4Utils.isNotEmpty(obj)) { sql.append(f.getName() + ",");
     * values.append(obj + ","); } } } sqlString = sql.toString().substring(0,
     * sql.lastIndexOf(",")) + ")"; valuesString = " values(" +
     * values.substring(0, values.lastIndexOf(",")) + ")"; sqlString = sqlString
     * + valuesString; System.out.print(sqlString); } catch (Exception e) {
     * e.printStackTrace(); } return sqlString; }
     */

    /**
     * 反射生成sql2 增加了数据库类型判断 例句：insert into cdjg (id,name) values(1,'222');
     * 
     * @param c
     * @param dtype
     *            数据库类型
     * @param semicolon
     *            分号
     * @return
     */
    public static String createInsertSql(Object obj1, Class<?> c, String dtype, String semicolon) {
        StringBuffer sql = new StringBuffer();
        String sqlString = "";
        String valuesString = "";
        StringBuffer values = new StringBuffer();
        try {
            Object o = obj1;
            int lastIndex = c.getName().lastIndexOf(".");
            String tableName = c.getName().substring(lastIndex + 1).toLowerCase();
            sql.append("insert into " + tableName + "(");
            Field[] fiels = c.getDeclaredFields();
            Method m = null;
            Object obj = null;
            for (Field f : fiels) {
                if (f.toString().contains("private")) {
                    String str = f.getName(); // 私有属性名
                    m = o.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
                    obj = m.invoke(o);
                    if (G4Utils.isNotEmpty(obj)) {
                        if (dtype.equalsIgnoreCase("oracle") && f.getName().equalsIgnoreCase("row")) {
                            sql.append("\"" + f.getName().toUpperCase() + "\",");
                        } else {
                            sql.append(f.getName() + ",");
                        }
                        String type = f.getType().getName(); // 类型名称
                        if (types[7].equalsIgnoreCase(type)) {
                            if (dtype.equalsIgnoreCase("oracle")) {
                                values.append("TO_DATE('" + G4Utils.getSplitLast(obj.toString().trim(), ".")
                                        + "','YYYY_MM_DD HH24:MI:SS'),");
                            } else {
                                values.append("'" + G4Utils.getSplitLast(obj.toString().trim(), ".") + "',");
                            }
                        } else if (types[8].equalsIgnoreCase(type)) {
                            if (obj.toString().trim().equals("")) {
                                values.append("' " + obj.toString().trim() + "',");
                            } else {
                                values.append("'" + obj.toString().trim() + "',");
                            }
                        } else {
                            values.append(obj + ",");
                        }
                    }/*
                      * else { values.append(null + ","); }
                      */
                }
            }
            sqlString = sql.toString().substring(0, sql.lastIndexOf(",")) + ")";
            valuesString = " values(" + values.substring(0, values.lastIndexOf(",")) + ")" + semicolon;
            sqlString = sqlString + valuesString;
            log.info("sql:" + sqlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlString;
    }


    /**
     * 反射生成sql2 增加了数据库类型判断 例句：insert into cdjg (id,name) values(1,'222');
     * 
     * @param c
     * @param dtype
     *            数据库类型
     * @param semicolon
     *            分号
     * @param tname
     *            表明
     * @return
     */
    public static String createInsertSql(Object obj1, Class<?> c, String dtype, String semicolon, String tname) {
        StringBuffer sql = new StringBuffer();
        String sqlString = "";
        String valuesString = "";
        StringBuffer values = new StringBuffer();
        try {
            Object o = obj1;
            int lastIndex = c.getName().lastIndexOf(".");
            String tableName = c.getName().substring(lastIndex + 1).toLowerCase();
            if (G4Utils.isNotEmpty(tname)) {
                tableName = tname;
            }
            sql.append("insert into " + tableName + "(");
            Field[] fiels = c.getDeclaredFields();
            Method m = null;
            Object obj = null;
            for (Field f : fiels) {
                if (f.toString().contains("private")) {
                    String str = f.getName(); // 私有属性名
                    m = o.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
                    obj = m.invoke(o);
                    if (G4Utils.isNotEmpty(obj)) {
                        if (dtype.equalsIgnoreCase("oracle") && f.getName().equalsIgnoreCase("row")) {
                            sql.append("\"" + f.getName().toUpperCase() + "\",");
                        } else {
                            sql.append(f.getName() + ",");
                        }
                        String type = f.getType().getName(); // 类型名称
                        if (types[7].equalsIgnoreCase(type)) {
                            if (dtype.equalsIgnoreCase("oracle")) {
                                values.append("TO_DATE('" + G4Utils.getSplitLast(obj.toString().trim(), ".")
                                        + "','YYYY_MM_DD HH24:MI:SS'),");
                            } else {
                                values.append("'" + G4Utils.getSplitLast(obj.toString().trim(), ".") + "',");
                            }
                        } else if (types[8].equalsIgnoreCase(type)) {
                            if (obj.toString().trim().equals("")) {
                                values.append("' " + obj.toString().trim() + "',");
                            } else {
                                values.append("'" + obj.toString().trim() + "',");
                            }
                        } else {
                            values.append(obj + ",");
                        }
                    }/*
                      * else { values.append(null + ","); }
                      */
                }
            }
            sqlString = sql.toString().substring(0, sql.lastIndexOf(",")) + ")";
            valuesString = " values(" + values.substring(0, values.lastIndexOf(",")) + ")" + semicolon;
            sqlString = sqlString + valuesString;
            log.info("sql:" + sqlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlString;
    }


    /**
     * 动态生成sql 例句：insert into cdjg (id,name) values(1,'222');
     * 
     * @param obj1
     * @return
     */
    public static String createInsertSql(Object obj1, Class<?> c) {
        StringBuffer sql = new StringBuffer();
        String sqlString = "";
        String valuesString = "";
        StringBuffer values = new StringBuffer();
        try {
            Object o = obj1;
            int lastIndex = c.getName().lastIndexOf(".");
            String tableName = c.getName().substring(lastIndex + 1).toLowerCase();
            sql.append("insert into " + tableName + "(");
            Field[] fiels = c.getDeclaredFields();
            Method m = null;
            Object obj = null;
            for (Field f : fiels) {
                if (f.toString().contains("private")) {
                    String str = f.getName(); // 私有属性名
                    m = o.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
                    obj = m.invoke(o);
                    sql.append(f.getName() + ",");
                    String type = f.getType().getName(); // 类型名称
                    if (G4Utils.isNotEmpty(obj)) {
                        if (types[7].equalsIgnoreCase(type) || types[8].equalsIgnoreCase(type)) {
                            values.append("'" + obj.toString() + "',");
                        } else {
                            values.append(obj + ",");
                        }
                    } else {
                        values.append(null + ",");
                    }
                }
            }
            sqlString = sql.toString().substring(0, sql.lastIndexOf(",")) + ")";
            valuesString = " values(" + values.substring(0, values.lastIndexOf(",")) + ");";
            sqlString = sqlString + valuesString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlString;
    }


    /**
     * 生成特定的sql 例句：insert into CDJG
     * (id,TIMESTAMP,xmdm,xmmc,JYJB,CDDM,ZCD,CLCX,JYDM) select max(id)+1 as id,
     * TO_DATE('2012-09-25 11:06:42','YYYY_MM_DD HH24:MI:SS') as timestamp,
     * '654321' as xmdm from CDJG；
     * 
     * @param obj1
     * @param c
     * @param dtype
     *            数据库类型
     * @param semicolon
     *            分号
     * @return
     */
    public static String cSpeciFicSql(Object obj1, Class<?> c, String dtype, String semicolon) {
        StringBuffer sql = new StringBuffer();
        String sqlString = "";
        String valuesString = "";
        StringBuffer values = new StringBuffer();
        try {
            Object o = obj1;
            int lastIndex = c.getName().lastIndexOf(".");
            String tableName = c.getName().substring(lastIndex + 1).toLowerCase();
            sql.append("insert into " + tableName + "(");
            Field[] fiels = c.getDeclaredFields();
            Method m = null;
            Object obj = null;
            for (Field f : fiels) {
                if (f.toString().contains("private")) {
                    String str = f.getName(); // 私有属性名
                    m = o.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
                    obj = m.invoke(o);
                    String type = f.getType().getName(); // 类型名称
                    if (obj != null) {
                        if (dtype.equalsIgnoreCase("oracle") && f.getName().equalsIgnoreCase("row")) {
                            sql.append("\"" + f.getName().toUpperCase() + "\",");
                        } else {
                            sql.append(f.getName() + ",");
                        }
                        if (f.getName().equalsIgnoreCase("id")) {
                            values.append("select max(id)").append(" + 1 ").append(" as ").append("id")
                                .append(",");
                        } else {
                            if (types[7].equalsIgnoreCase(type)) { // 日期类型
                                if (dtype.equalsIgnoreCase("oracle")) {
                                    values
                                        .append(
                                            "TO_DATE('" + G4Utils.getSplitLast(obj.toString().trim(), ".")
                                                    + "','YYYY_MM_DD HH24:MI:SS')").append(" as ")
                                        .append(f.getName()).append(",");
                                } else {
                                    values.append("'")
                                        .append(G4Utils.getSplitLast(obj.toString().trim(), ".")).append("'")
                                        .append(" as ").append(f.getName()).append(",");
                                }
                            } else if (types[8].equalsIgnoreCase(type)) {
                                if (obj.toString().trim().equals("")) {
                                    values.append("' ").append(obj.toString().trim()).append("' as ")
                                        .append(f.getName()).append(",");
                                } else {
                                    values.append("'").append(obj.toString().trim().replaceAll("'", "\""))
                                        .append("' as ").append(f.getName()).append(",");
                                }
                            } else {
                                if (dtype.equalsIgnoreCase("oracle") && f.getName().equalsIgnoreCase("row"))
                                    values.append(obj).append(" as ")
                                        .append("\"" + f.getName().toUpperCase()).append("\",");
                                else
                                    values.append(obj).append(" as ").append(f.getName()).append(",");
                            }
                        }
                    }
                }
            }
            sqlString = sql.toString().substring(0, sql.length() - 1) + ")";
            valuesString = " " + values.substring(0, values.length() - 1) + " from " + tableName + semicolon;
            sqlString = sqlString + valuesString;
            log.info("sql:" + sqlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlString;
    }


    /**
     * 生成特定的sql 例句：insert into CDJG
     * (id,TIMESTAMP,xmdm,xmmc,JYJB,CDDM,ZCD,CLCX,JYDM) select max(id)+1 as id,
     * TO_DATE('2012-09-25 11:06:42','YYYY_MM_DD HH24:MI:SS') as timestamp,
     * '654321' as xmdm from CDJG；
     * 
     * @param obj1
     * @param c
     * @param dtype
     *            数据库类型
     * @param tname
     *            別名，如果為空，默认取类名
     * @param semicolon
     *            分号
     * @return
     */
    public static String cSpeciFicSql(Object obj1, Class<?> c, String dtype, String semicolon, String tname) {
        StringBuffer sql = new StringBuffer();
        String sqlString = "";
        String valuesString = "";
        StringBuffer values = new StringBuffer();
        try {
            Object o = obj1;
            int lastIndex = c.getName().lastIndexOf(".");
            String tableName = c.getName().substring(lastIndex + 1).toLowerCase();
            if (G4Utils.isNotEmpty(tname)) {
                tableName = tname;
            }
            sql.append("insert into " + tableName + "(");
            Field[] fiels = c.getDeclaredFields();
            Method m = null;
            Object obj = null;
            for (Field f : fiels) {
                if (f.toString().contains("private")) {
                    String str = f.getName(); // 私有属性名
                    m = o.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
                    obj = m.invoke(o);
                    String type = f.getType().getName(); // 类型名称
                    if (obj != null) {
                        if (dtype.equalsIgnoreCase("oracle") && f.getName().equalsIgnoreCase("row")) {
                            sql.append("\"" + f.getName().toUpperCase() + "\",");
                        } else {
                            sql.append(f.getName() + ",");
                        }
                        if (f.getName().equalsIgnoreCase("id")) {
                            values.append("select max(id)").append(" + 1 ").append(" as ").append("id")
                                .append(",");
                        } else {
                            if (types[7].equalsIgnoreCase(type)) { // 日期类型
                                if (dtype.equalsIgnoreCase("oracle")) {
                                    values
                                        .append(
                                            "TO_DATE('" + G4Utils.getSplitLast(obj.toString().trim(), ".")
                                                    + "','YYYY_MM_DD HH24:MI:SS')").append(" as ")
                                        .append(f.getName()).append(",");
                                } else {
                                    values.append("'")
                                        .append(G4Utils.getSplitLast(obj.toString().trim(), ".")).append("'")
                                        .append(" as ").append(f.getName()).append(",");
                                }
                            } else if (types[8].equalsIgnoreCase(type)) {
                                if (obj.toString().trim().equals("")) {
                                    values.append("' ").append(obj.toString().trim()).append("' as ")
                                        .append(f.getName()).append(",");
                                } else {
                                    values.append("'").append(obj.toString().trim().replaceAll("'", "\""))
                                        .append("' as ").append(f.getName()).append(",");
                                }
                            } else {
                                if (dtype.equalsIgnoreCase("oracle") && f.getName().equalsIgnoreCase("row"))
                                    values.append(obj).append(" as ")
                                        .append("\"" + f.getName().toUpperCase()).append("\",");
                                else
                                    values.append(obj).append(" as ").append(f.getName()).append(",");
                            }
                        }
                    }
                }
            }
            sqlString = sql.toString().substring(0, sql.length() - 1) + ")";
            valuesString = " " + values.substring(0, values.length() - 1) + " from " + tableName + semicolon;
            sqlString = sqlString + valuesString;
            log.info("sql:" + sqlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlString;
    }


    /**
     * 查找一个文件下的所有sql文件，并执行
     * 
     * @param root
     */
    public static void findFileToAdd(String root) throws IOException, SQLException {
        File file = new File(root);
        File[] files = file.listFiles();
        BufferedReader bufferedReader = null;
        for (File file1 : files) {
            bufferedReader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(root + "\\"
                            + file1.getName()), "utf-8"));
            String data = null;
            while ((data = bufferedReader.readLine()) != null) {
                update(G4Utils.getSplitLast(data, ";"), null);
            }
            bufferedReader.close();
        }
    }


    public static boolean Connection(EadtSysDatabaseSource eadtSysDatabaseSource)
            throws ClassNotFoundException, SQLException {
        boolean isconnect = true;
        try {
            Class.forName(eadtSysDatabaseSource.getDbdriver());
            con =
                    DriverManager.getConnection(eadtSysDatabaseSource.getConnecturl(),
                        eadtSysDatabaseSource.getUsername(), eadtSysDatabaseSource.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            isconnect = false;
            throw e;

        } catch (SQLException e) {
            e.printStackTrace();
            isconnect = false;
            throw e;
        }
        return isconnect;
    }
}
