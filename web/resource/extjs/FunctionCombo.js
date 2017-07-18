/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-4-12
 * Time: 下午5:55
 * To change this template use File | Settings | File Templates.
 */
Ext.ux.FunctionSCombo = Ext.extend(Ext.form.ComboBox, {
            displayField:'codedesc',
            valueField:'code',
            mode: 'local',
            editable: false,
            triggerAction: 'all',
            emptyText :"请选择",

            initComponent: function(config) {
                if (this.store == undefined || this.store == null) {
                    var typestore = new Ext.data.SimpleStore({
                                fields: [this.valueField, this.displayField],
                                data : this.data
                            });

                    this.store = typestore;
                }
                this.hiddenName = this.name;
                Ext.apply(this, config);
                Ext.ux.FunctionSCombo.superclass.initComponent.call(this);
            }

        });

Ext.ux.FunctionDCombo = Ext.extend(Ext.form.ComboBox, {
            displayField:'codedesc',
            valueField:'code',
            mode:'local',
            editable: false,
            triggerAction: 'all',
            emptyText :"请选择",
//    selectOnFocus:true,
//            lazyInit:true,

            initComponent: function(config) {

                if (this.store == undefined || this.store == null) {
                    var RecordDef = Ext.data.Record.create([
                        {
                            name: this.valueField
                        },
                        {
                            name: this.displayField
                        }
                    ]);
                    var typestore = new Ext.data.Store({
                                proxy:new Ext.data.HttpProxy({url:_contextPath_ + '/' + this.action + '.action'}),
                                reader: new Ext.data.JsonReader({ id:"id" }, RecordDef),
                                remoteStore:false
                            });
                    typestore.load();
                    this.store = typestore;
                }
                this.hiddenName = this.name;
                Ext.apply(this, config);
                Ext.ux.FunctionDCombo.superclass.initComponent.call(this);
            }
            ,onLoad:function() {
                if (this.getStore().getCount() > 0) {
                    var first = this.getStore().getAt(0);
//                    alert(first.get(this.valueField));
//                    alert(first.get(this.displayField) == "　");
                    if (this.allowBlank && first.get(this.valueField) != undefined ) {
                        var Record = this.store.reader.recordType;
//                    var f = Record.prototype.fields,
//                            fi = f.items,
//                            fl = f.length;
//                    var emptyRecordData = new Record(this.extractValues(fi, fl));
                        var value = {};
                        value[this.displayField] = "　";
                        var emptyRecordData = new Record(value);
                        this.getStore().insert(0, emptyRecordData);
                    } else if (!this.allowBlank && first.get(this.valueField) == undefined ) {
                        this.getStore().removeAt(0);
                    }
                }
                Ext.ux.FunctionDCombo.superclass.onLoad.call(this);
            }

//            ,extractValues : function(items, len) {
//                var f, values = {};
//                for (var j = 0; j < len; j++) {
//                    f = items[j];
//                    values[f.name] = "　";
//                }
//                return values;
//            }

        });

Ext.reg('scombo', Ext.ux.FunctionSCombo);
Ext.reg('dcombo', Ext.ux.FunctionDCombo);