package org.jjche.sys.modules.user.service;

import lombok.RequiredArgsConstructor;
import org.jjche.sys.modules.user.mapstruct.UserExtMapStruct;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.sys.modules.system.service.UserService;
import org.jjche.sys.modules.user.api.vo.UserExtVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户扩展服务
 * </p>
 *
 * @author miaoyj
 * @since 2024-03-08
 */
@Service
@RequiredArgsConstructor
public class UserExtService {
    private final UserService userService;
    private final UserExtMapStruct userExtMapStruct;

    /**
     * 分页查询
     *
     * @param page     分页参数
     * @return /
     */
    public MyPage<UserExtVO> page(PageParam page) {
        MyPage myPage = userService.page(page);
        List<UserExtVO> list = userExtMapStruct.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }
}
