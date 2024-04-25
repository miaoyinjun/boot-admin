package aorg.jjche.sba.conf;


import org.jjche.common.yml.CoreEnvironmentPostProcessor;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class SbaEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for SbaEnvironmentPostProcessor</p>
     */
    public SbaEnvironmentPostProcessor() {
        super.setYmlName("sba.yml");
    }
}
