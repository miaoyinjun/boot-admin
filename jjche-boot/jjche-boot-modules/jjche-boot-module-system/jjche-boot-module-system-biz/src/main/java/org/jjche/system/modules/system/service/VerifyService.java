package org.jjche.system.modules.system.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.system.modules.tool.api.vo.EmailVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * <p>VerifyService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-26
 */
@Service
@RequiredArgsConstructor
public class VerifyService {

    private final RedisService redisService;
    @Value("${jjche.tool.mail.code.expiration}")
    private Long expiration;

    /**
     * {@inheritDoc}
     *
     * @param email a {@link java.lang.String} object.
     * @param key   a {@link java.lang.String} object.
     * @return a {@link EmailVO} object.
     */
    @Transactional(rollbackFor = Exception.class)
    public EmailVO sendEmail(String email, String key) {
        EmailVO emailVo;
        String content;
        String redisKey = key + email;
        // 如果不存在有效的验证码，就创建一个新的
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/email.ftl");
        String oldCode = redisService.stringGetString(redisKey);
        if (oldCode == null) {
            String code = RandomUtil.randomNumbers(6);
            // 存入缓存
            redisService.stringSetString(redisKey, code, expiration);
            content = template.render(Dict.create().set("code", code));
            emailVo = new EmailVO(Collections.singletonList(email), "ADMIN后台管理系统", content);
            // 存在就再次发送原来的验证码
        } else {
            content = template.render(Dict.create().set("code", oldCode));
            emailVo = new EmailVO(Collections.singletonList(email), "ADMIN后台管理系统", content);
        }
        return emailVo;
    }

    /**
     * {@inheritDoc}
     *
     * @param key  a {@link java.lang.String} object.
     * @param code a {@link java.lang.String} object.
     */
    public void validated(String key, String code) {
        String value = redisService.stringGetString(key);
        Assert.isFalse(value == null || !value.toString().equals(code), "无效验证码");
        redisService.delete(key);
    }
}
