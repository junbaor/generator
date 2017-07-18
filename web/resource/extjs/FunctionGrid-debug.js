/**
 * \ * Created by IntelliJ IDEA. User: ritchrs Date: 11-4-11 Time: 下午4:09 To
 * change this template use File | Settings | File Templates.
 */
Ext.ux.FunctionGrid = Ext.extend(Ext.grid.GridPanel, {
			region : 'center',
			margins : '5 5 5 5',
			forceFit : true,
			loadMask : true,
			root : 'root',
			dataId : 'id',
			totalProperty : 'total',
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
			viewVisible : true,
			addVisible : true,
			editVisible : true,
			removeVisible : true,
			listBaseParams : {},// 默认参数
			addBtnDisabled : false,// 添加按钮禁止使用标志 默认不禁止
			editBtnDisabled : false,// 修改按钮禁止使用标志
			delBtnDisabled : false,// 删除按钮禁止使用标志
			viewBtnDisabled : false,// 查看按钮禁止使用标志
			variable : null,// 工作流审核判断条件
			addTbar : false,// 设置工具栏按钮添加在grid的上面还是下面
			exportVisble : false,
			portalColumns : 4,
			addText : "增加",
			winWidth : 250,
			winHeight : 350,
			editText : "修改",
			deleteText : "删除",
			viewText : "查看",
			saveText : "保存",
			cancelText : "取消",
			autoSearch : true,
			dbclick : true,
			orderby : '',// 排序字段，支持单一字段
			dir : 'asc', // 排序方式
			propsGridVisible : false,// 侧边选择栏
			layoutFormSet : "form",// formset布局
			initComponent : function(config) {
                var _this=this;
				this.isPortal = (isPortal != undefined ? isPortal : false);
				var fields = new Array();
				var cms = new Array();
				if (this.cmRender != undefined) {
					var cmLength = this.isPortal ? this.portalColumns : this.cmRender.length;
					for (var i = 0; i < cmLength; i++) {
						fields[i] = {
							name : this.cmRender[i][2]
						};
						cms[i] = {
							header : this.cmRender[i][0],
							sortable : this.cmRender[i][1],
							dataIndex : this.cmRender[i][2]
						};
						if (this.cmRender[i][3] != undefined && this.cmRender[i][3] != null && this.cmRender[i][3] != "") {
							cms[i].renderer = this.cmRender[i][3];
						}
						if (this.cmRender[i][4] != undefined && this.cmRender[i][4] != null && this.cmRender[i][4] != "") {
							cms[i].xtype = 'actioncolumn';
							cms[i].items = this.cmRender[i][4];
						}
						/**
						if (this.cmRender[i][5] != undefined && this.cmRender[i][5] != null && this.cmRender[i][5] != "") {
							cms[i].width = this.cmRender[i][5];
						}
						**/
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
					this.addEvents('searchBefore'); // 点击查询按钮前处理
					this.addEvents('resetAfter'); // 点击查询按钮前处理
					this.colModel = new Ext.grid.ColumnModel(cms);
					delete this.cmRender;
				} else if (this.columns != undefined) {
					var cmLength = this.isPortal ? this.portalColumns : this.columns.length;
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
					var cmLength = this.isPortal ? this.portalColumns : this.colModel.getColumnCount();

					for (var i = this.pluginSize; i < cmLength; i++) {
						fields[i] = {
							name : this.colModel.getDataIndex(i)
						};
					}
				}
				var reader = new Ext.data.JsonReader({
							root : this.root,
							id : Ext.isString(this.dataId) ? this.dataId : "",
							totalProperty : this.totalProperty,
							fields : fields
						});

				var ds = new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
										url : _contextPath_ + '/' + this.listAction + '.action'
									}),
							remoteSort : this.remoteSort,
							reader : reader
						});
				var viewConfig = {
					// forceFit : this.isPortal ? false : this.forceFit
					forceFit : true
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

				this.store = ds;
				if (this.edit) {

					var simple = new Ext.FormPanel({
								region : 'center',
								labelWidth : this.formLabelWidth + 30,
								labelAlign : 'right',
								url : _contextPath_ + '/' + this.saveAction + '.action',
								frame : true,
								autoScroll : true,
								plugins : new Ext.ux.AllowBlank({
											hint : false
										}),
								layout : this.layoutFormSet,
								bodyStyle : 'padding:5px 5px 0',
								items : this.formSet
							});

					var win = new Ext.Window({
								layout : 'border',
								width : this.formWidth - this.winWidth ,
								height :  this.formHeight - this.winHeight,
								closable : true,
								modal : true,
								closeAction : 'hide',
								constrain : true, // 设置窗口是否可以溢出父容器
								plain : true,
								items : [simple],
								listeners : {
									'beforehide' : function() {
										var mainTabs = window.parent.mainTabs;
										if (undefined != mainTabs && null != mainTabs) {
											var activeTab = mainTabs.getActiveTab();
											var tabId = activeTab.id;
											if ('tab_id_011005' == tabId || 'tab_id_011007' == tabId) {
												window.location.reload();
											}
										}
									}
								},
								buttons : [{
											text : this.saveText,
											handler : this.saveForm,
											scope : this
										}, {
											text : this.cancelText,
											handler : function() {
												win.hide();
											}
										}]

							});
					this.win1 = win;
				}
				if (!this.isPortal) {
					var items = ['->'];
					if (this.saveAction != undefined && this.saveAction != null) {
						if (this.addVisible) {
							items.push({
										text : this.addText,
										iconCls : 'add',
										id : 'save_id',
										disabled : this.addBtnDisabled,
										handler : this.addObject,
										scope : this

									}, '-');
						}
						if (this.editVisible) {
							var editMethod = null;
							if (undefined == this.editHandler || 'undefined' == this.editHandler) {
								editMethod = this.editObject;
							} else {
								editMethod = this.editHandler;
							}

							items.push({
										text : this.editText,
										iconCls : 'edit',
										id : 'update_id',
										disabled : this.editBtnDisabled,
										handler : editMethod,
										scope : this

									});
						}
					}
					if (this.deleteAction != undefined && this.deleteAction != null) {
						if (this.removeVisible) {
							items.push('-', {
										text : this.deleteText,
										iconCls : 'remove',
										disabled : this.delBtnDisabled,
										handler : this.deleteObject,
										id : 'delete_id',
										scope : this
									});
						}
					}
					if (this.viewVisible) {
						var viewMethod = null;
						if (undefined == this.viewHandler || 'undefined' == this.viewHandler) {
							viewMethod = this.viewObject;
						} else {
							viewMethod = this.viewHandler;
						}
						items.push('-', {
									text : this.viewText,
									iconCls : 'view',
									disabled : this.viewBtnDisabled,
									handler : viewMethod,
									name : 'view',
									id : 'view_id',
									scope : this
								});
					}
					if (this.exportVisble) {
						function getExportExcelTotalCount(totalCount) {
							if (Ext.isEmpty(totalCount)) {
								return 0;
							} else {
								var modCount = new Number(totalCount) % Prophet.GridPanel.exportExcel.limit;
								var result = new Number(totalCount) / Prophet.GridPanel.exportExcel.limit;
								if (modCount == 0) {
									return result;
								} else {
									return Math.floor(result) + 1;
								}
							}
						}
						items.push(
								/*
								 * '-', '每个文件' + Prophet.GridPanel.exportExcel.limit + '条',
								 */'-', '导出第', {
									xtype : 'numberfield',
									id : 'pageNum',
									name : 'pageNum',
									value : '1',
									width : 40,
									allowNegative : false,
									decimalPrecision : 0
								}, '个,', '共', {
									xtype : 'label',
									id : 'totalPageCount',
									text : '1'
								}, '个', {
									id : 'exportIcon',
									tooltip : '导出Excel',
									iconCls : 'page_excelIcon',
									text : '导出',
									handler : (function() {
										var totalPageCount = Ext.getCmp("totalPageCount").text;
										var pageNum = Ext.getCmp("pageNum").getValue();
										if (parseInt(pageNum) > parseInt(totalPageCount) && parseInt(totalPageCount) != 0) {
											showErrMsg('导出页数比总页数大');
											return;
										}
										var startNo = (pageNum - 1) * Prophet.GridPanel.exportExcel.limit;
										var endNo = Prophet.GridPanel.exportExcel.limit;
										this.fireEvent('exportexcel', startNo, endNo);
									}).createDelegate(this)
								});
						this.store.on('load', (function() {
									Ext.getCmp('totalPageCount').setText(getExportExcelTotalCount(ds.getTotalCount()));
									Ext.getCmp('pageNum').setValue('1');
								}));
					}
					if (this.paging) {
						var bbar = new Ext.PagingToolbar({
									pageSize : this.pageSize,
									// prependButtons:true,
									store : ds,
									plugins : [new Ext.ux.PageSizePlugin(), new Ext.ux.ProgressBarPager()],
									displayInfo : true,
									onFirstLayout:function(){//增加这个配置
										if(this.dsLoaded){
						                    this.onLoad.apply(this, this.dsLoaded);
						                }
											if(this.rendered&&this.refresh){
											this.refresh.hide();}
									}
								});
						if (this.edit && !this.addTbar) {
							bbar.add(items);
						}
						this.bbar = bbar;
					} else if (!this.paging && this.edit) {
						var bbar = new Ext.Toolbar({
									items : items
								});
						if (!this.addTbar) {
							this.bbar = bbar;
						}
					}

					if (this.addTbar) {
						if (items.length > 1) {
							items.splice(0, 1);
						}
						var tbar = new Ext.Toolbar({
									items : items
								});
						this.tbar = tbar;
					}

				} else {
					delete this.title;
				}
				// Ext.apply(this, config);
				Ext.ux.FunctionGrid.superclass.initComponent.call(this);
				// 双击一条记录弹出查看页面
				if (this.dbclick) {

					if (undefined == this.viewHandler || 'undefined' == this.viewHandler) {

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

                    ds.on('beforeload', function() {
                        if(_this.listBaseParams){
                            Ext.apply(ds.baseParams , _this.listBaseParams);
                        }
                    });

					if (this.paging) {
						params.start = 0;
						params.limit = this.pageSize;
						//if (this.listBaseParams)
						//	params = Ext.applyIf(params, this.listBaseParams);

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
									params : this.listBaseParams,
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
						searchItems[witchColumn].items.push(this.searchSet[searchIndex]);
					}
					var formheigt = 28 * Math.ceil(this.searchSet.length / this.searchColumn) + 75;
					var searchFrom = new Ext.FormPanel({
								labelWidth : this.searchLabelWidth + 20,
								height : formheigt,
								title : "搜索",
								region : 'north',
								labelAlign : 'right',
								url : _contextPath_ + '/' + this.searchAction + '.action',
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
												// 查询前处理
												if (this.hasListener("searchBefore")) {
													if (!this.fireEvent('searchBefore')) {
														return;
													}
												}
												if (searchFrom.form.isValid()) {
                                                    ds.on('beforeload', function() {
                                                        ds.baseParams =  _this.listBaseParams;
                                                        if(_this.listBaseParams){
                                                            Ext.apply(ds.baseParams , searchFrom.getForm().getValues());
                                                        }
                                                    });
													if (this.paging) {
														var size = this.getBottomToolbar().pageSize;
														var params = {
															start : 0,
															limit : size
														};
														ds.load({
																	params : params
																});
													} else {
                                                        ds.load();
													}
												}
											},
											scope : this
										}, {
											text : '重置',
											name : 'resetbtn',
											id : 'resetbtn',
											tooltip : '重置',
											scope : this,
											handler : function() {
												searchFrom.form.reset();
												// 重置后处理
												if (this.hasListener("resetAfter")) {
													this.fireEvent('resetAfter');
												}
											}
										}]

							});

					this.panels = [searchFrom, this];

				}
				// new Ext.Viewport({
				// layout:'border',
				// items:[panels]
				// });
			},

			addObject : function() {
				// 修改后处理
				if (this.hasListener("addBefore")) {
					if (!this.fireEvent('addBefore')) {
						return;
					}
				}
				if (this.initAddHandler) {
					this.initAddHandler.call(this.scope || this, this, this.win1.items.get(0));
				}
				this.win1.setTitle(this.addTitle);
				this.win1.show();
				this.win1.items.get(0).form.reset();
				this.win1.items.get(0).form.baseParams = {
					dataEditflag : false
				};

				var fn = function(c) {
					var insertItems = c.items;
					if (insertItems != undefined && insertItems != null) {
						if (undefined != insertItems.each) {
							insertItems.each(fn);
						}

					} else {
						if (this.addDisVisabled) {
							c.hide();
						} else {
							c.show();
							if (c.xtype == 'textarea') {
								c.setDisabled(false);
							} else {
								if (!this.addReadOnly) {
									if (undefined != c.setReadOnly) {
										c.setReadOnly(false);
									}
									c.removeClass("textReadonly");
								} else {
									c.setReadOnly(true);
									c.addClass("textReadonly");
								}
							}
						}

					}
				};
				this.win1.items.get(0).items.each(fn);
				this.win1.buttons[0].show();

			},
			editObject : function() {
				// 修改后处理
				if (this.hasListener("editBefore")) {
					if (!this.fireEvent('editBefore')) {
						return;
					}
				}
				this.win1.items.get(0).form.baseParams = {
					dataEditflag : true
				};
				var c = this.getSelectionModel().getSelections();
				if (c.length == 1) {
					function intForm(o) {
						if (this.initEditHandler) {
							this.initEditHandler.call(this.scope || this, this, this.win1.items.get(0));
						}
						var resultJson = Ext.util.JSON.decode(o.responseText);
						this.win1.setTitle(this.editTitle);
						this.win1.show();
						this.win1.items.get(0).form.setValues(resultJson);
						this.win1.items.get(0).items.each(fn);

						if (this.propsGridVisible) {
							Ext.getCmp('propsGrid').setSource(resultJson.propsGrid)
						}
						// 修改后处理
						if (this.hasListener("updateAfter")) {
							if (!this.fireEvent('updateAfter', resultJson)) {
								return;
							}
						}

					}

					var fn = function(c) {
						var insertItems = c.items;
						if (insertItems != undefined && insertItems != null) {
							if (c instanceof Ext.form.CheckboxGroup) {
								raidoOrCheckBoxReadonly(c)
							}
							insertItems.each(fn);
						} else {
							try {
								if (this.editDisVisabled) {
									c.hide();
								} else {
									c.show();
									if (c.xtype == 'textarea') {
										c.setDisabled(false);
									} else {
										if (c.editReadOnly != undefined && c.editReadOnly) {
											c.setReadOnly(true);
											c.addClass("textReadonly");
											// c.el.dom.disabled = true;
										} else {
											c.setReadOnly(false);
											c.removeClass("textReadonly");
											// c.el.dom.disabled =
											// false;
										}
									}
								}
							} catch (e) {

							}

						}
					};

					if (this.remoteLoad) {
						Ext.Ajax.request({
									url : _contextPath_ + '/' + this.initEditAction + '.action',
									success : intForm,
									failure : function(response) {
										var errorWindow = new Ext.Window({
													title : "出错啦！",
													width : 200,
													height : 200,
													maximizable : true,
													autoScroll : true,

													// layout:'fit',
													html : response.responseText
												});
										errorWindow.show();
										errorWindow.maximize();
									},
									params : this.makeParams(c[0]),
									scope : this
								});
					} else {
						if (this.initEditHandler) {
							this.initEditHandler.call(this.scope || this, this, this.win1.items.get(0));
						}
						this.win1.setTitle(this.editTitle);
						this.win1.show();
						this.win1.items.get(0).form.setValues(c[0].json);
						this.win1.items.get(0).items.each(fn);

						// 修改后处理
						if (this.hasListener("updateAfter")) {
							if (!this.fireEvent('updateAfter', c[0].json)) {
								return;
							}
						}

					}

					function raidoOrCheckBoxReadonly(c) {
						var insertItems = c.items;
						if (c.editReadOnly != undefined && c.editReadOnly) {
							var func1 = function(cc) {
								if (!cc.checked) {
									cc.hide();
								}
							};
							insertItems.each(func1);
						}
					}

					this.win1.buttons[0].show();
				} else {
					Ext.MessageBox.show({
								title : '警告',
								msg : "请选择一个对象",
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING
							});
				}
			},

			viewObject : function() {
				var c = this.getSelectionModel().getSelections();
				if (c.length == 1) {
					function intForm(o) {
						if (this.initViewHandler) {
							this.initViewHandler.call(this.scope || this, this, this.win1.items.get(0));
						}
						var resultJson = Ext.util.JSON.decode(o.responseText);
						this.win1.setTitle(this.viewTitle);
						this.win1.show();
						this.win1.items.get(0).form.setValues(resultJson);
						this.win1.items.get(0).items.each(fn);

						if (this.propsGridVisible) {
							Ext.getCmp('propsGrid').setSource(resultJson.propsGrid)
						}
						// 查看后处理
						if (this.hasListener("viewAfter")) {
							if (!this.fireEvent('viewAfter', resultJson)) {
								return;
							}
						}

					}

					var fn = function(c) {
						var insertItems = c.items;
						if (insertItems != undefined && insertItems != null) {
							insertItems.each(fn);
						} else {
							if (this.viewDisVisabled) {
								c.hide();
							} else {
								c.show();
								if (c.xtype == 'textarea') {
									c.setDisabled(true);
								} else {
									c.setReadOnly(true);
									c.addClass("textReadonly");
								}

							}

						}
					};
					if (this.remoteLoad) {
						Ext.Ajax.request({
									url : _contextPath_ + '/' + this.initEditAction + '.action',
									success : intForm,
									failure : function(response) {
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
									params : this.makeParams(c[0]),
									scope : this
								});
					} else {
						if (this.initViewHandler) {
							this.initViewHandler.call(this.scope || this, this, this.win1.items.get(0));
						}

						this.win1.setTitle(this.viewTitle == undefined ? '' : this.viewTitle);
						this.win1.show();
						this.win1.items.get(0).form.setValues(c[0].json);
						this.win1.items.get(0).items.each(fn);
						// 查看后处理
						if (this.hasListener("viewAfter")) {
							if (!this.fireEvent('viewAfter', c[0].json)) {
								return;
							}
						}
					}
					this.win1.buttons[0].hide();
				} else {
					Ext.MessageBox.show({
								title : '警告',
								msg : "请选择一个对象",
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING
							});
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
					if (obj.hasListener("deleteAfter")) {
						obj.fireEvent("deleteAfter");
					}
					/*
					 * //记录日志 var
					 * operateObject=this.listAction.substring(this.listAction.lastIndexOf('/')+1,this.listAction.lastIndexOf('_'));
					 * operInfos('delete',operateObject);
					 */
				}
			},

			saveForm_noTerminate : function(obj) {
				if (obj.win1.items.get(0).form.isValid()) {
					obj.win1.items.get(0).form.submit({
								success : function(form, action) {
									obj.win1.hide();
									obj.store.reload();
									if (action.result.success || 'true' == action.result.success) {
										Ext.MessageBox.show({
													title : '提示',
													msg : action.result.msg,
													buttons : Ext.MessageBox.OK,
													icon : Ext.MessageBox.INFO
												});
									}
									var operate = '';
									if (obj.win1.items.get(0).form.baseParams.dataEditflag == true) {
										operate = 'update';
									} else {
										operate = 'save';
									}
									// 添加修改后处理
									if (obj.hasListener("saveAfter")) {
										obj.fireEvent("saveAfter", action);
									}
									/*
									 * //记录日志 var
									 * operateObject=obj.listAction.substring(obj.listAction.lastIndexOf('/')+1,obj.listAction.lastIndexOf('_'));
									 * operInfos(operate,operateObject);
									 */
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

			deleteObject : function() {
				if (this.hasListener("deleteBefore")) {
					if (!this.fireEvent('deleteBefore')) {
						return;
					}
				}
				var c = this.getSelectionModel().getSelections();

				if (c.length == 1) {
					Ext.MessageBox.confirm('消息', '确认要删除所选对象?', function(btn) {
								// 查询此交易需不需要审批，如果需要审批以什么样的方式进行审批
								if (btn == 'yes') {
									this.doOperation('delete_id', 'delete', this, this.deleteObject_noTerminate, c);
								}
							}, this);
				}

				else {
					Ext.MessageBox.show({
								title : '警告',
								msg : "请选择一个对象",
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING
							});
				}

			},
			// 执行操作 1功能ID 2操作标识，3整个js对象 4原功能执行方法 5删除赋值方法
			doOperation : function(operateWay, operate, obj, executeObject, formData) {
                executeObject(obj);
//				Ext.Ajax.request({
//							url : _contextPath_ + '/jsp/conmg/WorkflowApproveControl_judgeExsitsRecord.action',
//							params : {
//								operateWay : operateWay,
//								menuId : top.mainTabs.activeTab.id.substring(top.mainTabs.activeTab.id.lastIndexOf('_') + 1)
//							},
//							success : function(response) {
//								var result = Ext.util.JSON.decode(response.responseText);
//								var success = result.success;
//								if (success) {
//									var returnObject = result.obj;
//									if (returnObject != "1") {
//										var approval = new Approval();
//										if (returnObject.approveWay == "1") {// 临柜
//											approval.execute = function() {
//												executeObject(obj);
//											};
//											authorise_grant(returnObject.roleid, approval);
//										} else if (returnObject.approveWay == "2") {// 工作流
//											var submitData = null;
//											// 如果是删除
//											if ('delete' == operate) {
//												submitData = formData[0].data;// 表单值
//												submitData.id = formData[0].id;
//											}
//											approval = obj.assemblyApproval(obj, returnObject.processkey, operate, submitData);
//											approval.menuId=top.mainTabs.activeTab.id.substring(top.mainTabs.activeTab.id.lastIndexOf('_') + 1);
//
//											terminatlWay(approval);
//										} else if (returnObject.approveWay == "3") { // 临柜加工作流审核方式
//											var submitData = null;
//											if ('delete' == operate) {
//												submitData = formData[0].data;// 表单值
//												submitData.id = formData[0].id;
//											}
//											approval = obj.assemblyApproval(obj, returnObject.processkey, operate, submitData);
//											approval.execute = function() { // 临柜后处理
//												terminatlWay(approval);
//											};
//											authorise_grant(returnObject.roleid, approval);
//										} else {
//											executeObject(obj);
//										}
//									} else {
//										executeObject(obj);
//									}
//								} else {
//									showErrMsg(result.msg);
//								}
//
//							},
//							failure : function(response) {
//								showErrMsg('程序有错');
//							}
//						});
			},// 组装审核对象
			assemblyApproval : function(obj, key, operate, delData) {
				var approval = new Approval();
				approval.operate = operate;

				if (!Ext.isEmpty(obj.pageUrl)) {
					approval.pageUrl = obj.pageUrl;
				}

				approval.afterExecute = function() {
					obj.win1.hide();
				};
				approval.key = key;
				// 操作标题
				approval.variable = obj.variable;// 审核变量
				if ('delete' == operate) {
					approval.title = obj.deleteTitle;
					approval.url = _contextPath_ + '/' + obj.deleteAction + '.action', approval.submitData = delData;
					approval.recordId = delData.id;
				} else {
					approval.title = obj.win1.title;
					approval.url = obj.getForm().url;
					var idField = obj.getForm().form.findField('id');
					if (null != idField) {
						approval.recordId = idField.getValue();
					}
					// 列表利用sql查询全大写的
					var ID_Field = obj.getForm().form.findField('ID');
					if (null != ID_Field) {
						approval.recordId = ID_Field.getValue();
					}
					if (undefined == obj.getForm().form || undefined == obj.getForm().form.getValues()) {
						Ext.MessageBox.show({
									title : '警告',
									msg : "关键参数为空",
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.WARNING
								});
						return;
					}
					// 表单值
					approval.submitData = obj.getForm().form.getValues();
				}

				if (operate == 'update') {
					approval.submitData.dataEditflag = true;
				}
				return approval;
			},

			saveForm : function() {
				if (this.hasListener("saveBefore")) {
					if (!this.fireEvent('saveBefore')) {
						return;
					}
				}
				if (undefined != this.submitFormBefore) {
					alert('sssssss');
					this.submitFormBefore();
				}

				if (this.win1.items.get(0).form.isValid()) {
					var operate = '';
					var operateWay = '';
					if (this.win1.items.get(0).form.baseParams.dataEditflag == true) {
						operate = 'update';
						operateWay = 'update_id';
					} else {
						operate = 'save';
						operateWay = 'save_id';
					}
					this.doOperation(operateWay, operate, this, this.saveForm_noTerminate);
				} else {
					Ext.MessageBox.show({
								title : '警告',
								msg : "有必输项未输入或数据输入不正确",
								buttons : Ext.MessageBox.OK,
								icon : Ext.MessageBox.WARNING
							});
				}
			},

			// //跨终端授权
			// terminatlWay:function(operate,key,obj){
			// Ext.Ajax.request({
			// url: _contextPath_ +
			// '/jsp/conmg/WorkflowApproveInfo_judgeExsitsRecord.action',//根据ID查询此交易在审批记录表中是否存在
			// params:{id:obj.win1.items.get(0).getForm().findField('id').getValue()},
			// success : function(response) {
			// var result = Ext.util.JSON.decode(response.responseText);
			// var flag = result.flag;
			// if(!flag){
			// obj.win1.hide();
			// showErrMsg('当前记录存在未审批，请先去审批再对此记录进行操作');
			// }else{
			// authorise_terminalway(obj,operate,obj.win1.items.get(0),key);
			// /* //记录日志
			// var
			// operateObject=obj.listAction.substring(obj.listAction.lastIndexOf('/')+1,obj.listAction.lastIndexOf('_'));
			// operInfos(operate,operateObject);*/
			// }
			// },
			// failure : function(response) {
			//						
			// }
			// });
			// },
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

			/**
			 * @deprecated
			 */
			getForm : function() {
				return this.win1.items.get(0);
			},
			/**
			 * 
			 */
			getEditForm : function() {
				return this.win1.items.get(0);
			},
			getWin : function() {
				return this.win1;
			},
			getSearchForm : function() {
				return this.panels[0];
			},
			getStore : function() {
				return this.store;
			},
			getWin : function() {
				return this.win1;
			}

		});
