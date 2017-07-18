/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-5-26
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */


function HttpRequest(o) {
    o.failure = function(response) {
        var errorWindow = new Ext.Window({
                    title:"系统异常！",
                    width:200,
                    height:200,
                    maximizable:true,
                    autoScroll:true,
                    html:response.responseText
                });
        errorWindow.show();
        errorWindow.maximize();
    };
    return Ext.Ajax.request(o);
}

function makeParams(records, columns) {
    var ids = new Array();
    for (var i = 0, len = records.length; i < len; i++) {
        var record = records[i];
        var params = {};
        for (var j = 0; j < columns.length; j++) {
            var key = columns[j];
            var value = record.get(key);
            params[key] = value;
        }
        ids[ids.length] = params;
    }
    return {
        params : Ext.util.JSON.encode(ids)
    }
}