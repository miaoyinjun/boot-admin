package org.jjche.bpm.modules.task.vo.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.bpm.onstants.DictTypeConstants;
import org.jjche.common.annotation.Dict;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@ApiModel(value = "工作流 - 流程实例的 Response VO")
@Data
public class BpmProcessInstanceRespVO {

    @ApiModelProperty(value = "流程实例的编号", required = true, example = "1024")
    private String id;

    @ApiModelProperty(value = "流程名称", required = true, example = "芋道")
    private String name;

    @ApiModelProperty(value = "流程分类-参见 bpm_model_category 数据字典", required = true, example = "1")
    private String category;

    @ApiModelProperty(value = "流程实例的状态-参见 bpm_process_instance_status", required = true, example = "1")
    private Integer status;

    @ApiModelProperty(value = "流程实例的结果-参见 bpm_process_instance_result", required = true, example = "2")
    @Dict(DictTypeConstants.BPM_PROCESS_INSTANCE_RESULT)
    private Integer result;

    @ApiModelProperty(value = "提交时间", required = true)
    private Timestamp gmtCreate;

    @ApiModelProperty(value = "结束时间", required = true)
    private Timestamp endTime;

    @ApiModelProperty(value = "提交的表单值", required = true)
    private Map<String, Object> formVariables;

    @ApiModelProperty(value = "业务的唯一标识-例如说，请假申请的编号", example = "1")
    private String businessKey;

    /**
     * 发起流程的用户
     */
    private User startUser;

    /**
     * 流程定义
     */
    private ProcessDefinition processDefinition;

    @ApiModel(value = "用户信息")
    @Data
    public static class User {

        @ApiModelProperty(value = "用户编号", required = true, example = "1")
        private Long id;
        @ApiModelProperty(value = "用户昵称", required = true, example = "芋艿")
        private String nickName;

        @ApiModelProperty(value = "部门编号", required = true, example = "1")
        private Long deptId;
        @ApiModelProperty(value = "部门名称", required = true, example = "研发部")
        private String deptName;

    }

    @ApiModel(value = "流程定义信息")
    @Data
    public static class ProcessDefinition {

        @ApiModelProperty(value = "编号", required = true, example = "1024")
        private String id;

        @ApiModelProperty(value = "表单类型-参见 bpm_model_form_type 数据字典", example = "1")
        private Integer formType;
        @ApiModelProperty(value = "表单编号-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", example = "1024")
        private Long formId;
        @ApiModelProperty(value = "表单的配置-JSON 字符串。在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", required = true)
        private String formConf;
        @ApiModelProperty(value = "表单项的数组-JSON 字符串的数组。在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空", required = true)
        private List<String> formFields;
        @ApiModelProperty(value = "自定义表单的提交路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
                example = "/oa/leave/create")
        private String formCustomCreatePath;
        @ApiModelProperty(value = "自定义表单的查看路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空",
                example = "/oa/leave/view")
        private String formCustomViewPath;

        @ApiModelProperty(value = "BPMN XML", required = true)
        private String bpmnXml;

    }

}
