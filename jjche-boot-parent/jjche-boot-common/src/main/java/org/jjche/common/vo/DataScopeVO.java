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

package org.jjche.common.vo;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * <p>
 * 数据权限查询参数
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-05
 */
@Data
public class DataScopeVO implements Serializable {

    /**
     * Constant <code>F_SQL_SCOPE_NAME="dept_id"</code>
     */
    public static final String F_SQL_SCOPE_NAME = "dept_id";
    /**
     * Constant <code>F_SQL_CREATOR_NAME="creator_user_id"</code>
     */
    public static final String F_SQL_CREATOR_NAME = "created_by";
    private static final long serialVersionUID = 1L;
    /**
     * 具体的数据范围
     */
    private Set<Long> deptIds = CollUtil.newHashSet();
    /**
     * 全部数据
     */
    private boolean isAll;
    /**
     * 本人数据
     */
    private boolean isSelf;

    private Long userId;

    private String userName;

}
