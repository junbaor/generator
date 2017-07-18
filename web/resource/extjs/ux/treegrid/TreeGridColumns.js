/*!
 * Ext JS Library 3.1.0
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
(function() {    
    Ext.override(Ext.list.Column, {
        init : function() {            
            if(!this.type){
                this.type = "auto";
            }

            var st = Ext.data.SortTypes;
            // named sortTypes are supported, here we look them up
            if(typeof this.sortType == "string"){
                this.sortType = st[this.sortType];
            }

            // set default sortType for strings and dates
            if(!this.sortType){
                switch(this.type){
                    case "string":
                        this.sortType = st.asUCString;
                        break;
                    case "date":
                        this.sortType = st.asDate;
                        break;
                    default:
                        this.sortType = st.none;
                }
            }
        },
        getRenderer : function(col) {
        	if(!Ext.isEmpty(this.renderer)){
        		return this.renderer(col)
        	}else{
        		return this.defaultRenderer(col);
        	}
        },
        defaultRenderer : function(value){
            if(typeof value == "string" && value.length < 1){
                return "&#160;";
            }
            return value;
        }
     
    });

    Ext.tree.Column = Ext.extend(Ext.list.Column, {});
    
    Ext.tree.NumberColumn = Ext.extend(Ext.list.NumberColumn, {});
    Ext.tree.DateColumn = Ext.extend(Ext.list.DateColumn, {});
    Ext.tree.BooleanColumn = Ext.extend(Ext.list.BooleanColumn, {});

    Ext.reg('tgcolumn', Ext.tree.Column);
    Ext.reg('tgnumbercolumn', Ext.tree.NumberColumn);
    Ext.reg('tgdatecolumn', Ext.tree.DateColumn);
    Ext.reg('tgbooleancolumn', Ext.tree.BooleanColumn);
})();
