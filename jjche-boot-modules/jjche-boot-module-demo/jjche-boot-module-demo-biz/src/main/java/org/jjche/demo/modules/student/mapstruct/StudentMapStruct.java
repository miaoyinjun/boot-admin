package org.jjche.demo.modules.student.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jjche.common.param.MyPage;
import org.jjche.core.base.BaseMapStruct;
import org.jjche.demo.modules.student.api.vo.StudentApiVO;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.dto.StudentDTO;
import org.jjche.demo.modules.student.dto.StudentImportDTO;
import org.jjche.demo.modules.student.vo.StudentVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 学生 转换类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapStruct extends BaseMapStruct<StudentDO, StudentDTO, StudentVO> {
    /**
     * <p>
     * 转换excel
     * </p>
     *
     * @param dtoList /
     * @return /
     */
    List<StudentDO> toDO(Set<StudentImportDTO> dtoList);

    /**
     * <p>
     * DO分页转VO分页
     * </p>
     *
     * @param pageDo 分页DO
     * @return VO分页
     */
    MyPage<StudentApiVO> toApiVO(Page<StudentDO> pageDo);
}
