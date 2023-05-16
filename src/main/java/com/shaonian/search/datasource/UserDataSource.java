package com.shaonian.search.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.search.model.dto.user.UserQueryRequest;
import com.shaonian.search.model.vo.UserVO;
import com.shaonian.search.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现
 *
 * @author 少年
 * 
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setPageSize(pageSize);
        userQueryRequest.setCurrent(pageNum);
        return userService.listUserVoByPage(userQueryRequest);
    }
}
