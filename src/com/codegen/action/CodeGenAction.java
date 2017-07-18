package com.codegen.action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.rapid_framework.generator.GeneratorFacade;
import org.rapid_framework.generator.GeneratorProperties;
import org.rapid_framework.generator.provider.db.table.TableFactory;
import org.rapid_framework.generator.provider.db.table.model.ColumnWeb;
import org.rapid_framework.generator.provider.db.table.model.ForeignLink;
import org.rapid_framework.generator.provider.db.table.model.TableWeb;

import com.codegen.po.EadtSysDatabaseSource;
import com.codegen.service.EadtSysDatabaseSourceService;
import com.common.base.BaseAction;
import com.common.base.DynamicDataSource;
import com.common.utils.SpringBeanLoader;


/**
 * Created by IntelliJ IDEA. User: ritchrs Date: 11-6-7 Time: 上午10:09 desc:
 */
public class CodeGenAction extends BaseAction {
    EadtSysDatabaseSourceService eadtSysDatabaseSourceService =
            (EadtSysDatabaseSourceService) SpringBeanLoader.getSpringBean("eadtSysDatabaseSourceService");
    DynamicDataSource dataSource = (DynamicDataSource) SpringBeanLoader.getSpringBean("defaultDataSource");
    private JSON resultArray;


    public JSON getResultArray() {
        return resultArray;
    }


    public void setResultArray(JSON resultArray) {
        this.resultArray = resultArray;
    }


    /**
     * 切换数据源
     */
    public String setDataSource() {
        HttpServletRequest request = ServletActionContext.getRequest();
        JSONObject returnObject = new JSONObject();
        try {
            String sid = request.getParameter("sid"); // 获取数据源表的id
            EadtSysDatabaseSource eadtSysDatabaseSource = eadtSysDatabaseSourceService.getById(sid);
            dataSource.addDataSource(eadtSysDatabaseSource.getDatabasesourcedbid(), eadtSysDatabaseSource); // 添加数据源
            TableFactory
                .setConnection(dataSource.getConnection(eadtSysDatabaseSource.getDatabasesourcedbid()));// 设置连接
            returnObject.element(SUCCESS, true);
            returnObject.element("msg", "切换成功");
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.element(SUCCESS, false);
            returnObject.element("msg", "切换失败");
        }
        setResultArray(returnObject);
        return SUCCESS;
    }


