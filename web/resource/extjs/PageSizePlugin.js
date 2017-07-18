Ext.namespace('Ext.ux');

Ext.ux.PageSizePlugin = function() {
    Ext.ux.PageSizePlugin.superclass.constructor.call(this, {
                store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['20', 20],
                                ['50', 50],
                                ['100', 100],
                                ['250', 250],
                                ['500', 500]
                            ]
                        }),
                mode: 'local',
                displayField: 'text',
                valueField: 'value',
                editable: false,
                allowBlank: false,
                triggerAction: 'all',
                width:50
            });
};

Ext.extend(Ext.ux.PageSizePlugin, Ext.form.ComboBox, {
            init: function(paging) {
//        paging.on('render', this.onInitView, this);
                this.onInitView(paging);
            },

            onInitView: function(paging) {
//        paging.add('-',
//            this,
//            '条/页'
//        );
                paging.insert(11, '-');
                paging.insert(12, this);
                paging.insert(13, "条/页");
                this.setValue(paging.pageSize);
                this.on('select', this.onPageSizeChanged, paging);
            },

            onPageSizeChanged: function(combo) {
                this.pageSize = parseInt(combo.getValue());
//        compage = parseInt(combo.getValue());
                this.doLoad(0);
            }
        });