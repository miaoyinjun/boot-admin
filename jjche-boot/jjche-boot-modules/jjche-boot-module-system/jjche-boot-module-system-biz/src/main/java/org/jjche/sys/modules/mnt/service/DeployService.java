package org.jjche.sys.modules.mnt.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.exception.util.BusinessExceptionUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.core.util.FileUtil;
import org.jjche.core.util.SecurityUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.mnt.domain.AppDO;
import org.jjche.sys.modules.mnt.domain.DeployDO;
import org.jjche.sys.modules.mnt.domain.DeployHistoryDO;
import org.jjche.sys.modules.mnt.domain.ServerDeployDO;
import org.jjche.sys.modules.mnt.dto.AppDTO;
import org.jjche.sys.modules.mnt.dto.DeployDTO;
import org.jjche.sys.modules.mnt.dto.DeployQueryCriteriaDTO;
import org.jjche.sys.modules.mnt.dto.ServerDeployDTO;
import org.jjche.sys.modules.mnt.mapper.DeployMapper;
import org.jjche.sys.modules.mnt.mapstruct.DeployMapStruct;
import org.jjche.sys.modules.mnt.util.ExecuteShellUtil;
import org.jjche.sys.modules.mnt.util.ScpClientUtil;
import org.jjche.sys.modules.mnt.websocket.MsgType;
import org.jjche.sys.modules.mnt.websocket.SocketMsg;
import org.jjche.sys.modules.mnt.websocket.WebSocketServer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>DeployService class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Service
@RequiredArgsConstructor
public class DeployService extends MyServiceImpl<DeployMapper, DeployDO> {

