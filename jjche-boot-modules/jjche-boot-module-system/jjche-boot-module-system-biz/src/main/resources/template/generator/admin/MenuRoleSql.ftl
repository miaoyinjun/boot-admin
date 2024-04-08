BEGIN;

-- 添加菜单-列表
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
		VALUES
			((SELECT id FROM (SELECT id FROM sys_menu WHERE `title` = '系统管理') AS temp), 3, 1, '${apiAlias}管理', '${className}Menu', 'system/${tableName}/index', 999, 'tree', '${className}', b'0', b'0', b'0', '${tableName}:list', 'admin', 'admin', now(), now());

-- 添加菜单-增，删，改
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = '${className}Menu') AS temp), 0, 2, '${apiAlias}新增', '${tableName}_add', '', 5, '', '', b'0', b'0', b'0', '${changeClassName}:add', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = '${className}Menu') AS temp), 0, 2, '${apiAlias}编辑', '${tableName}_edit', '', 5, '', '', b'0', b'0', b'0', '${changeClassName}:edit', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = '${className}Menu') AS temp), 0, 2, '${apiAlias}删除', '${tableName}_del', '', 5, '', '', b'0', b'0', b'0', '${changeClassName}:del', 'admin', 'admin', now(), now());

-- 添加菜单-角色关联
INSERT INTO `sys_roles_menus`
	VALUES
		((SELECT id FROM sys_menu WHERE `permission` = '${tableName}:list'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = '${tableName}:add'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = '${tableName}:edit'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = '${tableName}:del'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN'));

-- 删除菜单--回滚

-- DELETE FROM `sys_roles_menus` WHERE
-- 		(menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${tableName}:list')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${tableName}:add')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${tableName}:edit')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${tableName}:del')
-- 		)
-- 		AND
-- 		role_id = (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')
-- ;

-- DELETE FROM `sys_menu` WHERE
--     title = '${apiAlias}管理' || title = '${apiAlias}新增' || title = '${apiAlias}编辑' || title = '${apiAlias}删除';

COMMIT;