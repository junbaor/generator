<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#if table.pkCount gt 1>
<#assign classType = "${className}Id">
<#else>
<#assign classType = table.pkColumn.simpleJavaType>
</#if>
package ${basepackage}.service.impl;

import ${basicpackage}.dao.po.${module}.${className}Po;
import ${basepackage}.service.${className}Service;
import org.springframework.stereotype.Service;
import com.common.base.IBaseServiceImpl;

@Service("${classNameLower}Service")
public class ${className}ServiceImpl extends IBaseServiceImpl<${className}Po> implements ${className}Service{

}
