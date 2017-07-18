package com.codegen.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codegen.po.EadtSysDatabaseSource;
import com.codegen.service.EadtSysDatabaseSourceService;
import com.codegen.vo.EadtSysDatabaseSourceQuery;
import com.common.base.BaseAction;
import com.common.base.BaseJdbcDao;
import com.common.json.JsonHelper;
import com.common.utils.CountOrder;
import com.common.utils.CountOrderUtil;
import com.common.utils.G4Utils;
import com.common.utils.QueryUitl;
import com.common.utils.SpringBeanLoader;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class EadtSysDatabaseSourceAction extends BaseAction implements
		Preparable, ModelDriven {
    private static Logger log = LoggerFactory.getLogger(EadtSysDatabaseSourceAction.class);
	EadtSysDatabaseSourceService eadtSysDatabaseSourceService = (EadtSysDatabaseSourceService) SpringBeanLoader
			.getSpringBean("eadtSysDatabaseSourceService");

	private JSON resultJson;

	private EadtSysDatabaseSource eadtSysDatabaseSource;

	public JSON getResultJson() {
		return resultJson;
	}

	public void setResultJson(JSON resultJson) {
		this.resultJson = resultJson;
	}

	public void prepare() throws Exception {
		eadtSysDatabaseSource = new EadtSysDatabaseSource();
	}

	public Object getModel() {
		return eadtSysDatabaseSource;
	}

	public String list() {
		CountOrder countOrder = CountOrderUtil.getCountOrder();

		EadtSysDatabaseSourceQuery eadtSysDatabaseSourceQuery = QueryUitl
				.newQuery(EadtSysDatabaseSourceQuery.class);
		List<EadtSysDatabaseSource> list = eadtSysDatabaseSourceService
				.searchEadtSysDatabaseSource(eadtSysDatabaseSourceQuery,
						countOrder);
		Long pageCount = eadtSysDatabaseSourceService
				.countEadtSysDatabaseSource(eadtSysDatabaseSourceQuery);

		JSONObject jsonObject = JsonHelper.encodeList2JSONArray(list,
				pageCount.intValue(), null);
		setResultJson(jsonObject);
		return SUCCESS;
	}

	public String find() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = new String(request.getParameter("databasesourcedbid"));
		EadtSysDatabaseSource eadtSysDatabaseSource = eadtSysDatabaseSourceService
				.getById(id);

		JSONObject jsonObject = JsonHelper.encodeObject2JSONObject(
				eadtSysDatabaseSource, null);
		setResultJson(jsonObject);
		return SUCCESS;
	}

	public String save() {
		HttpServletRequest request = ServletActionContext.getRequest();
		JSONObject returnObject = new JSONObject();
		try {

			boolean isEdit = Boolean.parseBoolean(request
					.getParameter("dataEditflag"));
			if (G4Utils.isEmpty(eadtSysDatabaseSource.getDatabasesourcedbid()))
				eadtSysDatabaseSource.setDatabasesourcedbid(null);
			if (isEdit)
				eadtSysDatabaseSourceService.update(eadtSysDatabaseSource);
			else
				eadtSysDatabaseSourceService.save(eadtSysDatabaseSource);
			returnObject.element(SUCCESS, true);
			returnObject.element("msg", "操作成功");

		} catch (Exception e) {
			returnObject.put("failure", true);
			returnObject.put("msg", e.getMessage());
			e.printStackTrace();
		}

		setResultJson(returnObject);
		return SUCCESS;
	}

	public String delete() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String ids = request.getParameter("ids");
		String[] idarray = ids.split(",");
		JSONObject returnObject = new JSONObject();
		try {
			for (int i = 0; i < idarray.length; i++) {
				String id = new String(idarray[i]);
				eadtSysDatabaseSourceService.removeById(id);
			}
			returnObject.put("success", true);
			returnObject.put("msg", "删除成功");
		} catch (Exception e) {
			returnObject.put("failure", true);
			returnObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		setResultJson(returnObject);
		return SUCCESS;
	}

	/**
	 * 测试连接数据源
	 * 
	 * @return
	 */
	public String testConnect() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String sid = request.getParameter("sid");
		JSONObject returnObject = new JSONObject();
		try {
			EadtSysDatabaseSource eadtSysDatabaseSource1 = eadtSysDatabaseSourceService
					.getById(sid);
			BaseJdbcDao.Connection(eadtSysDatabaseSource1);
			returnObject.element(SUCCESS, true);
			returnObject.element("msg", "连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			returnObject.element(SUCCESS, false);
			returnObject.element("msg", e + "连接失败");
		}
		setResultJson(returnObject);
		return SUCCESS;
	}
	
    
}
