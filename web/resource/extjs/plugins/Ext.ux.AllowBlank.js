/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-7-4
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
Ext.ns("Ext.ux", "Ext");

Ext.ux.AllowBlank = Ext.extend(Object, {

            /**
             * @param String
             */
//            asteriskColor: '#8B0000',
            asteriskColor: 'red',

            /**
             * @param Boolean
             */
            hint: true,

            /**
             * @param String
             */
            hintText: 'Required fields',

            /**
             * @param String
             */
            hintCls: '',

            /**
             * @param String
             */
            hintStyle: 'font-family: tahoma,arial,helvetica,sans-serif; font-size: 12px; margin-top: 20px;',

            /**
             * constructor
             * @param config
             */
            constructor: function(config) {
                config = config || {};
                Ext.apply(this, config);

            },
            init: function(form) {

                var requiredFields = false;
                var color = this.asteriskColor;
                var separator;

                Ext.each(form.find(), function (item) {
                    if (item.allowBlank === false) {

                        if (item.labelSeparator) {
                            separator = item.labelSeparator;
                        } else if (form.labelSeparator) {
                            separator = form.labelSeparator;
                        } else {
                            separator = ':';
                        }

                        item.labelSeparator = '<span style="color:' + color + ';">*</span>' + separator;
                        requiredFields = true;
                    }
                });

                if (requiredFields && this.hint) {
                    form.add({
                                xtype: 'box',
                                anchor: '100%',
                                autoEl:{
                                    tag:'div',
                                    html: '<span style="color:' + color + ';">*</span> ' + this.hintText,
                                    style: this.hintStyle,
                                    cls: this.hintCls
                                }
                            });
                }
            }
        });

Ext.preg('AllowBlank', Ext.ux.AllowBlank);