/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-7-27
 * Time: 下午3:44
 * To change this template use File | Settings | File Templates.
 */
Ext.override(Ext.form.NumberField, {
            setValue : function(v) {
                v = this.fixPrecision(v);
                v = Ext.isNumber(v) ? v : String(v).replace(this.decimalSeparator, ".");
                v = isNaN(v) ? '' : String(v).replace(".", this.decimalSeparator);
                return Ext.form.NumberField.superclass.setValue.call(this, v);
            },
            fixPrecision : function(value) {
                var nan = isNaN(value);
                if (!this.allowDecimals || this.decimalPrecision == -1 || nan ) {
                    return nan ? '' : value;
                }
                return parseFloat(value).toFixed(this.decimalPrecision);
            }
        });