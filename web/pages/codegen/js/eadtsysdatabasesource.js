Ext.namespace("org.conmg");
Ext.onReady(function () {
    /**
     * 创建表格
     * @param config
     */
    org.conmg.getGrid = function (config) {
        var dbdriverStore = new Ext.data.SimpleStore({
            fields:['value','text'],
            data:[
                ['com.mysql.jdbc.Driver','com.mysql.jdbc.Driver'],
                ['com.ibm.db2.jcc.DB2Driver','com.ibm.db2.jcc.DB2Driver'],
                ['oracle.jdbc.driver.OracleDriver','oracle.jdbc.driver.OracleDriver']

            ]
        });


        var grid = new Ext.ux.FunctionGrid({
            title: "记录数据库配置信息列表",
            searchColumn: 3,
            dataId: "databasesourcedbid",
            columns: [
                {
                    header: '配置名称', sortable: true, dataIndex: 'configname'
                },
                {
                    header: '数据库版本', sortable: true, dataIndex: 'dbversion'
                },
                {
                    header: '连接地址', sortable: true, dataIndex: 'connecturl'
                },
                {
                    header: '驱动类', sortable: true, dataIndex: 'dbdriver'
                }
            ],
            stripeRows: true,
            columnLines: true,
            listAction: "jsp/conmg/EadtSysDatabaseSource_list",
            addTitle: "增加记录数据库配置信息",
            editTitle: "编辑记录数据库配置信息",
            viewTitle: "查看记录数据库配置信息",
            initEditAction: "jsp/conmg/EadtSysDatabaseSource_find",
            saveAction: "jsp/conmg/EadtSysDatabaseSource_save",
            deleteAction: "jsp/conmg/EadtSysDatabaseSource_delete",
            searchSet: [
                {"anchor": "95%", "fieldLabel": "配置名称", "name": "configname", "xtype": "textfield"},
                {"anchor": "95%", "fieldLabel": "数据库版本", "name": "dbversion", "xtype": "textfield"},
                {"anchor": "95%", "fieldLabel": "连接地址", "name": "connecturl", "xtype": "textfield"}
            ],
            formSet: [
                {xtype: 'hidden', fieldLabel: '数据源配置记录ID', name: 'databasesourcedbid'},
                {
                    layout: 'column',
                    items: [
                        {
                            columnWidth: .5,
                            layout: 'form',
                            items: [
                                {
                                    "allowBlank": false,
                                    "anchor": "95%",
                                    "fieldLabel": "配置名称",
                                    "maxLength": 200,
                                    "name": "configname",
                                    "xtype": "textfield"
                                },
                                {
                                    "allowBlank": false,
                                    "anchor": "95%",
                                    "fieldLabel": "连接地址",
                                    "maxLength": 500,
                                    "name": "connecturl",
                                    "xtype": "textfield"
                                },
                                {
                                    "allowBlank": true,
                                    "anchor": "95%",
                                    "fieldLabel": "用户名",
                                    "maxLength": 200,
                                    "name": "username",
                                    "xtype": "textfield"
                                }
                            ]
                        },
                        {
                            columnWidth: .5,
                            layout: 'form',
                            items: [
                                {
                                    "allowBlank": false,
                                    "anchor": "95%",
                                    "fieldLabel": "数据库版本",
                                    "maxLength": 50,
                                    "name": "dbversion",
                                    "xtype": "textfield"
                                },
                                {
                                    "allowBlank": false,
                                    "anchor": "95%",
                                    "fieldLabel": "驱动类",
                                    mode : 'local',
                                    "name": "dbdriver",
                                    "hiddenName":"dbdriver",
                                    valueField : 'value',
                                    displayField : 'text',
                                    "store": dbdriverStore,
                                    "xtype": "combo"
                                },
                                {
                                    "allowBlank": true,
                                    "anchor": "95%",
                                    "fieldLabel": "密码",
                                    "maxLength": 50,
                                    "name": "password",
                                    "xtype": "textfield"
                                }
                            ]
                        }
                    ]
                }
            ]
        });
        var btn1 = new Ext.Button({
            text: '测试连接',
            iconCls: 'database_connectIcon',
            handler: function () {
                var fid = grid.getSelectionModel().getSelected().json["databasesourcedbid"];
                HttpRequest({
                    url: _contextPath_ + '/jsp/conmg/EadtSysDatabaseSource_testConnect.action',
                    timeout: 2000,
                    success: function (response) {
                        var result = Ext.util.JSON.decode(response.responseText);
                        if (result.success) {
                            Ext.MessageBox.show({
                                title: '信息',
                                msg: result.msg,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.INFO
                            });
                        } else {
                            Ext.MessageBox.show({
                                title: '信息',
                                msg: result.msg,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING
                            });
                        }
                    },
                    failure: function () {
                        Ext.MessageBox.show({
                            title: '信息',
                            msg: "连接失败",
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING
                        });
                    },
                    params: {sid: fid}
                });
            }
        });
        var btn2 = new Ext.Button({
            text: '复制新增 ',
            iconCls: 'page_refreshIcon',
            handler: function () {
                var c = grid.getSelectionModel().getSelections();
                if (Ext.isEmpty(c)) {
                    Ext.MessageBox.show({
                        title: '警告',
                        msg: "请选择一个对象",
                        buttons: Ext.MessageBox.OK,
                        icon: Ext.MessageBox.WARNING
                    });
                    return;
                } else {
                    grid.win1.items.get(0).form.setValues(c[0].json);
                    grid.addObject();
                }
            }
        });
        grid.getBottomToolbar().add('-', btn1, '-', btn2);
        return grid;
    }

    /**
     * 布局视图
     */
    new Ext.Viewport({
        layout: 'border',
        items: [org.conmg.getGrid().panels]
    });
//    flowUtils.isUseFlow('jsp/conmg/EadtSysDatabaseSource_findByProcessId',org.conmg.getGrid());

});