    /**
     * 数据库表展现
     * 
     * @return
     */
    public String dbTree() {
        List<String> tables = TableFactory.getInstance().getAllTableNames();
        JSONArray jsonArray = new JSONArray();
        for (String table : tables) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.element("id", table);
            jsonObject.element("text", table);
            jsonObject.element("checked", false);
            jsonObject.element("leaf", true);
            jsonArray.element(jsonObject);
        }
        setResultArray(jsonArray);
        return SUCCESS;
    }


    /**
     * 初始化表的生成属性
     * 
     * @return
     */
    public String initTable() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String tableName = request.getParameter("table");

        HashMap<String, TableWeb> tableMap = (HashMap<String, TableWeb>) session.getAttribute("table");
        TableWeb table = null;
        if (tableMap != null) {
            table = tableMap.get(tableName);
        } else {
            tableMap = new HashMap<String, TableWeb>();
            session.setAttribute("table", tableMap);
        }
        if (table == null) {
            table = new TableWeb();
            tableMap.put(tableName, table);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.element("paging", table.getPaging());
        jsonObject.element("columnLock", table.getColumnLock());
        jsonObject.element("edit", table.getCanedit());
        jsonObject.element("stripeRows", table.getStripeRows());
        jsonObject.element("columnLines", table.getColumnLines());
        jsonObject.element("layout", table.getLayout());
        jsonObject.element("colspan", table.getColspan());

        List list = table.getGridPlugins();
        if (list != null && !list.isEmpty()) {
            String girdPlugins = StringUtils.join(list, ",");
            jsonObject.element("girdPlugins", girdPlugins);
        }
        setResultArray(jsonObject);
        return SUCCESS;
    }


    /**
     * 更改表的生成属性
     */
    public void changeTable() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String tableName = request.getParameter("table");

        HashMap<String, TableWeb> tableMap = (HashMap<String, TableWeb>) session.getAttribute("table");
        TableWeb table = tableMap.get(tableName);
        String attribute = request.getParameter("attribute");
        String value = request.getParameter("value");
        if ("paging".equals(attribute)) {
            table.setPaging(Boolean.valueOf(value));
        } else if ("columnLock".equals(attribute)) {
            table.setColumnLock(Boolean.valueOf(value));
        } else if ("edit".equals(attribute)) {
            table.setCanedit(Boolean.valueOf(value));
        } else if ("stripeRows".equals(attribute)) {
            table.setStripeRows(Boolean.valueOf(value));
        } else if ("columnLines".equals(attribute)) {
            table.setColumnLines(Boolean.valueOf(value));
        } else if ("girdPlugins".equals(attribute)) {
            List<String> list = new ArrayList<String>();
            if (StringUtils.isNotBlank(value)) {
                String[] strings = value.split(",");
                Collections.addAll(list, strings);
            }
            table.setGridPlugins(list);
        } else if ("layout".equals(attribute)) {
            table.setLayout(value);
        } else if ("colspan".equals(attribute)) {
            table.setColspan(Integer.valueOf(value));
        }
    }


    /**
     * 展现一张表的列
     * 
     * @return
     * @throws SQLException
     */
    public String dbColumn() throws SQLException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String tableName = request.getParameter("table");

        String cellids = request.getParameter("cellids");
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        if (StringUtils.isNotBlank(cellids)) {
            String[] arr = cellids.split(",");
            for (String str : arr) {
                map.put(str, true);
            }
        }

        HashMap<String, TableWeb> tableMap = (HashMap<String, TableWeb>) session.getAttribute("table");
        TableWeb table = null;
        List<ColumnWeb> columnWebList = null;
        if (tableMap != null) {
            table = tableMap.get(tableName);
        } else {
            tableMap = new HashMap<String, TableWeb>();
            session.setAttribute("table", tableMap);
        }
        if (table != null) {
            columnWebList = table.getColumns();
            if (columnWebList == null) {
                columnWebList = TableFactory.getInstance().getTableColumns(tableName);
                table.setColumns(columnWebList);
            }
        } else {
            table = new TableWeb();
            columnWebList = TableFactory.getInstance().getTableColumns(tableName);
            table.setColumns(columnWebList);
            tableMap.put(tableName, table);
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        int i = 0;
        for (ColumnWeb columnWeb : columnWebList) {
            JSONObject object = new JSONObject();
            object.element("name", columnWeb.getSqlName());
            object.element("remark", columnWeb.getRemarks());
            object.element("type", columnWeb.getSimpleJavaType());
            object.element("size", columnWeb.getSize());
            object.element("pk", columnWeb.isPk());
            object.element("grid", columnWeb.isGridField());
            object.element("search", columnWeb.isSearchField());
            object.element("form", columnWeb.isFormField());
            object.element("xtype", columnWeb.getxType());
            object.element("vtype", columnWeb.getvType());
            object.element("editReadOnly", columnWeb.isEditReadOnly());
            object.element("check", map.get(columnWeb.getSqlName()) == null ? false : true);
            object.element("indexView", ++i);
            object.element("colspan", columnWeb.getColspan());
            jsonArray.element(object);
        }
        jsonObject.element("root", jsonArray);
        setResultArray(jsonObject);
        return SUCCESS;
    }


    /**
     * 保存列中更改
     */
    public void makeParams() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String table = request.getParameter("table");
        String column = request.getParameter("columnName");
        String field = request.getParameter("field");
        String value = request.getParameter("value");

        HashMap<String, TableWeb> tableMap = (HashMap<String, TableWeb>) session.getAttribute("table");
        TableWeb tableWeb = tableMap.get(table);
        List<ColumnWeb> columnWebList = tableWeb.getColumns();
        for (ColumnWeb columnWeb : columnWebList) {
            if (columnWeb.getSqlName().equals(column)) {
                if ("pk".equals(field)) {
                    columnWeb.setPk(Boolean.valueOf(value));
                } else if ("grid".equals(field)) {
                    columnWeb.setGridField(Boolean.valueOf(value));
                } else if ("search".equals(field)) {
                    columnWeb.setSearchField(Boolean.valueOf(value));
                } else if ("form".equals(field)) {
                    columnWeb.setFormField(Boolean.valueOf(value));
                } else if ("xtype".equals(field)) {
                    columnWeb.setxType(value);
                } else if ("vtype".equals(field)) {
                    columnWeb.setvType(StringUtils.isBlank(value) ? null : value);
                } else if ("editReadOnly".equals(field)) {
                    columnWeb.setEditReadOnly(Boolean.valueOf(value));
                } else if ("colspan".equals(field)) {
                    columnWeb.setColspan(Integer.valueOf(value));
                }
            }
        }
    }


    /**
     * 初始化下拉
     */
    public String loadSelectCode() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String table = request.getParameter("table");
        String column = request.getParameter("columnName");

        HashMap<String, TableWeb> tableMap = (HashMap<String, TableWeb>) session.getAttribute("table");
        TableWeb tableWeb = tableMap.get(table);
        List<ColumnWeb> columnWebList = tableWeb.getColumns();
        for (ColumnWeb columnWeb : columnWebList) {
            if (columnWeb.getSqlName().equals(column)) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.element("success", true);
                jsonObject2.element("data", columnWeb.getSelectCode());
                setResultArray(jsonObject2);
                break;
            }
        }
        return SUCCESS;
    }


    /**
     * 保存关联表
     * 
     * @return
     */
    public String saveCode() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String table = request.getParameter("table");
        String column = request.getParameter("columnName");

        String tableSelect = request.getParameter("tableName");
        String field = request.getParameter("field");
        String code = request.getParameter("code");
        String codedesc = request.getParameter("codedesc");
        String fieldvalue = request.getParameter("fieldvalue");

        ForeignLink foreignLink = new ForeignLink(tableSelect, field, code, codedesc, fieldvalue);

        HashMap<String, TableWeb> tableMap = (HashMap<String, TableWeb>) session.getAttribute("table");
        TableWeb tableWeb = tableMap.get(table);
        List<ColumnWeb> columnWebList = tableWeb.getColumns();
        for (ColumnWeb columnWeb : columnWebList) {
            if (columnWeb.getSqlName().equals(column)) {
                columnWeb.setSelectCode(foreignLink);
                break;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.element(SUCCESS, true);
        setResultArray(jsonObject);
        return SUCCESS;
    }


    /**
     * 初始化全局配置
     * 
     * @return
     */
    public String initglobalConfig() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        HashMap<String, String> hashMap = (HashMap<String, String>) session.getAttribute("globalConfig");
        JSONObject jsonObject2 = new JSONObject();
        if (hashMap != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.element("basepackage", hashMap.get("basepackage"));
            jsonObject.element("namespace", hashMap.get("namespace"));
            jsonObject.element("outRoot", hashMap.get("outRoot"));
            jsonObject2.element("data", jsonObject);
        }
        jsonObject2.element("success", true);
        setResultArray(jsonObject2);
        return SUCCESS;
    }


    /**
     * 保存全局配置
     */
    public void saveGlobal(String genWay) {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String basepackage = request.getParameter("basepackage");
        String namespace = request.getParameter("namespace");
       
        HashMap<String, String> hashMap = (HashMap<String, String>) session.getAttribute("globalConfig");
        if (hashMap == null) {
            hashMap = new HashMap<String, String>();
            session.setAttribute("globalConfig", hashMap);
        }
        hashMap.put("basepackage", basepackage);
    	int lastindex=basepackage.lastIndexOf(".");
    	if(lastindex!=-1){
    		hashMap.put("module", basepackage.substring(lastindex+1));
    		if(genWay.equals("dipper_po")){
    			lastindex=basepackage.substring(0,lastindex).lastIndexOf(".");
    		}
        	hashMap.put("basicpackage", basepackage.substring(0,lastindex));
        	
    	}
        hashMap.put("namespace", namespace);
    }


    public String generator() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String tables = request.getParameter("tables");
        String genWay = request.getParameter("generatorMothed");
        String[] tableNames = StringUtils.split(tables, ",");

        GeneratorFacade g = new GeneratorFacade();
        // g.printAllTableNames();//打印数据库中的表名称

        g.deleteOutRootDir();// 删除生成器的输出目录
        saveGlobal(genWay);
        HashMap<String, TableWeb> tableMap = (HashMap<String, TableWeb>) session.getAttribute("table");
        if ("dipper_po".equals(genWay)) {
            g.generateByTables(tableNames, tableMap,
                request.getSession().getServletContext().getRealPath("/template/dipper_po")); // 通过数据库表生成文件,template为模板的根目录
        } else if ("editorGrid".equals(genWay)) {
            g.generateByTables(tableNames, tableMap,
                request.getSession().getServletContext().getRealPath("/template/editorGrid")); // 通过数据库表生成文件,template为模板的根目录
        } else if ("oms_mybatis".equals(genWay)) {
            g.generateByTables(tableNames, tableMap,
                    request.getSession().getServletContext().getRealPath("/template/oms_mybatis")); // 通过数据库表生成文件,template为模板的根目录
        } else {// 以本项目的结构生成
            g.generateByTables(tableNames, tableMap,
                request.getSession().getServletContext().getRealPath("/template/oms_hibernate")); // 通过数据库表生成文件,template为模板的根目录
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.element("success", true);
        setResultArray(jsonObject);
        return SUCCESS;
    }


    public void openOutRoot() throws IOException {
        Runtime.getRuntime().exec("cmd.exe /c start " + GeneratorProperties.getRequiredProperty("outRoot"));
    }

}
