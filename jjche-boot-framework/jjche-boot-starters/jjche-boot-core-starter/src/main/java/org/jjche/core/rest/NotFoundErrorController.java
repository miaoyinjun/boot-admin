package org.jjche.core.rest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

/** 
 * <p>
 * 404页面
 * </p>
 *            
 * @author miaoyj
 * @since 2023-01-29
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class NotFoundErrorController implements ErrorController{

    @RequestMapping
    public Map<String, Object> handlerError() throws NoHandlerFoundException {
        throw new NoHandlerFoundException(null, null, null);
    }

}