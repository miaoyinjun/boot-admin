package aorg.jjche.sba.actuator.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * <p>
 * 自定义健康检查
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class DbCountHealthIndicator implements HealthIndicator {
    /**
     * {@inheritDoc}
     */
    @Override
    public Health health() {
        try {
            long count = 1;
            if (count >= 0) {
                return Health.up().withDetail("count", count).build();
            } else {
                return Health.unknown().withDetail("count", count).build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
