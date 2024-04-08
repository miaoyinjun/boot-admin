package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.MediaType;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import io.swagger.annotations.Api;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;

/**
* <p>
* ${table.comment!} 控制器
* </p>
*
* @author ${author}
* @since ${date}
*/
@Api(tags = "${table.comment!}")
@ApiSupport(order = 1, author = "${author}")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping(value = "<#noparse>${server.api.url-prefix}</#noparse>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>", produces = MediaType.APPLICATION_JSON_VALUE)
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

}
</#if>