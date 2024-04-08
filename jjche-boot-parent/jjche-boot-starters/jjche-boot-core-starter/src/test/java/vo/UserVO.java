package vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户出参
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
@NoArgsConstructor
public class UserVO implements Serializable {
    Integer age;
    private String name;
    private Long goodId;
    private Date gmtCreate;
}
