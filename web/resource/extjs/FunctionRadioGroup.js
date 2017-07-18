/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-6-15
 * To change this template use File | Settings | File Templates.
 */

Ext.ux.FunctionRadioGroup = Ext.extend(Ext.form.RadioGroup, {
            displayField:'codedesc',
            valueField:'code',

            initComponent: function() {
                if (this.store) {
                    this.items =new Array();
                    this.store.each(function(record) {
                        var radio = {boxLabel:record.get(this.displayField),name:this.name,inputValue:record.get(this.valueField)};
                        this.items.push(radio);
                    },this);
                }
                Ext.ux.FunctionRadioGroup.superclass.initComponent.call(this);
            }
        });

Ext.reg('dradiogroup', Ext.ux.FunctionRadioGroup);
