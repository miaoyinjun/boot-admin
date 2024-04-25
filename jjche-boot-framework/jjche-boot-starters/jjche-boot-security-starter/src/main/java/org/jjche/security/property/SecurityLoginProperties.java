package org.jjche.security.property;

import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.Data;
import org.jjche.common.util.StrUtil;

import java.awt.*;
import java.util.Objects;

/**
 * 配置文件读取
 *
 * @author liaojinlong
 * @version 1.0.8-SNAPSHOT
 * @since loginCode.length0loginCode.length0/6/10 17:loginCode.length6
 */
@Data
public class SecurityLoginProperties {

    /**
     * 账号单用户 登录
     */
    private boolean single = false;

    private SecurityLoginCodeProperties loginCode;
    /**
     * 用户登录信息缓存
     */
    private boolean cacheEnable;

    /**
     * <p>isSingle.</p>
     *
     * @return a boolean.
     */
    public boolean isSingle() {
        return single;
    }

    /**
     * <p>Setter for the field <code>single</code>.</p>
     *
     * @param single a boolean.
     */
    public void setSingle(boolean single) {
        this.single = single;
    }

    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public Captcha getCaptcha() {
        if (Objects.isNull(loginCode)) {
            loginCode = new SecurityLoginCodeProperties();
            if (Objects.isNull(loginCode.getCodeType())) {
                loginCode.setCodeType(LoginCodeEnum.arithmetic);
            }
        }
        return switchCaptcha(loginCode);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param loginCode 验证码配置信息
     * @return /
     */
    private Captcha switchCaptcha(SecurityLoginCodeProperties loginCode) {
        Captcha captcha;
        synchronized (this) {
            switch (loginCode.getCodeType()) {
                case arithmetic:
                    // 算术类型 https://gitee.com/whvse/EasyCaptcha
                    captcha = new ArithmeticCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    // 几位数运算，默认是两位
                    captcha.setLen(loginCode.getLength());
                    break;
                case chinese:
                    captcha = new ChineseCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case chinese_gif:
                    captcha = new ChineseGifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case gif:
                    captcha = new GifCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setLen(loginCode.getLength());
                    break;
                case spec:
                    captcha = new SpecCaptcha(loginCode.getWidth(), loginCode.getHeight());
                    captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
                    captcha.setLen(loginCode.getLength());
                    break;
                default:
                    throw new IllegalArgumentException("验证码配置信息错误！正确配置查看 LoginCodeEnum ");
            }
        }
        if (StrUtil.isNotBlank(loginCode.getFontName())) {
            captcha.setFont(new Font(loginCode.getFontName(), Font.PLAIN, loginCode.getFontSize()));
        }
        return captcha;
    }
}
