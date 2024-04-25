package ${packageApi}.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
* <p>
* ${apiAlias} 排序枚举
* </p>
*
* @author ${author}
* @since ${date}
*/
@Getter
@AllArgsConstructor
public enum ${className}SortEnum{

    /**
    * 主键
    */
    ID_DESC("id DESC", "id倒序"),
    ID_ASC("id ASC", "id正序"),
    ;

    /**
    * Constant <code>MAPPINGS</code>
    */
    private static final Map<String, ${className}SortEnum> MAPPINGS;

    static {
        Map<String, ${className}SortEnum> temp = new HashMap<String, ${className}SortEnum>();
        for (${className}SortEnum courseEnum : values()) {
        temp.put(courseEnum.value, courseEnum);
        }
        MAPPINGS = Collections.unmodifiableMap(temp);
    }

    @JsonValue
    private final String value;
    private final String desc;

    /**
    * <p>
    * 根据index获取枚举
    * </p>
    *
    * @param index a String.
    * @return 枚举
    * @author ${author}
    * @since ${date}
    */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ${className}SortEnum resolve(String index) {
    return MAPPINGS.get(index);
    }
}