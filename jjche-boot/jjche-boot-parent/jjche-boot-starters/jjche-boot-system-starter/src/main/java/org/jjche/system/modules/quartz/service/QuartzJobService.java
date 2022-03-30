package org.jjche.system.modules.quartz.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.StrUtil;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.quartz.domain.QuartzJobDO;
import org.jjche.system.modules.quartz.domain.QuartzLogDO;
import org.jjche.system.modules.quartz.dto.JobQueryCriteriaDTO;
import org.jjche.system.modules.quartz.mapper.QuartzJobMapper;
import org.jjche.system.modules.quartz.mapper.QuartzLogMapper;
import org.jjche.system.modules.quartz.utils.QuartzManage;
import org.quartz.CronExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>QuartzJobService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Service
@RequiredArgsConstructor
public class QuartzJobService extends MyServiceImpl<QuartzJobMapper, QuartzJobDO> {

    private final QuartzLogMapper quartzLogMapper;
    private final QuartzManage quartzManage;
    private final RedisService redisService;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(JobQueryCriteriaDTO criteria) {
        return MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    public MyPage<QuartzJobDO> queryAll(JobQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.page(page, queryWrapper);
    }

    /**
     * 分页查询日志
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    public MyPage<QuartzLogDO> queryAllLog(JobQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.quartzLogMapper.pageQuery(page, queryWrapper);
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    public List<QuartzJobDO> queryAll(JobQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.baseMapper.queryAll(queryWrapper);
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    public List<QuartzLogDO> queryAllLog(JobQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.quartzLogMapper.queryAll(queryWrapper);
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    public QuartzJobDO findById(Long id) {
        QuartzJobDO quartzJob = this.getById(id);
        ValidationUtil.isNull(quartzJob.getId(), "QuartzJobDO", "id", id);
        return quartzJob;
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(QuartzJobDO resources) {
        Boolean isError = !CronExpression.isValidExpression(resources.getCronExpression());
        Assert.isFalse(isError, "cron表达式格式错误");
        this.save(resources);
        quartzManage.addJob(resources);
    }


    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(QuartzJobDO resources) {
        Boolean isError = !CronExpression.isValidExpression(resources.getCronExpression());
        Assert.isFalse(isError, "cron表达式格式错误");
        if (StrUtil.isNotBlank(resources.getSubTask())) {
            List<String> tasks = Arrays.asList(resources.getSubTask().split("[,，]"));
            Assert.isFalse(tasks.contains(resources.getId().toString()), "子任务中不能添加当前任务ID");
        }
        this.updateById(resources);
        quartzManage.updateJobCron(resources);
    }

    /**
     * 更改定时任务状态
     *
     * @param quartzJob /
     */
    public void updateIsPause(QuartzJobDO quartzJob) {
        if (quartzJob.getIsPause()) {
            quartzManage.resumeJob(quartzJob);
            quartzJob.setIsPause(false);
        } else {
            quartzManage.pauseJob(quartzJob);
            quartzJob.setIsPause(true);
        }
        this.updateById(quartzJob);
    }

    /**
     * 立即执行定时任务
     *
     * @param quartzJob /
     */
    public void execution(QuartzJobDO quartzJob) {
        quartzManage.runJobNow(quartzJob);
    }

    /**
     * 删除任务
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            QuartzJobDO quartzJob = findById(id);
            quartzManage.deleteJob(quartzJob);
            this.removeByIdWithFill(quartzJob);
        }
    }

    /**
     * <p>listIsPauseIsFalse.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<QuartzJobDO> listIsPauseIsFalse() {
        LambdaQueryWrapper<QuartzJobDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuartzJobDO::getIsPause, false);
        return this.list(queryWrapper);
    }

    /**
     * 执行子任务
     *
     * @param tasks /
     * @throws java.lang.InterruptedException if any.
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void executionSubJob(String[] tasks) throws InterruptedException {
        for (String id : tasks) {
            if (cn.hutool.core.util.StrUtil.isBlank(id)) {
                // 如果是手动清除子任务id，会出现id为空字符串的问题
                continue;
            }
            QuartzJobDO quartzJob = findById(Long.parseLong(id));
            // 执行任务
            String uuid = IdUtil.simpleUUID();
            quartzJob.setUuid(uuid);
            // 执行任务
            execution(quartzJob);
            // 获取执行状态，如果执行失败则停止后面的子任务执行
            Boolean result = redisService.objectGetObject(uuid, Boolean.class);
            while (result == null) {
                // 休眠5秒，再次获取子任务执行情况
                Thread.sleep(5000);
                result = redisService.objectGetObject(uuid, Boolean.class);
            }
            if (!result) {
                redisService.delete(uuid);
                break;
            }
        }
    }

    /**
     * 导出定时任务
     *
     * @param quartzJobs 待导出的数据
     * @param response   /
     * @throws java.io.IOException if any.
     */
    public void download(List<QuartzJobDO> quartzJobs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QuartzJobDO quartzJob : quartzJobs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzJob.getJobName());
            map.put("Bean名称", quartzJob.getBeanName());
            map.put("执行方法", quartzJob.getMethodName());
            map.put("参数", quartzJob.getParams());
            map.put("表达式", quartzJob.getCronExpression());
            map.put("状态", quartzJob.getIsPause() ? "暂停中" : "运行中");
            map.put("描述", quartzJob.getDescription());
            map.put("创建日期", quartzJob.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 导出定时任务日志
     *
     * @param queryAllLog 待导出的数据
     * @param response    /
     * @throws java.io.IOException if any.
     */
    public void downloadLog(List<QuartzLogDO> queryAllLog, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QuartzLogDO quartzLog : queryAllLog) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("任务名称", quartzLog.getJobName());
            map.put("Bean名称", quartzLog.getBeanName());
            map.put("执行方法", quartzLog.getMethodName());
            map.put("参数", quartzLog.getParams());
            map.put("表达式", quartzLog.getCronExpression());
            map.put("异常详情", quartzLog.getExceptionDetail());
            map.put("耗时/毫秒", quartzLog.getTime());
            map.put("状态", quartzLog.getIsSuccess() ? "成功" : "失败");
            map.put("创建日期", quartzLog.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
