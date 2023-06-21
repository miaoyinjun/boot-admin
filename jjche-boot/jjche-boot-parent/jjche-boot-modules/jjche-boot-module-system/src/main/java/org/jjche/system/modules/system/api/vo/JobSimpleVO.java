package org.jjche.system.modules.system.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 岗位精简信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobSimpleVO {

    @Schema(description = "岗位编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "岗位名称", required = true)
    private String name;

}
