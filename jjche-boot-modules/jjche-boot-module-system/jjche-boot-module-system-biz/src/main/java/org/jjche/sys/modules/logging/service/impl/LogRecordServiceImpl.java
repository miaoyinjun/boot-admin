package org.jjche.sys.modules.logging.service.impl;//package org.jjche.log.modules.logging.service.impl;
//
//import cn.hutool.core.collection.CollUtil;
//import org.jjche.common.dto.LogRecordDTO;
//import org.jjche.log.biz.service.ILogRecordService;
//import org.jjche.log.modules.logging.domain.LogDO;
//import org.jjche.log.modules.logging.mapstruct.LogRecordMapStruct;
//import org.jjche.log.modules.logging.service.LogService;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * <p>LogRecordServiceImpl class.</p>
// *
// * @author muzhantong
// * create on 2020/4/29 4:34 下午
// * @version 1.0.0-SNAPSHOT
// */
//@Component
//public class LogRecordServiceImpl implements ILogRecordService {
//
//    @Resource
//    private LogService logService;
//    @Resource
//    private LogRecordMapStruct logRecordMapper;
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void record(LogRecordDTO logRecord) {
//        LogDO log = logRecordMapper.toLog(logRecord);
//        logService.saveLog(log);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<LogRecordDTO> queryLog(String bizKey) {
//        return CollUtil.newArrayList();
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public List<LogRecordDTO> queryLogByBizNo(String bizNo) {
//        return CollUtil.newArrayList();
//    }
//}
