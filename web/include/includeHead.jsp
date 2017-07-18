 <%@ page contentType="text/html;charset=utf-8" language="java" %>
 <% String _contextPath_ = request.getContextPath().equals("/") ? "" : request.getContextPath();%>
<html>
<head>
<!-- todo 控制页面强制更新
<meta http-equiv="expires" content="">
-->
<!-- 使用客户端当前IE的最高版本渲染机制,禁止使用兼容模式(IE9登录异常) 
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
-->
<link rel="shortcut icon" href="<%=_contextPath_%>/resource/image/g4.ico" />  
<link rel="stylesheet" type="text/css" href="<%=_contextPath_%>/resource/theme/default/resources/css/ext-all.css"/> 
<link href="<%=_contextPath_%>/resource/myux/datatimefield/css/Spinner.css" rel="stylesheet" type="text/css"/>
<link href="<%=_contextPath_%>/resource/css/layout-browser.css" rel="stylesheet" type="text/css"/>
<link href="<%=_contextPath_%>/resource/extjs/ux/css/LockingGridView.css" rel="stylesheet" type="text/css"/>
<link href="<%=_contextPath_%>/resource/extjs/ux/fileuploadfield/css/fileuploadfield.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" href="<%=_contextPath_%>/resource/css/g4studio.css"/>
<link rel="stylesheet" type="text/css" href="<%=_contextPath_%>/resource/css/ext_css_patch.css" />
<link rel="stylesheet" type="text/css" href="<%=_contextPath_%>/resource/css/ext_icon.css" />
<link href="<%=_contextPath_%>/resource/extjs/plugins/Ext.ux.form.CheckboxCombo.min.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript" src="<%=_contextPath_%>/resource/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=_contextPath_%>/resource/extjs/ext-all.js"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/ux/LockingGridView.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=_contextPath_%>/resource/commonjs/ext-lang-zh_CN.js"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/PageSizePlugin.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/ux/ProgressBarPager.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/ux/RowExpander.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/ux/fileuploadfield/FileUploadField.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/myux/datatimefield/DateTimeField.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/myux/datatimefield/SpinnerField.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/myux/datatimefield/Spinner.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
  var webContext = '<%=_contextPath_%>';
  var _contextPath_ = "<%=_contextPath_%>";
    Ext.BLANK_IMAGE_URL = _contextPath_ + '/resource/extjs/resources/images/default/s.gif';
    Ext.QuickTips.init();
      var isPortal = <%=request.getParameter("isPortal")%>;
</script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/plugins/Ext.ux.AllowBlank.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/plugins/UnitField.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/plugins/DecimalField.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/HttpRequest.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/CheckSession.js" type="text/javascript"></script>

<script language="javascript" src="<%=_contextPath_%>/resource/extjs/FunctionGrid-debug.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/FunctionCombo.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/FunctionRadioGroup.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/FunctionCombo.js" type="text/javascript"></script>


<script language="javascript" src="<%=_contextPath_%>/resource/extjs/ux/MultiSelect.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/ux/ItemSelector.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/myItemSelector.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/FormWindow-debug.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/lovcombo.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/FunctionEditorGrid-debug.js" type="text/javascript"></script>

<script language="javascript" src="<%=_contextPath_%>/resource/extjs/plugins/Ext.ux.form.CheckboxCombo.min.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/ux/CheckColumn.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/Vtype.js" type="text/javascript"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/MultiComboBox.js"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/validate.js"></script>
<script language="javascript" src="<%=_contextPath_%>/resource/extjs/IFrameComponent.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=_contextPath_%>/resource/commonjs/g4studio.js"></script>

