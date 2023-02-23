package org.jjche.bpm.modules.bpm.controller.admin.definition.vo.group;
import com.sun.istack.internal.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 用户组更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmUserGroupUpdateReqVO extends BpmUserGroupBaseVO {

    @Schema(description = "编号", required = true, example = "1024")
    @NotNull(message = "编号不能为空")
    private Long id;

}
