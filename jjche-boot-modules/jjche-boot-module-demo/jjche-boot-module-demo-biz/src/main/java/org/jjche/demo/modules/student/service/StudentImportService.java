package org.jjche.demo.modules.student.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jjche.core.excel.ExcelImportTemplate;
import org.jjche.core.vo.ExcelImportRetVO;
import org.jjche.demo.modules.student.dto.StudentImportDTO;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 学生 导入
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-20
 */
public class StudentImportService extends ExcelImportTemplate {
    private MultipartFile multipartFile;
    private StudentService studentService;
    private Validator globalValidator;

    public StudentImportService(MultipartFile file, StudentService studentService, Validator globalValidator) {
        this.multipartFile = file;
        this.studentService = studentService;
        this.globalValidator = globalValidator;
    }

    @Override
    public Validator validator() {
        return this.globalValidator;
    }

    @Override
    public MultipartFile file() {
        return multipartFile;
    }

    @Override
    public Class clazz() {
        return StudentImportDTO.class;
    }

    @Override
    public List<ExcelImportRetVO> check(Object object, int rowNum) {
        List<ExcelImportRetVO> errList = CollUtil.newArrayList();
        StudentImportDTO orderImportDTO = (StudentImportDTO) object;
        String name = orderImportDTO.getName();
        //重复验证
        LambdaQueryWrapper<StudentDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(StudentDO::getName, name);
        if (studentService.count(queryWrapper) > 0) {
            String errorMsg = StrUtil.format("姓名：{} 已存在", name);
            ExcelImportRetVO excelImportRetVO = new ExcelImportRetVO();
            excelImportRetVO.setRowNum(rowNum);
            excelImportRetVO.setErrMsg(errorMsg);
            errList.add(excelImportRetVO);
        }
        return errList;
    }

    @Override
    public void importOperation(Set<?> importSet) {
        studentService.importStudent(importSet);
    }

    @Override
    public int startSheetIndex() {
        return 0;
    }
}
