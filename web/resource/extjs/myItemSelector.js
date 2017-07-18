Ext.ux.myItemSelector = Ext.extend(Ext.ux.ItemSelector, {
    addRecordCall: '',
    delRecordCall: '',
    sinClickCall: '',
    imagePath: "../../resource/extjs/ux/images/",

    toData: null,// 右
    valueField: "value",
    displayField: "text",
    toLegend: "已选列",
    fromLegend: "未选列",
    fromData: null,// 左
    drawUpIcon: false,
    drawDownIcon: false,
    drawTopIcon: false,
    drawBotIcon: false,
    msWidth: 155,
    msHeight: 150,
    hideNavIcons: false,
    fromStore: null,
    toStore: null,
    fromData: null,
    toData: null,
    displayField: 0,
    valueField: 1,
    switchToFrom: false,
    allowDup: false,
    focusClass: undefined,
    delimiter: ',',
    readOnly: false,
    toLegend: null,
    fromLegend: null,
    toSortField: null,
    fromSortField: null,
    toSortDir: 'ASC',
    fromSortDir: 'ASC',
    toTBar: null,
    fromTBar: null,
    bodyStyle: null,
    border: false,
    defaultAutoCreate: {
        tag: "div"
    },
    initComponent: function () {
        this.initStore();
        Ext.ux.myItemSelector.superclass.initComponent.call(this);// 调用父类初始化
    },
    onRender: function (ct, position) { // 扩展组件自己的渲染事件
        Ext.ux.myItemSelector.superclass.onRender.call(this, ct,
            position); // 调用父类的渲染事件

    },
    initStore: function (callback) {
        if (!this.toData) {
            this.toData = new Array();
        }
        if (!this.fromData) {
            this.fromData = new Array();
        }
        if (!this.fromStore) {
            this.fromStore = new Ext.data.ArrayStore({ // 数据仓库
                fields: [this.valueField, this.displayField],
                data: this.fromData
            });
        }

        if (!this.toStore) {
            this.toStore = new Ext.data.ArrayStore({ // 数据仓库
                fields: [this.valueField, this.displayField],
                data: this.toData
            });
        }
        if (this.value) {
            var values = this.value.split(this.delimiter);
            for (var i = 0; i < values.length; i++) {
                for (var j = 0; j < this.store.getCount(); j++) {
                    var record = this.store.getAt(j);
                    if (record.get(this.valueField) == values[i]) {
                        // this.toData.push(record.data);
                        this.toStore.add(record);
                        break;
                    }
                }
            }
            for (var j = 0; j < this.store.getCount(); j++) {
                var flag = true;
                var record = this.store.getAt(j);
                for (var i = 0; i < values.length; i++) {
                    if (record.get(this.valueField) == values[i]) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    this.fromStore.add(record);
                }
            }
        } else {
            for (var j = 0; j < this.store.getCount(); j++) {
                // this.fromData.push(this.store.getAt(j).data);
                this.fromStore.add(this.store.getAt(j));

            }
        }

        if (!this.multiselects) {
            // this.fromStore.commitChanges() ;
            // this.toStore.commitChanges() ;

            this.multiselects = new Array();
            this.multiselects[0] = new Ext.ux.Multiselect({// 定义一个
                legend: this.fromLegend,
                delimiter: this.delimiter,
                allowDup: this.allowDup,
                copy: this.allowDup,
                allowTrash: this.allowDup,
                width: this.msWidth,
                height: this.msHeight,
                dataFields: this.dataFields,
                data: this.fromData,
                displayField: this.displayField,
                valueField: this.valueField,
                store: this.fromStore,
                isFormField: false,
                tbar: this.fromTBar,
                appendOnly: true,
                sortField: this.fromSortField,
                sortDir: this.fromSortDir
            });

            this.multiselects[1] = new Ext.ux.Multiselect({ // 定义一个
                legend: this.toLegend,
                delimiter: this.delimiter,
                allowDup: this.allowDup,
                width: this.msWidth,
                height: this.msHeight,
                displayField: this.displayField,
                valueField: this.valueField,
                store: this.toStore,
                isFormField: false,
                tbar: this.toTBar,
                sortField: this.toSortField,
                sortDir: this.toSortDir,
                sinClickCall: this.sinClickCall
            });
        }
        if (!Ext.isEmpty(callback) && Ext.isFunction(callback)) {
            callback();
        }
    },

    clearData:function(){
        if(this.fromStore){
            this.fromStore.removeAll();
        }
        if(this.toStore){
            this.toStore.removeAll();
        }
    },
    setValue: function (values) {
        var index;
        var selectionsArray = [];
        if (this.toMultiselect) {
            this.toMultiselect.view.clearSelections();
            this.hiddenField.dom.value = '';

            if (!values || (values == '')) {
                return;
            }

            if (!Ext.isArray(values)) {
                values = values.split(this.delimiter);
            }
            for (var i = 0; i < values.length; i++) {
                index = this.fromMultiselect.view.store
                    .indexOf(this.fromMultiselect.view.store.query(
                        this.fromMultiselect.valueField,
                        new RegExp(
                            '^' + values[i].trim() + '$',
                            "i")).itemAt(0));
                if (index > -1) {
                    selectionsArray.push(index);
                }
            }

            var records = [], record;
            if (selectionsArray.length > 0) {
                for (var i = 0; i < selectionsArray.length; i++) {
                    var record = this.fromMultiselect.view.store
                        .getAt(selectionsArray[i]);
                    records.push(record);
                }
                if (!this.allowDup)
                    selectionsArray = [];
                for (var i = 0; i < records.length; i++) {
                    record = records[i];
                    if (this.allowDup) {
                        var x = new Ext.data.Record();
                        record.id = x.id;
                        delete x;
                        this.toMultiselect.view.store.add(record);
                    } else {
                        this.fromMultiselect.view.store.remove(record);
                        this.toMultiselect.view.store.add(record);
                        selectionsArray
                            .push((this.toMultiselect.view.store
                                .getCount() - 1));
                    }
                }
            }
            this.toMultiselect.view.refresh();
            this.fromMultiselect.view.refresh();
            var si = this.toMultiselect.store.sortInfo;
            if (si) {
                this.toMultiselect.store.sort(si.field, si.direction);
            }
            this.toMultiselect.view.select(selectionsArray);
        }
    }
});

Ext.reg("myitemselector", Ext.ux.myItemSelector); // 注册组件
