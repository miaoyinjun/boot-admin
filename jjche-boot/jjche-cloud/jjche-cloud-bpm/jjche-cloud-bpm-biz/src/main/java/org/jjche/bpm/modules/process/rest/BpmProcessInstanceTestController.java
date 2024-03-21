package org.jjche.bpm.modules.process.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.core.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试：工作流：流程实例")
@RestController
@RequestMapping("/process-instance/test")
@RequiredArgsConstructor
public class BpmProcessInstanceTestController extends BaseController {


}
