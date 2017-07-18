Ext.ux.FormWindow = Ext.extend(Ext.Window, {
	formItem : [],
	jasperNameValue : "",
	tellerSeqValue : "",
	voucherValue : "",
	saveText : "提交",
	saveUrl : "",
	printUrl : "",
	layout : 'border',
	width : window.screen.width - 250,
	height : window.screen.height - 300,
	closable : false,
	modal : true,
	initComponent : function(config) {
		var formItem = [ {
			fieldLabel : 'jasper名称',
			maxLength : 32,
			id : 'jasperName',
			name : 'jasperName',
			allowBlank : false,
			value : this.jasperNameValue,
			xtype : 'hidden'
		}, {
			fieldLabel : '流水表序号',
			maxLength : 10,
			id : 'tellerSeq',
			name : 'tellerSeq',
			allowBlank : false,
			xtype : 'hidden',
			value : this.tellerSeqValue
		}, {
			maxLength : 40,
			allowBlank : true,
			xtype : 'hidden',
			name : "voucher",
			anchor : '100%',
			value : this.voucherValue
		}, {
			xtype : 'hidden',
			name : "flag"
		} ];

		this.formItem = Ext.apply(formItem, this.formItem);

		var fromPanel = new Ext.form.FormPanel({
			id : 'voucherForm',
			autoScroll : true,
			modal : true,
			stripeRows : true,
			columnLines : true,
			labelAlign : 'right',
			labelWidth : 120,
			buttonAlign : 'center',
			bodyStyle : 'padding:5 5 5 5',
			items : [ {
				layout : 'column',
				defaults : {
					columnWidth : .5,
					layout : 'form'
				},
				items : this.formItem
			} ]
		});
		this.items = [ {
			id : 'voucherPanel',
			height : 450,
			frame : true,
			region : 'center',
			xtype : "panel",
			buttonAlign : 'center',
			items : fromPanel
		} ];
		this.buttons = [ {
			text : this.saveText,
			handler : this.saveInfos,
			scope : this
		} ];
		Ext.ux.FormWindow.superclass.initComponent.call(this);
	},
	saveInfos : function() {
		var voucherForm=Ext.getCmp("voucherForm");
		if (voucherForm.getForm().isValid()) {
			var forms = voucherForm.getForm().getValues();
			var waitMsg = Ext.Msg.wait("正在通信，请不要关闭浏览器...");
			Ext.Ajax.request({
				url : _contextPath_ + this.saveUrl,
				params : forms,
				success : function(response) {
					waitMsg.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					var code = result.RspCode;
					var msg = result.RspMsg;
					if (result.success == true) {
						var backInfo = result.backInfo;
						delete backInfo.__proto__;
						Ext.getCmp("voucherForm").getForm().setValues(Ext.encode(backInfo));
						Ext.MessageBox.show({
							title : '提示',
							msg : '是否打印？',
							width : 200,
							buttons : Ext.MessageBox.OKCANCEL,
							fn : function(btn, text) {
								if (btn == 'ok'){								
									exportCatalog(Ext.getCmp("voucherForm"));
								}
							}
						})

					} else {
						showErrMsg(code + ':' + msg);
					}
				},
				failure : function(response) {
					waitMsg.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					var msg = result.RspMsg;
					var code = result.RspCode;
					showErrMsg(code + ':' + msg);
				}
			});
		} else {
			Ext.MessageBox.show({
				title : '警告',
				msg : "有必输项未输入或数据输入不正确",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.WARNING
			});
		}
	}
});