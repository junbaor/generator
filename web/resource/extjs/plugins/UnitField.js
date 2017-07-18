/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-7-4
 * Time: 下午7:47
 * To change this template use File | Settings | File Templates.
 */
Ext.override(Ext.form.TextField, {
            unitText : '',
            charLength:null,
            charLengthText:"该输入项的固定长度是 {0} 个字符",
            onRender : function(ct, position) {
                Ext.form.TextField.superclass.onRender.call(this, ct, position);
                // 如果单位字符串已定义 则在后方增加单位对象
                if (this.unitText != '') {
                    this.unitEl = ct.createChild({
                                tag : 'div',
                                html : this.unitText
                            });
                    this.unitEl.addClass('x-form-unit');
//                    // 增加单位名称的同时 按单位名称大小减少文本框的长度 初步考虑了中英文混排 未考虑为负的情况
                    if (this.width != undefined) {
                        this.width = this.width - (this.unitText.replace(/[^\x00-\xff]/g, "xx").length * 6 + 2);
                    }
//                    // 同时修改错误提示图标的位置
                    this.alignErrorIcon = function() {
                        this.errorIcon.alignTo(this.unitEl, 'tl-tr', [2, 0]);
                    };
                }
            },

            getErrors: function(value) {
                var errors = Ext.form.TextField.superclass.getErrors.apply(this, arguments);

                value = Ext.isDefined(value) ? value : this.processValue(this.getRawValue());

                if (Ext.isFunction(this.validator)) {
                    var msg = this.validator(value);
                    if (msg !== true) {
                        errors.push(msg);
                    }
                }

                if (value.length < 1 || value === this.emptyText) {
                    if (this.allowBlank) {
                        //if value is blank and allowBlank is true, there cannot be any additional errors
                        return errors;
                    } else {
                        errors.push(this.blankText);
                    }
                }

                if (!this.allowBlank && (value.length < 1 || value === this.emptyText)) { // if it's blank
                    errors.push(this.blankText);
                }

                if (value.replace(/[^\x00-\xff]/g, "xx").length < this.minLength) {
                    errors.push(String.format(this.minLengthText, this.minLength));
                }

                if (value.replace(/[^\x00-\xff]/g, "xx").length > this.maxLength) {
                    errors.push(String.format(this.maxLengthText, this.maxLength));
                }
                if (this.charLength != null && (value.replace(/[^\x00-\xff]/g, "xx").length != this.charLength)) {
                    errors.push(String.format(this.charLengthText, this.charLength));
                }
                if (this.vtype) {
                    var vt = Ext.form.VTypes;
                    if (!vt[this.vtype](value, this)) {
                        errors.push(this.vtypeText || vt[this.vtype + 'Text']);
                    }
                }

                if (this.regex && !this.regex.test(value)) {
                    errors.push(this.regexText);
                }

                return errors;
            }
        });