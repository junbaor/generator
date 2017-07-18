/**
 * \ * Created by IntelliJ IDEA. User: ritchrs Date: 11-4-11 Time: 下午4:09 To
 * change this template use File | Settings | File Templates.
 */
Ext.ux.FunctionEditorGrid = Ext.extend(Ext.grid.EditorGridPanel, {
	region : 'center',
	margins : '5 5 5 5',
	forceFit : true,
	loadMask : true,
	root : 'root',
	dataId : 'id',
	totalProperty : 'total',
	baseParams : null,
	remoteSort : false,
	formWidth : window.screen.width,
	formHeight : window.screen.height,
	searchColumn : 4,
	pluginSize : 0,
	paging : true,
	edit : true,
	pageSize : 20,
	lock : false,
	formLabelWidth : 150,
	searchLabelWidth : 75,
	remoteLoad : false,
	addVisible : true,
	removeVisible : true,
	variable : null,// 工作流审核判断条件
	addTbar : false,// 设置工具栏按钮添加在grid的上面还是下面
	portalColumns : 4,
	winWidth : 250,
	winHeight : 350,
	autoSearch : true,
	dbclick : true,
	orderby : '',// 排序字段，支持单一字段
	dir : 'asc', // 排序方式
	propsGridVisible : false,// 侧边选择栏
	saveAction : "",// 保存地址
	loadData:null,//本地数据
	saveVisible:true,
	initComponent : function(config) {
		var items = [];
		if (this.addVisible) {
			items.push({
						text : '新增',
						iconCls : 'addIcon',
						name : 'add_id',
						handler : this.addRow,
						scope : this
					}, '-');
		}
		if (this.removeVisible) {
			items.push({
						text : '删除',
						iconCls : 'deleteIcon',
						name : 'remove_id',
						handler : this.deleteRow,
						scope : this

					});
		}
		if(this.saveVisible)
		{
		   items.push({
					text : '保存',
					iconCls : 'acceptIcon',
					handler : this.saveData,
					scope : this
				});
        }
		var tbar = new Ext.Toolbar({
					items : items
				});

		this.tbar = Ext.apply(tbar, this.tbar);// 表格工具栏

		this.isPortal = (isPortal != undefined ? isPortal : false);
		var fields = new Array();
		var cms = new Array();
		if (this.cmRender != undefined) {
			var cmLength = this.isPortal
					? this.portalColumns
					: this.cmRender.length;
			for (var i = 0; i < cmLength; i++) {
				fields[i] = {
					name : this.cmRender[i][2]
				};
				cms[i] = {
					header : this.cmRender[i][0],
					sortable : this.cmRender[i][1],
					dataIndex : this.cmRender[i][2]
				};
				if (this.cmRender[i][3] != undefined
						&& this.cmRender[i][3] != null
						&& this.cmRender[i][3] != "") {
					cms[i].renderer = this.cmRender[i][3];
				}
				if (this.cmRender[i][4] != undefined
						&& this.cmRender[i][4] != null
						&& this.cmRender[i][4] != "") {
					cms[i].xtype = 'actioncolumn';
					cms[i].items = this.cmRender[i][4];
				}
				if (this.cmRender[i][5] != undefined
						&& this.cmRender[i][5] != null
						&& this.cmRender[i][5] != "") {
					cms[i].width = this.cmRender[i][5];
				}
			}

			this.colModel = new Ext.grid.ColumnModel(cms);
			delete this.cmRender;
		} else if (this.columns != undefined) {
			var cmLength = this.isPortal
					? this.portalColumns
					: this.columns.length;
			var columnsTemp = [];
			for (var i = this.pluginSize; i < cmLength; i++) {
				fields[i] = {
					name : this.columns[i].dataIndex
				};
			}
			if (this.isPortal) {
				for (var i = 0; i < cmLength; i++) {
					columnsTemp[i] = this.columns[i];
				}
				this.columns = columnsTemp;
				delete columnsTemp;
			}
			if (this.lock && !this.isPortal) {
				this.colModel = new Ext.ux.grid.LockingColumnModel(this.columns);
				this.view = new Ext.ux.grid.LockingGridView();
				delete this.columns;
			}
		} else if (this.colModel != undefined) {
			var cmLength = this.isPortal ? this.portalColumns : this.colModel
					.getColumnCount();

			for (var i = this.pluginSize; i < cmLength; i++) {
				fields[i] = {
					name : this.colModel.getDataIndex(i)
				};
			}
		}
		var ds = null;
		if (this.loadData) {
			ds = new Ext.data.JsonStore({
						data : this.loadData,
						fields : fields

					});
		} else {
			var reader = new Ext.data.JsonReader({
						root : this.root,
						id : Ext.isString(this.dataId) ? this.dataId : "",
						totalProperty : this.totalProperty,
						fields : fields
					});

			ds = new Ext.data.Store({
						proxy : new Ext.data.HttpProxy({
									url : _contextPath_ + '/' + this.listAction
											+ '.action'
								}),
						baseParams : this.baseParams,
						remoteSort : this.remoteSort,
						reader : reader
					});
		}
		var viewConfig = {
			forceFit : this.isPortal ? true : this.forceFit
		};
		this.viewConfig = viewConfig;
		if (this.groupField) {
			if (this.viewConfig) {
				delete this.viewConfig;
			}
			ds = new Ext.data.GroupingStore({
						url : _contextPath_ + '/' + this.listAction + '.action',
						reader : reader,
						remoteSort : this.remoteSort,
						sortInfo : {
							field : this.groupField,
							direction : "ASC"
						},
						groupField : this.groupField
					});
			this.view = new Ext.grid.GroupingView({
				forceFit : true,
				groupTextTpl : '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "Items" : "Item"]})'
			});

		}
		if (this.paging) {
			var bbar = new Ext.PagingToolbar({
						pageSize : this.pageSize,
						// prependButtons:true,
						store : ds,
						plugins : [new Ext.ux.PageSizePlugin(),
								new Ext.ux.ProgressBarPager()],
						displayInfo : true
					});
			this.bbar = bbar;
		}
		this.store = ds;
		if (!this.isPortal) {

		} else {
			delete this.title;
		}
		// Ext.apply(this, config);
		Ext.ux.FunctionEditorGrid.superclass.initComponent.call(this);
		// 双击一条记录弹出查看页面
		if (this.dbclick) {

			if (undefined == this.viewHandler
					|| 'undefined' == this.viewHandler) {

				this.addListener('rowdblclick', this.viewObject);
			} else {

				this.addListener('rowdblclick', this.viewHandler);
			}

		}
		// ds.load({params:{start:0,limit:this.pageSize}});
		if (this.autoSearch) {
			var params = {};
			if (!Ext.isEmpty(this.orderby)) {
				params.sort = this.orderby;
				params.dir = this.dir;
			}
			if (this.paging) {
				params.start = 0;
				params.limit = this.pageSize;
				ds.load({
							params : params,
							callback : function(r, o, s) {
								if (!s) {
									Ext.Msg.show({
												title : '错误',
												msg : '列表数据调用出错',
												icon : Ext.MessageBox.ERROR
											});
								}
							}
						});
			} else {
				ds.load({
							params : params,
							callback : function(r, o, s) {
								if (!s) {
									Ext.Msg.show({
												title : '错误',
												msg : '列表数据调用出错',
												icon : Ext.MessageBox.ERROR
											});
								}
							}
						});
			}
		}
		this.panels = [this];
		if (this.searchSet != undefined && !this.isPortal) {
			var searchItems = new Array();
			for (var columnIndex = 0; columnIndex < this.searchColumn; columnIndex++) {
				searchItems[columnIndex] = {
					columnWidth : 1 / this.searchColumn,
					layout : 'form',
					items : []
				};
			}

			for (var searchIndex = 0; searchIndex < this.searchSet.length; searchIndex++) {
				var witchColumn = searchIndex % this.searchColumn;
				searchItems[witchColumn].items
						.push(this.searchSet[searchIndex]);
			}
			var formheigt = 28
					* Math.ceil(this.searchSet.length / this.searchColumn) + 75;
			var searchFrom = new Ext.FormPanel({
						labelWidth : this.searchLabelWidth + 20,
						height : formheigt,
						title : "搜索",
						region : 'north',
						labelAlign : 'right',
						url : _contextPath_ + '/' + this.searchAction
								+ '.action',
						frame : true,
						collapsible : true,
						bodyStyle : 'padding:5px 5px 0',
						buttonAlign : "center",
						items : {
							layout : 'column',
							items : searchItems
						},
						buttons : [{
							text : '查询',
							name : 'searchbtn',
							id : 'searchbtn',
							tooltip : '查询',
							handler : function() {
								if (searchFrom.form.isValid()) {
									ds.on('beforeload', function() {
												ds.baseParams = searchFrom
														.getForm().getValues();
											});
									if (this.paging) {
										var size = this.getBottomToolbar().pageSize;
										ds.load({
													params : {
														start : 0,
														limit : size
													}
												});
									} else {
										ds.load();
									}
								}
							},
							scope : this
						}, {
							text : '重置',
							tooltip : '重置',
							handler : function() {
								searchFrom.form.reset();
							}
						}]

					});

			this.panels = [searchFrom, this];

		}

		this.addEvents('addBefore'); // 点击新增按钮前处理
		this.addEvents('editBefore'); // 点击修改按钮前处理
		this.addEvents('viewAfter'); // 查看后处理
		this.addEvents('updateAfter'); // 点击修改按钮window显示后处理
		this.addEvents('saveBefore'); // 点击保存前处理（包含修改和保存）
		this.addEvents('saveAfter');// 点击保存后处理（包含修改和保存）
		this.addEvents('deleteBefore');// 删除前处理
		this.addEvents('deleteAfter');// 删除后处理
		this.addEvents('exportexcel');// 导出处理

		// new Ext.Viewport({
		// layout:'border',
		// items:[panels]
		// });
	},
	addRow : function() {
		if (this.hasListener("addBefore")) {
			if (!this.fireEvent('addBefore')) {
				return;
			}
		}
		var row = new MyRecord({});
		// row.set('qybz', '1'); // 赋初值
		this.stopEditing();
		this.getStore().insert(0, row);
		this.startEditing(0, 2);
	},
	editRow : function() {
		if (this.hasListener("editBefore")) {
			if (!this.fireEvent('editBefore')) {
				return;
			}
		}

	},
	deleteObject_noTerminate : function(obj) {

	},
	saveForm_noTerminate : function(obj) {
		if (obj.win1.items.get(0).form.isValid()) {
			obj.win1.items.get(0).form.submit({
				success : function(form, action) {
					obj.win1.hide();
					obj.store.reload();

					var operate = '';
					if (obj.win1.items.get(0).form.baseParams.dataEditflag == true) {
						operate = 'update';
					} else {
						operate = 'save';
					}
				},
				failure : function(form, action) {
					Ext.MessageBox.show({
								title : '提示',
								msg : action.result.msg,
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING
							});
				},
				scope : this,
				waitTitle : "请稍后",
				waitMsg : "数据保存中……"
			});
		}
	},
	deleteRow : function() {
		if (this.hasListener("deleteBefore")) {
			if (!this.fireEvent('deleteBefore')) {
				return;
			}
		}
		var c = this.getSelectionModel().getSelections();
		if (c.length == 1)
			Ext.MessageBox.confirm('消息', '确认要删除所选对象?', doDelObject, this);
		else
			Ext.MessageBox.show({
						title : '警告',
						msg : "请选择一个对象",
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.WARNING
					});

		function doDelObject(btn) { // 查询此交易需不需要审批，如果需要审批以什么样的方式进行审批
			if (btn == 'yes') {
				this.deleteObject_noTerminate(this);
			}
		}
	},
	deleteObject_noTerminate : function(obj) {
		var c = obj.getSelectionModel().getSelections();
		var waitMsg = Ext.Msg.wait("正在删除...");
		Ext.Ajax.request({
					url : _contextPath_ + '/' + obj.deleteAction + '.action',
					success : onSuccessDel,
					failure : function(response) {
						waitMsg.hide();
						var errorWindow = new Ext.Window({
									title : "出错啦！",
									width : 200,
									height : 200,
									maximizable : true,
									autoScroll : true,
									html : response.responseText
								});
						errorWindow.show();
						errorWindow.maximize();
					},
					params : obj.makeDelParams(c),
					scope : this
				});

		function onSuccessDel(o) {
			waitMsg.hide();
			var jsonobjs = Ext.decode(o.responseText);
			var msg = "删除对象成功";
			if (jsonobjs.msg != null && jsonobjs.msg != '') {
				msg = jsonobjs.msg;
			}
			Ext.MessageBox.show({
						title : '消息',
						msg : msg,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.INFO
					});
			obj.store.reload();
			/*
			 * //记录日志 var
			 * operateObject=this.listAction.substring(this.listAction.lastIndexOf('/')+1,this.listAction.lastIndexOf('_'));
			 * operInfos('delete',operateObject);
			 */
		}
	},
	// 执行操作 1功能ID 2操作标识，3整个js对象 4原功能执行方法 5删除赋值方法
	makeParams : function(record) {
		var params = {};
		if (Ext.isArray(this.dataId)) {
			for (var i = 0; i < this.dataId.length; i++) {
				var key = this.dataId[i];
				var value = record.json[key];
				var param = {};
				param[key] = value;
				Ext.applyIf(params, param);
			}
		} else {
			params[this.dataId] = record.id;
		}
		return params;
	},

	makeDelParams : function(records) {
		var ids = new Array();
		for (var i = 0, len = records.length; i < len; i++) {
			var record = records[i];
			if (Ext.isArray(this.dataId)) {
				var params = new Array();
				for (var j = 0; j < this.dataId.length; j++) {
					var key = this.dataId[j];
					var value = record.json[key];
					params[j] = value;
				}
				ids[ids.length] = params;
			} else {
				ids[ids.length] = record.id;
			}
		}
		return {
			ids : ids.toString()
		}
	},
	saveData : function() {
		// var m = this.getStore().modified.slice(0); // 获取修改过的record数组对象
		var m = this.getStore().getModifiedRecords();
		if (Ext.isEmpty(m)) {
			Ext.MessageBox.alert('提示', '没有数据需要保存!');
			return;
		}
		// if (!this.validateEditGrid(m, '1')) {
		// Ext.Msg.alert('提示', '必输字段数据校验不合法,请重新输入!', function() {
		// grid.startEditing(0, 2);
		// });
		// return;
		// }
		var jsonArray = [];
		// 将record数组对象转换为简单Json数组对象
		Ext.each(m, function(item) {
					if (jsonArray)
						jsonArray.push(item.data);
				});
		// 提交到后台处理
		Ext.Ajax.request({
					scope : this,
					url : _contextPath_ + '/' + this.saveAction + ".action",
					success : function(response) { // 回调函数有1个参数
						var resultArray = Ext.util.JSON
								.decode(response.responseText);
						Ext.Msg.alert('提示', resultArray.msg);
						this.store.reload();

					},
					failure : function(response) {
						Ext.MessageBox.alert('提示', '数据保存失败');
					},
					params : {
						// 系列化为Json资料格式传入后台处理
						requestData : Ext.encode(jsonArray)
					}
				});
	},
	// 检查新增行的可编辑单元格数据合法性
	validateEditGrid : function(m, colName) {
		for (var i = 0; i < m.length; i++) {
			var record = m[i];
			var rowIndex = this.getStore().indexOfId(record.id);
			var value = record.get(colName);
			if (Ext.isEmpty(value)) {
				// Ext.Msg.alert('提示', '数据校验不合法');
				return false;
			}
			var colIndex = cm.findColumnIndex(colName);
			var editor = cm.getCellEditor(colIndex).field;
			if (!editor.validateValue(value)) {
				// Ext.Msg.alert('提示', '数据校验不合法');
				return false;
			}
		}
		return true;
	},
	getSearchForm : function() {
		return this.panels[0];
	},
	getStore : function() {
		return this.store;
	}
});
