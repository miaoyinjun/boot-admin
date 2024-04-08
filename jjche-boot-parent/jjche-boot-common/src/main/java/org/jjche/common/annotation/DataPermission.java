///*
// *  Copyright (c) 2019-2020, somewhere (somewhere0813@gmail.com).
// *  <p>
// *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *  <p>
// * https://www.gnu.org/licenses/lgpl.html
// *  <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.boot.admin.common.annotation;
//
//import com.boot.admin.common.pojo.DataScope;
//
//import java.lang.annotation.*;
//
///**
// * <p>
// * 行数据权限过滤
// * </p>
// *
// * @author miaoyj
// * @version 1.0.10-SNAPSHOT
// * @since 2020-11-05
// */
//@Target({ElementType.METHOD, ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Deprecated
//public @interface DataPermission {
//    /**
//     * <p>
//     * 部门id范围字段名称
//     * </p>
//     *
//     * @return /
//     */
//    String deptIdInFieldName() default DataScope.F_SQL_SCOPE_NAME;
//
//    /**
//     * <p>
//     * 创建人id相等字段名称
//     * </p>
//     *
//     * @return /
//     */
//    String userIdEQFieldName() default DataScope.F_SQL_CREATOR_NAME;
//}
