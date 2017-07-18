package com.codegen.action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.rapid_framework.generator.GeneratorFacade;
import org.rapid_framework.generator.GeneratorProperties;
import org.rapid_framework.generator.provider.db.DataSourceProvider;
import org.rapid_framework.generator.provider.db.table.TableFactory;

import com.codegen.po.EadtSysDatabaseSource;
import com.codegen.service.EadtSysDatabaseSourceService;
import com.common.base.BaseAction;
import com.common.base.CommonDbpcDataSourcePools;
import com.common.utils.SpringBeanLoader;
import com.common.utils.ZipCompressorByAnt;


/**
 * Created by IntelliJ IDEA. User: user Date: 12-2-27 Time: 下午5:23 To change
 * this template use File | Settings | File Templates.
 */
public class EsbOrmGenAction extends BaseAction {
    EadtSysDatabaseSourceService eadtSysDatabaseSourceService =
            (EadtSysDatabaseSourceService) SpringBeanLoader.getSpringBean("eadtSysDatabaseSourceService");

    private JSON resultJson;


    public JSON getResultJson() {
        return resultJson;
    }


    public void setResultJson(JSON resultJson) {
        this.resultJson = resultJson;
    }


    public String listDataSource() {
        List<EadtSysDatabaseSource> list = eadtSysDatabaseSourceService.findAll();
        JSONArray jsonArray = new JSONArray();
        for (EadtSysDatabaseSource eadtSysDatabaseSource : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.element("id", eadtSysDatabaseSource.getDatabasesourcedbid());
            jsonObject.element("name", eadtSysDatabaseSource.getConfigname());
            jsonArray.element(jsonObject);
        }
        setResultJson(jsonArray);
        return SUCCESS;
    }


    public String listTable() throws SQLException, ClassNotFoundException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String dsId = request.getParameter("id");
        EadtSysDatabaseSource eadtSysDatabaseSource = eadtSysDatabaseSourceService.getById(dsId);

        /*
         * Class.forName(eadtSysDatabaseSource.getDbdriver()); Connection
         * connection =
         * DriverManager.getConnection(eadtSysDatabaseSource.getConnecturl(),
         * eadtSysDatabaseSource.getUsername(),
         * eadtSysDatabaseSource.getPassword()); DataSource dataSource =
         * DynamicDataSource
         * .createDataSource(edadtSysDatabaseSource.getDbdriver(),
         * eadtSysDatabaseSource.getConnecturl(),
         * eadtSysDatabaseSource.getUsername(),
         * eadtSysDatabaseSource.getPassword());
         */
        DataSource dataSource =
                CommonDbpcDataSourcePools.getDataSource(eadtSysDatabaseSource.getConfigname(),
                    eadtSysDatabaseSource.getDbdriver(), eadtSysDatabaseSource.getConnecturl(),
                    eadtSysDatabaseSource.getUsername(), eadtSysDatabaseSource.getPassword());
        DataSourceProvider.setDataSource(dataSource);
        List<Map<String, String>> tables = TableFactory.getInstance().getAllTableNameAndRemarks();

        JSONObject returnObject = new JSONObject();
        returnObject.element("root", tables);
        setResultJson(returnObject);
        return SUCCESS;
    }


    private String getOracleTableComments(Connection connection, String table) throws SQLException {
        String sql = "SELECT comments FROM user_tab_comments WHERE table_name='" + table + "'";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            String s = resultSet.getString(1);
            resultSet.close();
            return s;
        } else
            resultSet.close();
        return null;
    }


    public void makeOtherProperties() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String basepackage = request.getParameter("basepackage");
        // String namespace = request.getParameter("namespace");
        String outRoot = request.getParameter("outRoot");
        GeneratorProperties.setProperty("basepackage", basepackage);
        // GeneratorProperties.setProperty("namespace", namespace);
        GeneratorProperties.setProperty("outRoot", outRoot);
    }


    public String genCode() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        String ss = request.getParameter("tables");
        boolean bundle = Boolean.valueOf(request.getParameter("bundle"));
        String dataname = request.getParameter("dataname");
        JSONArray jsonArray = JSONArray.fromObject(ss);
        makeOtherProperties();
        GeneratorFacade g = new GeneratorFacade();
        g.deleteOutRootDir();
        String path = bundle ? "template/esb/orm" : "template/esb/orm/${basepackage}/src/main/java";
        g.generateEsbByTables(jsonArray, request.getSession().getServletContext().getRealPath(path));
        TableFactory.map.clear(); // 清空值
        return SUCCESS;
    }

    private static final int BUFFER_SIZE = 16 * 1024;


    public void downLoadFolder() throws FileNotFoundException {
        String outRoot = GeneratorProperties.getRequiredProperty("outRoot");
        String FILE_NAME = outRoot.substring(outRoot.lastIndexOf("/") + 1, outRoot.length());
        String outputfile = outRoot + ".zip";
        // String tmp = outRoot.substring(0, outRoot.lastIndexOf("/"));
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setHeader("Content-disposition", "attachment;filename=generator-output.zip");
        java.io.File file = new java.io.File(outputfile);
        ZipCompressorByAnt zca = new ZipCompressorByAnt(file);
        zca.compressExe(outRoot);
        InputStream input = new FileInputStream(file);
        long contentLength = file.length();
        if (contentLength > 0)
            response.setContentLength((int) contentLength);
        response.setContentType("application/octet-stream");
        try {
            OutputStream output = response.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int retlen;
            while ((retlen = input.read(buffer)) != -1) {
                output.write(buffer, 0, retlen);
                output.flush();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
