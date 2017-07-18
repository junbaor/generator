<#include "/java_copyright.include">
<#assign className = table.className>
<#assign classNameLower = className?uncap_first>
<#if table.pkCount gt 1>
<#assign classType = "${className}Id">
<#else>
<#assign classType = table.pkColumn.simpleJavaType>
</#if>
package ${basepackage}.service;
import ${basicpackage}.dao.po.${module}.${className}Po;
import com.common.base.IBaseService;

public interface ${className}Service extends IBaseService<${className}Po>{


}