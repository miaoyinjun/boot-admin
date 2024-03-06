package org.jjche.tool.modules.tool.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import lombok.RequiredArgsConstructor;
import org.jjche.common.util.RsaUtils;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.security.property.SecurityProperties;
import org.jjche.tool.modules.tool.api.vo.EmailVO;
import org.jjche.tool.modules.tool.constant.ToolCacheKey;
import org.jjche.tool.modules.tool.domain.EmailConfigDO;
import org.jjche.tool.modules.tool.mapper.EmailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>EmailService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-26
 */
@Service
@RequiredArgsConstructor
public class EmailService extends MyServiceImpl<EmailMapper, EmailConfigDO> {

    private final SecurityProperties securityProperties;

    /**
     * 更新邮件配置
     *
     * @param emailConfig 邮箱配置
     * @param old         /
     * @return /
     * @throws java.lang.Exception if any.
     */
    @CacheUpdate(name = ToolCacheKey.EMAIL, value = "#result")
    @Transactional(rollbackFor = Exception.class)
    public EmailConfigDO config(EmailConfigDO emailConfig, EmailConfigDO old) throws Exception {
        emailConfig.setId(1L);
        if (!emailConfig.getPass().equals(old.getPass())) {
            // 非对称加密
            String publicKey = securityProperties.getRsa().getPublicKey();
            emailConfig.setPass(RsaUtils.encryptBypPublicKey(publicKey, emailConfig.getPass()));
        }
        this.saveOrUpdate(emailConfig);
        return emailConfig;
    }

    /**
     * 查询配置
     *
     * @return EmailConfigDO 邮件配置
     */
    @Cached(name = ToolCacheKey.EMAIL)
    public EmailConfigDO find() {
        return this.getById(1L);
    }

    /**
     * 发送邮件
     *
     * @param emailVo     邮件发送的内容
     * @param emailConfig 邮件配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void send(EmailVO emailVo, EmailConfigDO emailConfig) {
        Assert.notNull(emailConfig.getId(), "请先配置，再操作");
        // 封装
        MailAccount account = new MailAccount();
        // 设置用户
        String user = emailConfig.getFromUser().split("@")[0];
        account.setUser(user);
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        try {
            String privateKey = securityProperties.getRsa().getPrivateKey();
            // 非对称解密
            account.setPass(RsaUtils.decryptByPrivateKey(privateKey, emailConfig.getPass()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        account.setFrom(emailConfig.getUser() + "<" + emailConfig.getFromUser() + ">");
        // ssl方式发送
        account.setSslEnable(true);
        // 使用STARTTLS安全连接
        account.setStarttlsEnable(true);
        String content = emailVo.getContent();
        // 发送
        try {
            int size = emailVo.getTos().size();
            Mail.create(account)
                    .setTos(emailVo.getTos().toArray(new String[size]))
                    .setTitle(emailVo.getSubject())
                    .setContent(content)
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
