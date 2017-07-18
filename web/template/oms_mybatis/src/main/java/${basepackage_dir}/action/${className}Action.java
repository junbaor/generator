<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
package ${basepackage}.action;

import com.common.base.BaseAction;
import com.common.utils.CountOrder;
import com.common.utils.CountOrderUtil;
import com.common.utils.QueryUitl;
import com.common.utils.SelectVo;
import com.common.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import ${basicpackage}.dao.po.${module}.${className}Po;
import ${basepackage}.service.${className}Service;
import ${basepackage}.vo.${className}Query;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.pactera.dipper.page.QueryResult;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.g4studio.common.util.SpringBeanLoader;
import org.g4studio.core.json.JsonHelper;
import org.g4studio.core.util.G4Utils;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

import static org.g4studio.core.util.G4Utils.isEmpty;


public class  ${className}Action extends BaseAction implements Preparable,ModelDriven<Object>{
    private static Logger log = LoggerFactory.getLogger(${className}Action.class);
     ${className}Service  ${classNameLower}Service = (${className}Service) SpringBeanLoader.getSpringBean("${classNameLower}Service");

    private JSON resultJson;
    
    private ${className}Query ${classNameLower}Query;
    
    private ${className}Po ${classNameLower}Po;

    public JSON getResultJson() {
        return resultJson;
    }

    public void setResultJson(JSON resultJson) {
        this.resultJson = resultJson;
    }

    public void prepare() throws Exception {
    	${classNameLower}Query = new ${className}Query();
    }

    public Object getModel() {
        return ${classNameLower}Query;
    }

    public String list() {
        try {
          <#if table.paging>
           CountOrder countOrder = CountOrderUtil.getCountOrder();
          ${className}Query ${classNameLower}Query = QueryUitl.newQuery(${className}Query.class);
          ${classNameLower}Po = new ${className}Po();
          G4Utils.copyPropBetweenBeans(${classNameLower}Query, ${classNameLower}Po);
          QueryResult<${className}Po> queryResult = ${classNameLower}Service.selectQueryResult(${classNameLower}Po, countOrder);
          JSONObject jsonObject = JsonHelper.encodeList2JSONArray(queryResult.getResultlist(),(int)queryResult.getTotalrecord(),null);
          <#else>
           List<${className}> list = ${classNameLower}Service.selectList(${classNameLower}Po);
           JSONObject jsonObject = JsonHelper.encodeList2JSONArray(list,null,null);
          </#if>
          setResultJson(jsonObject);
        } catch (Exception e) {
            log.error("分页查询数据异常", e);
        }
        return SUCCESS;
    }

    public String find() {
        try {
            ${classNameLower}Po = new ${className}Po();
			${classNameLower}Po.set${table.pkColumn.columnName}(${classNameLower}Query.get${table.pkColumn.columnName}());
            ${classNameLower}Po = ${classNameLower}Service.getById(${classNameLower}Po);
            JSONObject jsonObject = JsonHelper.encodeObject2JSONObject(${classNameLower}Po ,null);
            setResultJson(jsonObject);
        } catch (DataAccessException e) {
            log.error("根据主键查询对象信息异常", e);
        }
        return SUCCESS;
    }

    public String save() {
        HttpServletRequest request = ServletActionContext.getRequest();
        JSONObject returnObject = new JSONObject();
        try
        {
            boolean isEdit = Boolean.parseBoolean(request.getParameter("dataEditflag"));
            String processId = request.getParameter("processId");
            if (G4Utils.isNotEmpty(processId)) {
                // 得到流程管理的数据
                Map<String, Object> values = this.workFlowService.getHistVariables(processId);
                if (G4Utils.isNotEmpty(values.get("dataEditflag"))) {
                    isEdit = (boolean) values.get("dataEditflag");
                }
                ${classNameLower}Query = (${className}Query) Utils.convertMapToObj2(${className}Query.class, values);
            }
           <#if table.pkCount gt 1>
            <#list table.pkColumns as column>
              ${column.simpleJavaType} ${column.columnNameLower} = new ${column.simpleJavaType}(request.getParameter("${column.columnNameLower}"));
            </#list>
//            if(<#list table.pkColumns as column>StringUtils.isNotBlank(${column.columnNameLower})<#if column_has_next>&&</#if></#list>){
              ${className}Id id = new ${className}Id(<#list table.pkColumns as column>${column.columnNameLower}<#if column_has_next>,</#if></#list>);
              ${classNameLower}.setId(id);
//            }
           <#else>
           if(isEmpty(${classNameLower}Query.get${table.pkColumn.columnName}()))
        	  ${classNameLower}Query.set${table.pkColumn.columnName}(null);
           </#if>
           ${classNameLower}Po=new ${className}Po();
           G4Utils.copyPropBetweenBeans(${classNameLower}Query, ${classNameLower}Po);
           
            if (isEdit)
                ${classNameLower}Service.update(${classNameLower}Po);
            else
                ${classNameLower}Service.save(${classNameLower}Po);
                
            returnObject.element(SUCCESS, true);
            returnObject.element("msg", "操作成功");
        }
        catch (Exception e)
        {
            returnObject.put("failure", true);
            returnObject.put("msg", e.getMessage());
            log.error("执行添加或修改异常", e);
        }

        setResultJson(returnObject);
        return SUCCESS;
    }

    public String delete() {
        HttpServletRequest request = ServletActionContext.getRequest();
        JSONObject returnObject = new JSONObject();
        try
        {
        String ids = request.getParameter("ids");
        String processId = request.getParameter("processId");
        if (G4Utils.isNotEmpty(processId)) {
            // 得到流程管理的数据
            Map<String, Object> values = this.workFlowService.getHistVariables(processId);
            ids = (String) values.get("id");
        }
        String[] idarray = ids.split(",");

        for (int i = 0; i < idarray.length; i++)
         {
                ${table.pkColumn.simpleJavaType} id = new ${table.pkColumn.simpleJavaType}(idarray[i]);
                ${classNameLower}Po=new ${className}Po();
                ${classNameLower}Po.set${table.pkColumn.columnName}(id);
                ${classNameLower}Service.removeById(${classNameLower}Po);
         }
            returnObject.put("success", true);
            returnObject.put("msg", "删除成功");
        }
        catch (Exception e)
        {
            returnObject.put("failure", true);
            returnObject.put("msg", e.getMessage());
            log.error("删除数据异常", e);
        }
        setResultJson(returnObject);
        return SUCCESS;
    }
    
	public String getSelect() {
		JSONArray jsonArray = new JSONArray();
		CountOrder countOrder = CountOrderUtil.getCountOrder();
		SelectVo selectVo = QueryUitl.newQuery(SelectVo.class);
		 ${className}Po ${classNameLower}Po = com.alibaba.fastjson.JSON.parseObject(
				selectVo.getQuery(), ${className}Po.class);
		try {
			List<${className}Po> list = ${classNameLower}Service.selectList(
					${classNameLower}Po, countOrder);
			for (${className}Po ${classNameLower}Po2 : list) {
				jsonArray.element(JsonHelper.parseObjToJSONObject(
						${classNameLower}Po2, selectVo));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询数据异常", e);
		}
		
		setResultJson(jsonArray);
		return SUCCESS;
	}
}
