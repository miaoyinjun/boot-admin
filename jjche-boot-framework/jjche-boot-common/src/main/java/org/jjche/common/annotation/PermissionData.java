/*
 *  Copyright (c) 2019-2020, somewhere (somewhere0813@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jjche.common.annotation;

/**
 * <p>
 * 行数据权限过滤
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-05
 */

import java.lang.annotation.*;

/**
 * <p>
 * 数据权限注解
 * </p>
 *
 * @author miaoyj
 * @author miaoyj
 * @author miaoyj
 * @author miaoyj
 * @author miaoyj
 * @author miaoyj
 * @since 2021-11-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface PermissionData {

    /**
     * <p>
     * 部门id范围字段名称
     * </p>
     *
     * @return /
     */
    String deptIdInFieldName() default "";

    /**
     * <p>
     * 创建人相等字段名称
     * </p>
     *
     * @return /
     */
    String userIdEQFieldName() default "";

    /**
     * <p>
     * 是否处理字段修改权限
     * </p>
     *
     * @return /
     */
    boolean fieldUpdate() default false;

    /**
     * <p>
     * 是否处理字段返回权限
     * </p>
     *
     * @return /
     */
    boolean fieldReturn() default false;
}