    private final String FILE_SEPARATOR = "/";
    private final DeployMapStruct deployMapper;
    private final ServerDeployService serverDeployService;
    private final DeployHistoryService deployHistoryService;
    /**
     * 循环次数
     */
    private final Integer count = 30;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(DeployQueryCriteriaDTO criteria) {
        return MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(DeployQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<DeployDTO> list = deployMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<DeployDTO> queryAll(DeployQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return deployMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public DeployDTO findById(Long id) {
        return deployMapper.toVO(this.getById(id));
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DeployDO resources) {
        this.save(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DeployDO resources) {
        this.updateById(resources);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 部署文件到服务器
     *
     * @param fileSavePath 文件路径
     * @param appId        应用ID
     */
    public void deployApp(String fileSavePath, Long appId) {
        DeployDTO deploy = findById(appId);
        if (deploy == null) {
            sendMsg("部署信息不存在", MsgType.ERROR);
            throw BusinessExceptionUtil.exception(SysErrorCodeEnum.APP_DEPLOY_NOT_FOUND_ERROR);
        }
        AppDTO app = deploy.getApp();
        if (app == null) {
            sendMsg("包对应应用信息不存在", MsgType.ERROR);
            throw BusinessExceptionUtil.exception(SysErrorCodeEnum.APP_PACKAGE_NOT_FOUND_ERROR);
        }
        int port = app.getPort();
        //这个是服务器部署路径
        String uploadPath = app.getUploadPath();
        StringBuilder sb = new StringBuilder();
        String msg;
        Set<ServerDeployDTO> deploys = deploy.getDeploys();
        for (ServerDeployDTO deployDTO : deploys) {
            String ip = deployDTO.getIp();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(ip);
            //判断是否第一次部署
            boolean flag = checkFile(executeShellUtil, app);
            //第一步要确认服务器上有这个目录
            executeShellUtil.execute("mkdir -p " + app.getUploadPath());
            executeShellUtil.execute("mkdir -p " + app.getBackupPath());
            executeShellUtil.execute("mkdir -p " + app.getDeployPath());
            //上传文件
            msg = String.format("登陆到服务器:%s", ip);
            ScpClientUtil scpClientUtil = getScpClientUtil(ip);
            StaticLog.info(msg);
            sendMsg(msg, MsgType.INFO);
            msg = String.format("上传文件到服务器:%s<br>目录:%s下，请稍等...", ip, uploadPath);
            sendMsg(msg, MsgType.INFO);
            scpClientUtil.putFile(fileSavePath, uploadPath);
            if (flag) {
                sendMsg("停止原来应用", MsgType.INFO);
                //停止应用
                stopApp(port, executeShellUtil);
                sendMsg("备份原来应用", MsgType.INFO);
                //备份应用
                backupApp(executeShellUtil, ip, app.getDeployPath() + FILE_SEPARATOR, app.getName(), app.getBackupPath() + FILE_SEPARATOR, appId);
            }
            sendMsg("部署应用", MsgType.INFO);
            //部署文件,并启动应用
            String deployScript = app.getDeployScript();
            executeShellUtil.execute(deployScript);
            sleep(3);
            sendMsg("应用部署中，请耐心等待部署结果，或者稍后手动查看部署状态", MsgType.INFO);
            int i = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = checkIsRunningStatus(port, executeShellUtil);
                if (result) {
                    break;
                }
                // 休眠6秒
                sleep(6);
            }
            sb.append("服务器:").append(deployDTO.getName()).append("<br>应用:").append(app.getName());
            sendResultMsg(result, sb);
            executeShellUtil.close();
        }
    }

    private void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void backupApp(ExecuteShellUtil executeShellUtil, String ip, String fileSavePath, String appName, String backupPath, Long id) {
        String deployDate = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
        StringBuilder sb = new StringBuilder();
        backupPath += appName + FILE_SEPARATOR + deployDate + "\n";
        sb.append("mkdir -p ").append(backupPath);
        sb.append("mv -f ").append(fileSavePath);
        sb.append(appName).append(" ").append(backupPath);
        StaticLog.info("备份应用脚本:" + sb.toString());
        executeShellUtil.execute(sb.toString());
        //还原信息入库
        DeployHistoryDO deployHistory = new DeployHistoryDO();
        deployHistory.setAppName(appName);
        deployHistory.setDeployUser(SecurityUtil.getUsername());
        deployHistory.setIp(ip);
        deployHistory.setDeployId(id);
        deployHistoryService.create(deployHistory);
    }

    /**
     * 停App
     *
     * @param port             端口
     * @param executeShellUtil /
     */
    private void stopApp(int port, ExecuteShellUtil executeShellUtil) {
        //发送停止命令
        executeShellUtil.execute(String.format("lsof -i :%d|grep -v \"PID\"|awk '{print \"kill -9\",$2}'|sh", port));

    }

    /**
     * 指定端口程序是否在运行
     *
     * @param port             端口
     * @param executeShellUtil /
     * @return true 正在运行  false 已经停止
     */
    private boolean checkIsRunningStatus(int port, ExecuteShellUtil executeShellUtil) {
        String result = executeShellUtil.executeForResult(String.format("fuser -n tcp %d", port));
        return result.indexOf("/tcp:") > 0;
    }

    private void sendMsg(String msg, MsgType msgType) {
        try {
            WebSocketServer.sendInfo(new SocketMsg(msg, msgType), "deploy");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 查询部署状态
     *
     * @param resources /
     * @return /
     */
    public String serverStatus(DeployDO resources) {
        Set<ServerDeployDO> serverDeploys = resources.getDeploys();
        AppDO app = resources.getApp();
        for (ServerDeployDO serverDeploy : serverDeploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(serverDeploy.getIp());
            sb.append("服务器:").append(serverDeploy.getName()).append("<br>应用:").append(app.getName());
            boolean result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>正在运行");
                sendMsg(sb.toString(), MsgType.INFO);
            } else {
                sb.append("<br>已停止!");
                sendMsg(sb.toString(), MsgType.ERROR);
            }
            StaticLog.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    private boolean checkFile(ExecuteShellUtil executeShellUtil, AppDTO appDTO) {
        String result = executeShellUtil.executeForResult("find " + appDTO.getDeployPath() + " -name " + appDTO.getName());
        return result.indexOf(appDTO.getName()) > 0;
    }

    /**
     * 启动服务
     *
     * @param resources /
     * @return /
     */
    public String startServer(DeployDO resources) {
        Set<ServerDeployDO> deploys = resources.getDeploys();
        AppDO app = resources.getApp();
        for (ServerDeployDO deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(deploy.getIp());
            //为了防止重复启动，这里先停止应用
            stopApp(app.getPort(), executeShellUtil);
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            sendMsg("下发启动命令", MsgType.INFO);
            executeShellUtil.execute(app.getStartScript());
            sleep(3);
            sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看运行状态", MsgType.INFO);
            int i = 0;
            boolean result = false;
            // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
            while (i++ < count) {
                result = checkIsRunningStatus(app.getPort(), executeShellUtil);
                if (result) {
                    break;
                }
                // 休眠6秒
                sleep(6);
            }
            sendResultMsg(result, sb);
            StaticLog.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    /**
     * 停止服务
     *
     * @param resources /
     * @return /
     */
    public String stopServer(DeployDO resources) {
        Set<ServerDeployDO> deploys = resources.getDeploys();
        AppDO app = resources.getApp();
        for (ServerDeployDO deploy : deploys) {
            StringBuilder sb = new StringBuilder();
            ExecuteShellUtil executeShellUtil = getExecuteShellUtil(deploy.getIp());
            sb.append("服务器:").append(deploy.getName()).append("<br>应用:").append(app.getName());
            sendMsg("下发停止命令", MsgType.INFO);
            //停止应用
            stopApp(app.getPort(), executeShellUtil);
            sleep(1);
            boolean result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                sb.append("<br>关闭失败!");
                sendMsg(sb.toString(), MsgType.ERROR);
            } else {
                sb.append("<br>关闭成功!");
                sendMsg(sb.toString(), MsgType.INFO);
            }
            StaticLog.info(sb.toString());
            executeShellUtil.close();
        }
        return "执行完毕";
    }

    /**
     * 停止服务
     *
     * @param resources /
     * @return /
     */
    public String serverReduction(DeployHistoryDO resources) {
        Long deployId = resources.getDeployId();
        DeployDO deployInfo = this.getById(deployId);
        String deployDate = DateUtil.format(resources.getGmtCreate(), DatePattern.PURE_DATETIME_PATTERN);
        AppDO app = deployInfo.getApp();
        if (app == null) {
            sendMsg("应用信息不存在：" + resources.getAppName(), MsgType.ERROR);
            throw BusinessExceptionUtil.exception(SysErrorCodeEnum.APP_NOT_FOUND_ERROR, resources.getAppName());
        }
        String backupPath = app.getBackupPath() + FILE_SEPARATOR;
        backupPath += resources.getAppName() + FILE_SEPARATOR + deployDate;
        //这个是服务器部署路径
        String deployPath = app.getDeployPath();
        String ip = resources.getIp();
        ExecuteShellUtil executeShellUtil = getExecuteShellUtil(ip);
        String msg;

        msg = String.format("登陆到服务器:%s", ip);
        StaticLog.info(msg);
        sendMsg(msg, MsgType.INFO);
        sendMsg("停止原来应用", MsgType.INFO);
        //停止应用
        stopApp(app.getPort(), executeShellUtil);
        //删除原来应用
        sendMsg("删除应用", MsgType.INFO);
        executeShellUtil.execute("rm -rf " + deployPath + FILE_SEPARATOR + resources.getAppName());
        //还原应用
        sendMsg("还原应用", MsgType.INFO);
        executeShellUtil.execute("cp -r " + backupPath + "/. " + deployPath);
        sendMsg("启动应用", MsgType.INFO);
        executeShellUtil.execute(app.getStartScript());
        sendMsg("应用启动中，请耐心等待启动结果，或者稍后手动查看启动状态", MsgType.INFO);
        int i = 0;
        boolean result = false;
        // 由于启动应用需要时间，所以需要循环获取状态，如果超过30次，则认为是启动失败
        while (i++ < count) {
            result = checkIsRunningStatus(app.getPort(), executeShellUtil);
            if (result) {
                break;
            }
            // 休眠6秒
            sleep(6);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("服务器:").append(ip).append("<br>应用:").append(resources.getAppName());
        sendResultMsg(result, sb);
        executeShellUtil.close();
        return "";
    }

    private ExecuteShellUtil getExecuteShellUtil(String ip) {
        ServerDeployDTO serverDeployDTO = serverDeployService.findByIp(ip);
        if (serverDeployDTO == null) {
            sendMsg("IP对应服务器信息不存在：" + ip, MsgType.ERROR);
            throw BusinessExceptionUtil.exception(SysErrorCodeEnum.APP_IP_NOT_FOUND_ERROR, ip);
        }
        return new ExecuteShellUtil(ip, serverDeployDTO.getAccount(), serverDeployDTO.getPassword(), serverDeployDTO.getPort());
    }

    private ScpClientUtil getScpClientUtil(String ip) {
        ServerDeployDTO serverDeployDTO = serverDeployService.findByIp(ip);
        if (serverDeployDTO == null) {
            sendMsg("IP对应服务器信息不存在：" + ip, MsgType.ERROR);
            throw BusinessExceptionUtil.exception(SysErrorCodeEnum.APP_IP_NOT_FOUND_ERROR, ip);
        }
        return ScpClientUtil.getInstance(ip, serverDeployDTO.getPort(), serverDeployDTO.getAccount(), serverDeployDTO.getPassword());
    }

    private void sendResultMsg(boolean result, StringBuilder sb) {
        if (result) {
            sb.append("<br>启动成功!");
            sendMsg(sb.toString(), MsgType.INFO);
        } else {
            sb.append("<br>启动失败!");
            sendMsg(sb.toString(), MsgType.ERROR);
        }
    }

    /**
     * 导出数据
     *
     * @param queryAll /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<DeployDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeployDTO deployDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("应用名称", deployDto.getApp().getName());
            map.put("服务器", deployDto.getServers());
            map.put("部署日期", deployDto.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
