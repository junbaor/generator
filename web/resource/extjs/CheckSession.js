/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-4-29
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */

//Ext.Ajax.on('requestcomplete', checkUserSessionStatus, this);
//function checkUserSessionStatus(conn, response, options) {
//	var t = response.responseText;
//	if(t.charAt(0)=="{"){
//       if( Ext.decode(t).xlsFileFileName ){
//      		return;
//       }
//	}else{
//	    if (typeof response.getResponseHeader("sessionstatus") != 'undefined') {
//	        Ext.MessageBox.alert('提示', '由于长时间未操作会话已超时或已在他地登录!', function() {
//	            if (parent != null) {
//	                top.location.href = _contextPath_;
//	            } else
//	                window.location.href = _contextPath_;
//	        });
//	    }
//	}
//}
