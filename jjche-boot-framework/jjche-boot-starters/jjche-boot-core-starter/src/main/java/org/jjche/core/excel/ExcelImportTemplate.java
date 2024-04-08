package org.jjche.core.excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.jjche.common.util.StrUtil;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.common.wrapper.constant.HttpStatusConstant;
import org.jjche.common.wrapper.response.R;
import org.jjche.common.wrapper.response.RBadRequest;
import org.jjche.core.vo.ExcelImportRetVO;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 表格数据导入模板
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-20
 */
@Data
public abstract class ExcelImportTemplate {

    /**
     * <p>
     * 判断对象所有属性是否都为空，包括空串
     * </p>
     *
     * @param o            对象实体
     * @param excludeField 排除的字段数组
     * @return 结果
     */
    public static Boolean allFieldIsBlank(Object o, String[] excludeField) {
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                if (Arrays.stream(excludeField).anyMatch(item -> item.equals(field.getName()))) {
                    continue;
                }
                field.setAccessible(true);
                Object object = field.get(o);
                if (object instanceof CharSequence) {
                    if (!ObjectUtils.isEmpty(object)) {
                        return false;
                    }
                } else {
                    if (null != object) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            StaticLog.error(ThrowableUtil.getStackTrace(e));
        }
        return true;
    }

    public abstract Validator validator();

    /**
     * <p>
     * 文件
     * </p>
     *
     * @return 文件
     */
    public abstract MultipartFile file();

    /**
     * <p>
     * Class
     * </p>
     *
     * @return 结果
     */
    public abstract Class clazz();

    /**
     * <p>
     * 数据检查
     * </p>
     *
     * @param object 检查对象
     * @return 结果
     */
    public abstract List<ExcelImportRetVO> check(Object object, int rowNum);

    /**
     * <p>
     * 导入操作
     * </p>
     *
     * @param object   操作对象
     * @param userName 操作人
     */
    public abstract void importOperation(Set<?> object);

    /**
     * <p>
     * 开始索引
     * </p>
     *
     * @return 索引
     */
    public abstract int startSheetIndex();

    /**
     * <p>
     * 导入
     * </p>
     *
     * @return 结果
     */
    @SneakyThrows
    public R<List<ExcelImportRetVO>> importData() {
        R R = new RBadRequest();
        //定义一个存放错误信息的集合
        List<ExcelImportRetVO> errList = Lists.newArrayList();
        // 将待导入的文件抽取成集合
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(startSheetIndex());
        List<?> importList = ExcelImportUtil.importExcel(file().getInputStream(), clazz(), importParams);

        // 将非空行数据提炼出来
        Set importSet = new HashSet();
        String[] errorColumn = {"errorMsg", "rowNum"};
        for (int i = 0; i < importList.size(); i++) {
            Object item = importList.get(i);
            if (!allFieldIsBlank(item, errorColumn)) {
                //字段注解验证
                Set<? extends ConstraintViolation<?>> validate = validator().validate(item);
                if (validate.isEmpty()) {
                    //
                    List<ExcelImportRetVO> checkErrList = check(item, i + 1);
                    if (checkErrList.isEmpty() && !importSet.add(item)) {
                        importSet.remove(item);
                        importSet.add(item);
                    } else {
                        errList.addAll(checkErrList);
                    }
                } else {
                    //组装错误信息
                    StringBuilder msgStr = StrUtil.builder();
                    for (ConstraintViolation userDTOConstraintViolation : validate) {
                        String msg = userDTOConstraintViolation.getMessage();
                        msgStr.append(msg).append("，");
                    }
                    String errorMsg = StrUtil.removeSuffix(msgStr, "，");
                    ExcelImportRetVO excelImportRetVO = new ExcelImportRetVO();
                    excelImportRetVO.setRowNum(i + 1);
                    excelImportRetVO.setErrMsg(errorMsg);
                    errList.add(excelImportRetVO);
                    break;
                }
            }
        }
        importList.clear();
        // 如果全部为空行,则导入失败
        if (CollUtil.isEmpty(importSet) && CollUtil.isEmpty(errList)) {
            R.setCode(HttpStatusConstant.CODE_PARAMETER_ERROR);
            R.setMessage("导入模板不正确或没有数据要导入");
            return R;
        }
        // 如果有异常则返回前端告诉用户，如果一切正常则进行批量导入
        if (errList.isEmpty()) {
            //执行导入
            importOperation(importSet);
            return RBadRequest.ok();
        } else {
            ExcelImportRetVO errorVO = CollUtil.getFirst(errList);
            String message = StrUtil.format("第{}行，{}", errorVO.getRowNum(), errorVO.getErrMsg());
            R.setCode(HttpStatusConstant.CODE_PARAMETER_ERROR);
            R.setMessage(message);
            return R;
        }
    }

}
