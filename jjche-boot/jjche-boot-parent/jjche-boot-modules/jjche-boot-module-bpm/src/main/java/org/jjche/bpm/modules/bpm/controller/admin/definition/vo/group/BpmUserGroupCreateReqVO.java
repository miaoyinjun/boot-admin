package org.jjche.bpm.modules.bpm.controller.admin.definition.vo.group;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 用户组创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmUserGroupCreateReqVO extends BpmUserGroupBaseVO {

}
