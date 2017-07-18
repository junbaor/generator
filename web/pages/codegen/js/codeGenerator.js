/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-6-7
 * Time: 上午11:21
 * To change this template use File | Settings | File Templates.
 */
Ext.onReady(function() {
    var download = new Ext.ux.IFrameComponent({ id:"download", renderTo:"downloaddiv"});
    var hiddenPkgs = [];
    var selectAll = new Ext.form.Checkbox({
                boxLabel: '全选'
            });

    var searchNode = new Ext.form.TextField({
                emptyText:'表过滤',
                enableKeyEvents:true
            });

    var treeloader = new Ext.tree.TreeLoader({
                dataUrl :  _contextPath_ + '/CodeGendbTree.action'
            });
    var root = new Ext.tree.AsyncTreeNode({id: '0',
                text: '根' });
    var dbTree = new Ext.tree.TreePanel({
                region:'west',
                margins: '5 0 5 5',
                collapsible: true,
                title:"数据库表",
                split:true,
                rootVisible:false,
                autoScroll:true,
                width: 250,
                minSize: 150,
                maxSize: 400,
                layoutConfig:{
                    animate:true
                },
                containerScroll: true,
                root: root,
                loader :treeloader,
                tbar:[
                    selectAll,"    ",searchNode,"-",
                    {
                        text:"生成",pressed:true,
                        handler:function() {
                            var checkedNodes = dbTree.getChecked();
                            if (checkedNodes.length != 0) {
                                if (globalConfig.form.isValid()) {
                                    globalConfig.collapse();

                                    var tables = [];
                                    var j = 0;
                                    for (var i = 0; i < checkedNodes.length; i++) {
                                        if (!checkedNodes[i].hidden) {
                                            tables[j] = checkedNodes[i].id;
                                            j++;
                                        }
                                    }

                                    Ext.MessageBox.show({
                                                title: '请等待',
                                                msg: '生成中...',
                                                progressText: '进度...',
                                                width:300,
                                                progress:true,
                                                closable:false
                                            });

                                    var f = function(v) {
                                        return function() {
                                            var i = v / tables.length;
                                            Ext.MessageBox.updateProgress(i, Math.round(100 * i) + '% 完成');
                                        };
                                    };
                                    for (var i = 1; i < tables.length + 1; i++) {
                                        setTimeout(f(i), i * 300);

                                    }
                                    HttpRequest({
                                                url : _contextPath_ + '/CodeGengenerator.action',
                                                params:Ext.applyIf(globalConfig.form.getValues(), {tables:tables + ""}),
                                                success : function(response) {
                                                    var result = Ext.util.JSON.decode(response.responseText);
                                                    if (result.success == true) {
                                                        Ext.MessageBox.confirm('生成完成', '是否打开生成文件夹？',
                                                                function(btn) {
                                                                    if (btn == 'yes') {
                                                                    	 download.setUrl(_contextPath_ + '/ EsbOrmGendownLoadFolder.action');                                                                      /* HttpRequest({
                                                                                    url : _contextPath_ + '/CodeGenopenOutRoot.action'
                                                                         });*/
                                                                    }
                                                                },
                                                                this);
                                                    }
//                                                    else if (result.success == false) {
//                                                        Ext.MessageBox.alert('提示', '请填写并保存全局配置参数',
//                                                                function() {
//                                                                    globalConfig.expand();
//                                                                    globalConfig.form.isValid();
//                                                                },
//                                                                this);
//                                                    }
                                                }
                                            });
                                } else {
                                    Ext.MessageBox.alert('提示', '请填写并保存全局配置参数',
                                            function() {
                                                globalConfig.expand();
                                                globalConfig.form.isValid();
                                            },
                                            this);
                                }
                            } else {
                                Ext.MessageBox.alert('提示', '请选择要生成的表');
                            }
                        }
                    }
                ]
            });

    selectAll.on("check", function() {
        Ext.each(root.childNodes, function(n) {
            n.getUI().toggleCheck(selectAll.getValue());
        });
    });
    var treefilter = new Ext.tree.TreeFilter(dbTree, {
                clearBlank: true,
                autoClear: true
            });

    searchNode.on("keyup", function() {
        var text = searchNode.getValue();
        Ext.each(hiddenPkgs, function(n) {
            n.ui.show();
        });
        if (!text) {
            treefilter.clear();
            return;
        }

        dbTree.expandAll();

//        selectAll.setValue(false);
//        Ext.each(root.childNodes, function(n) {
//            n.getUI().toggleCheck(false);
//        });

        var re = new RegExp(Ext.escapeRe(text), 'i');

        treefilter.filterBy(function(n) {
            return !n.isLeaf() || re.test(n.text);
        });

        hiddenPkgs = [];

        dbTree.root.cascade(function(n) {
            if (!n.isLeaf() && n.ui.ctNode.offsetHeight < 3) {
                n.ui.hide();
                hiddenPkgs.push(n);
            }
        });

    });

    dbTree.on("click", function(node, e) {
        HttpRequest({
                    url : _contextPath_ + '/CodeGeninitTable.action',
                    success:function(response) {
                        var result = Ext.util.JSON.decode(response.responseText);
							paging.setValue(result.paging);
							columnLock.setValue(result.columnLock);
							edit.setValue(result.edit);
							stripeRows.setValue(result.stripeRows);
							columnLines.setValue(result.columnLines);
							girdPlugins.setValue(result.girdPlugins);
							layoutField.setValue(result.layout);
							colspanField.setValue(result.colspan);
                    },
                    params:{table:node.id}
                });
        ds.load({params:{table:node.id}});
    });

    var xtypeStore = new Ext.data.SimpleStore({
                fields: ['id', 'name'],
                data : [
                    ["textfield", 'textfield' ],
                    ["numberfield", 'numberfield' ],
                    ["datefield", 'datefield' ],
                    ["dcombo",'select'],
                    ["radiogroup", 'radiogroup'],
                    ["checkboxgroup", 'checkboxgroup'],
                    ["textarea", 'textarea'],
                    ["myitemselector", 'myitemselector'],
                    ["lovcombo", 'lovcombo']
                ]
            });
    var vtypeStore = new Ext.data.SimpleStore({
                fields: ['id', 'name'],
                data : [
                    [null,"　"],
                    ["url", 'url' ],
                    ["email", 'email' ],
                    ["phone", '电话,传真' ],
                    ["mobile", '手机' ],
                    ["idcard", '身份证' ],
                    ["postCode", '邮编' ],
                    ["IPAddress", 'ip']
                ]
            });
    var reader = new Ext.data.JsonReader({
                root:'root',id:'name',
                fields:  [
                    {
                        name:'name'
                    },
                    {
                        name:'remark'
                    },
                    {
                        name:'type'
                    },
                    {
                        name:'size'
                    },
                    {
                        name:'pk'
                    },
                    {
                        name:'grid', type: 'bool'
                    },
                    {
                        name:'search', type: 'bool'
                    },
                    {
                        name:'form', type: 'bool'
                    },
                    {
                        name:'xtype'
                    },
                    {
                        name:'vtype'
                    },
                    {
                        name:'editReadOnly', type: 'bool'
                    }, {
    					name : 'indexView'
    				} , {
    					name : 'colspan'
    				}
                ]}
    );

    var ds = new Ext.data.Store({
                proxy:new Ext.data.HttpProxy({url: _contextPath_ + '/CodeGendbColumn.action'}),
                remoteSort:false,
                reader:reader
            });

    var cm = new Ext.ux.grid.LockingColumnModel([
        {
            header:'字段名称',
            sortable: false,
            locked:true,
            width:120,
            dataIndex:'name'
        },
        {
            header:'字段备注',
            sortable: false,
            locked:true,
            dataIndex:'remark'
        },
        {
            header:'字段类型',
            sortable: false,
            width:80,
            dataIndex:'type'
        },
        {
            header:'最大长度',
            sortable: false,
            width:60,
            dataIndex:'size'
        },
        {
            header:'主键',
            sortable: false,
            width:60,
            xtype: 'checkcolumn',
            dataIndex:'pk'
        },
        {
            header:'列表显示',
            sortable: false,
            xtype: 'checkcolumn',
            width:60,
            dataIndex:'grid'
        },
        {
            header:'查询字段',
            sortable: false,
            width:60,
            xtype: 'checkcolumn',
            dataIndex:'search'
        },
        {
            header:'表单字段',
            sortable: false,
            width:60,
            xtype: 'checkcolumn',
            dataIndex:'form'
        },
        {
            header:'输入风格',
            sortable: false,
            dataIndex:'xtype',
            editor: new Ext.form.ComboBox({
                        store: xtypeStore,
                        triggerAction: 'all',
                        displayField:'name',
                        valueField:'id',
                        mode: 'local',
                        editable: false,
                        lazyRender: true

                    }),
            renderer: showXtypeDisplay
        },
        {
            header:'输入校验',
            sortable: false,
            dataIndex:'vtype',
            editor: new Ext.form.ComboBox({
                        store: vtypeStore,
                        triggerAction: 'all',
                        displayField:'name',
                        valueField:'id',
                        mode: 'local',
                        editable: false,
                        lazyRender: true

                    }),
            renderer: showVtypeDisplay
        },
        {
            header:'编辑不可更改',
            sortable: false,
            xtype: 'checkcolumn',
            dataIndex:'editReadOnly'
        }, {
			header : '表单值大小',
			dataIndex : 'colspan',
			editor : new Ext.form.NumberField({
			
			})
		} 
    ]);

    function showXtypeDisplay(value, cellmeta, record) {
        var index = xtypeStore.find("id", value);
        var record = xtypeStore.getAt(index);
        var displayText = "";
        if (record == null) {
            displayText = value;
        } else {
            displayText = record.get('name');
        }
        return displayText;
    }

    function showVtypeDisplay(value, cellmeta, record) {
        var index = vtypeStore.find("id", value);
        var record = vtypeStore.getAt(index);
        var displayText = "";
        if (record == null) {
            displayText = value;
        } else {
            displayText = record.get('name');
        }
        return displayText;
    }

    var girdPluginStore = new Ext.data.SimpleStore({
                fields: ['id', 'name'],
                data : [
                    ["RowNumberer", '行数字' ],
                     ["Checkbox", '行多选' ],
                    ["RowExpander", '行展开' ]
                ]
            });

    var tbar = new Ext.Toolbar();
    var paging = new Ext.form.Checkbox({name:"paging"});
    var columnLock = new Ext.form.Checkbox({name:"columnLock"});
    var edit = new Ext.form.Checkbox({name:"edit"});
    var stripeRows = new Ext.form.Checkbox({name:"stripeRows"});
    var columnLines = new Ext.form.Checkbox({name:"columnLines"});
    var girdPlugins = new Ext.ux.form.CheckboxCombo({
                store: girdPluginStore,
                width:100,
                displayField:'name',
                valueField:'id',
                mode: 'local',
                editable: false
            });
     var layoutField = new Ext.form.ComboBox({
				name : "layoutName",
				hiddenName : "layout",
				fieldLabel : "列值",
				width:100,
				store : new Ext.data.JsonStore({
							fields : ["id", "name"],
							data : [{
										"id" : "column",
										"name" : "列式布局"
									}, {
										"id" : "table",
										"name" : "表格布局"
									}]
						}),
				triggerAction : 'all',
				displayField : 'name',
				valueField : 'id',
				mode : 'local',
				fieldLabel : '数据源',
				editable : true,
				listeners:{
					select : function(c, record, index) {
						HttpRequest({
							url : _contextPath_ + '/CodeGenchangeTable.action',
							params : {
								table : dbTree.getSelectionModel().getSelectedNode().id,
								attribute :'layout',
								value : c.getValue()
							}
						});

			       }
				}
			});
    var colspanField = new Ext.form.NumberField({
				name : "colspan",
				fieldLabel : "列值",
				width : 70,
				value : 2,
				listeners : {
					change : function(c, newValue, oldValue) {
							HttpRequest({
							url : _contextPath_ + '/CodeGenchangeTable.action',
							params : {
								table : dbTree.getSelectionModel().getSelectedNode().id,
								attribute :'colspan',
								value : newValue
							}
						});
					}
				}

			});  
            
    var checkArray = [paging,columnLock,edit,stripeRows,columnLines];
    for (var i = 0; i < checkArray.length; i++) {
        var obj = checkArray[i];
        obj.on("check", function(checkbox, value) {
            HttpRequest({
                        url : _contextPath_ + '/CodeGenchangeTable.action',
                        params:{table:dbTree.getSelectionModel().getSelectedNode().id,attribute:checkbox.name,value:value}
                    });
        });

    }

    girdPlugins.on("collapse", function(cmb, value) {
            HttpRequest({
                        url : _contextPath_ + '/CodeGenchangeTable.action',
                        params:{table:dbTree.getSelectionModel().getSelectedNode().id,attribute:"girdPlugins",value:girdPlugins.getValue()}
                    });
    });
    
   
    var dataSource = new Ext.data.Store({
        proxy:new Ext.data.HttpProxy({url:_contextPath_ + '/EsbOrmGenlistDataSource.action'}),
        autoLoad:true,
        reader:new Ext.data.JsonReader({}, [
            {name:"id"},
            {name:"name"}
        ]),
        remoteSort:false
    });
    dataSource.load();
    var selectdataSources1 = new Ext.form.ComboBox({
        store:dataSource,
        triggerAction:'all',
        displayField:'name',
        valueField:'id',
        mode:'local',
        fieldLabel:'数据源',
        editable:false,
        name:"datasource",
        hiddenName:"datasource",
        listeners:{
            select:function () {
                //  alert(this.getValue());
                if (!Ext.isEmpty(this.getValue())) {
                    HttpRequest({
                        url:_contextPath_ + '/CodeGensetDataSource.action',
                        timeout:20000,
                        success:function (response) {
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.success) {
                                grid.getStore().removeAll();
                                //dbTree.getRootNode().removeAll(true);
                                root.reload();
                            } else {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    msg:'连接失败',
                                    buttons:Ext.MessageBox.OK,
                                    icon:Ext.MessageBox.WARNING
                                });
                            }

                        },
                        params:{sid:this.getValue()}
                    });
                }
            }
        }

    });
    tbar.add("分页:", paging, "-", "增删改查:", edit, "-", "列锁定:", columnLock,
            "-", "斑马线:", stripeRows, "-", "列线:", columnLines,"-", "插件:", girdPlugins,"布局方式:",layoutField,"列值:",colspanField ,"->","数据源:",selectdataSources1);

    var grid = new Ext.grid.EditorGridPanel({
                region:'center',
                id:'topic-grid',
                title:"字段",
                margins: '5 5 5 0',
                ds:ds,
                cm:cm,
                columnLines:true,
                stripeRows:true,
//                autoExpandColumn:'name',
                viewConfig:   {
                    forceFit: true
                },
                tbar:tbar,
                view:new Ext.ux.grid.LockingGridView(),
                loadMask:true
            });

    function afterEdit(e) {
        var r = e.record;
        var columnName = r.get("name");
        if (e.field == "xtype" && (e.value == "dcombo" || e.value == "radiogroup" || e.value == "checkboxgroup"|| e.value == "myitemselector"||e.value =="lovcombo")) {
            form.form.baseParams = {table:dbTree.getSelectionModel().getSelectedNode().id,columnName:columnName};
            form.form.load({
                        url : _contextPath_ + '/CodeGenloadSelectCode.action'
                    });
            window.show();
        }
        if (e.value != e.originalValue) {
            HttpRequest({
                        url : _contextPath_ + '/CodeGenmakeParams.action',
                        params:{table:dbTree.getSelectionModel().getSelectedNode().id,columnName:columnName,field:e.field,value:e.value}
                    });
        }
    }
    
    grid.on("afteredit", afterEdit, this);


    var form = new Ext.form.FormPanel({
                labelWidth: 65,
                url:_contextPath_ + '/CodeGensaveCode.action',
                defaultType: 'textfield',
                frame:true,
                items: [
                    {
                        fieldLabel: '关联表',
                        name: 'tableName',
                        value:'EACODE',
                        allowBlank:false,
                        anchor:'95%'
                    },
                    {
                        fieldLabel: '关联字段',
                        name: 'field',
                        value:'FIELD',
                        anchor:'95%'
                    },
                    {
                        fieldLabel: '存储字段',
                        name: 'code',
                        value:'CODE',
                        allowBlank:false,
                        anchor:'95%'
                    },
                    {
                        fieldLabel: '显示字段',
                        name: 'codedesc',
                        value:'CODEDESC',
                        allowBlank:false,
                        anchor:'95%'
                    },
                    {
                        fieldLabel: '关联字段值',
                        name: 'fieldvalue',
                        anchor:'95%'
                    }
                ]
            });

    var window = new Ext.Window({
                title: '关联表配置',
                width: 250,
                height:240,
                layout:'fit',
                closable:false,
                closeAction:'hide',
                plain:true,
                modal:true,
                allowBlank:false,
                buttonAlign:'center',
                items: form,
                buttons: [
                    {
                        text: '确定',
                        handler:saveForm
                    }
//                    ,{
//                        text: '取消',
//                        handler:function() {
//                            window.hide();
//                        }
//                    }
                ]
            });

    function saveForm() {
        if (form.form.isValid()) {
            form.form.submit({
                        success: function(form, action) {
                            window.hide();
                        }
                    });
        }
    }

    var globalConfig = new Ext.FormPanel({
		collapsible : true,
		height : 70,
		collapseMode : 'mini',
		region : 'south',
		title : "全局配置",
		labelAlign : 'right',
		url : _contextPath_ + '/CodeGensaveGlobal.action',
		frame : true,
		items : [{
					layout : 'column',
					border : false,
					items : [{
								columnWidth : .25,
								layout : 'form',
								items : [{
											xtype : 'textfield',
											fieldLabel : 'java生成路径',
											id : 'basepackage',
											name : 'basepackage',
											value : "com.dbank",
											allowBlank : false,
											anchor : '95%'
										}]
							}, {
								columnWidth : .25,
								layout : 'form',
								items : [{
											xtype : 'textfield',
											fieldLabel : 'jsp,js生成路径',
											name : 'namespace',
                                            id:"namespace",
											value : "jsp/",
											allowBlank : false,
											anchor : '95%'
										}]
							},
//                        {
//								columnWidth : .25,
//								layout : 'form',
//								items : [{
//											xtype : 'textfield',
//											fieldLabel : '模块名',
//											name : 'model',
//											id:'model',
//											allowBlank : false,
//											anchor : '95%'
//										}]
//							},
                        {
								columnWidth : .25,
								layout : 'form',
								items : [{
											xtype : "combo",
											name : "generatorMothedid",
											hiddenName : "generatorMothed",
											fieldLabel : "代码生成方式",
											anchor : '95%',
											store : new Ext.data.JsonStore({
														fields : ["id", "name"],
														data : [  {
															"id" : "dipper_po",
															"name" : "dipper_po"
														},
                                                        {
                                                            "id" : "oms_mybatis",
                                                            "name" : "oms_mybatis"
                                                        },
                                                        {
                                                                "id" : "oms_hibernate",
                                                                "name" : "oms_hibernate"
                                                          },
                                                         {
															"id" : "editorGrid",
															"name" : "editorGrid"
														 }
                                                        ]
													}),
											triggerAction : 'all',
											displayField : 'name',
											valueField : 'id',
											mode : 'local',
											editable : true,
											value : 'oms_mybatis',
											listeners : {
												select : function(combo,record,index) {
													if (record.id == 'dipper_po') {
                                                        Ext.getCmp('basepackage').show();
                                                        Ext.getCmp('namespace').hide();
//														Ext.getCmp("basepackage")
//																.setValue("com.dbank");
													}else{
                                                        Ext.getCmp('basepackage').show();
                                                        Ext.getCmp('namespace').show();
                                                    }
												}

											} 
										}]
							}]
				}]

// ,buttons: [
//                    {
//                        text: '保存',
//                        handler:function() {
//                            if (globalConfig.form.isValid()) {
//                                globalConfig.form.submit({
//                                            success: function(form, action) {
//                                                globalConfig.collapse();
//                                            }
//                                        });
//                            }
//
//                        }
//                    }
//                ]

            });
    globalConfig.form.load({
                url : _contextPath_ + '/CodeGeninitglobalConfig.action'
            });


    var viewport = new Ext.Viewport({
                layout:'border',
                items:[
                    dbTree,
                    grid,
                    globalConfig
                ]
            });
});