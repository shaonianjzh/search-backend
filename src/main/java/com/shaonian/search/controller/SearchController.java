package com.shaonian.search.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.search.common.BaseResponse;
import com.shaonian.search.common.ResultUtils;
import com.shaonian.search.model.dto.post.PostQueryRequest;
import com.shaonian.search.model.dto.search.SearchRequest;
import com.shaonian.search.model.dto.user.UserQueryRequest;
import com.shaonian.search.model.entity.Picture;
import com.shaonian.search.model.vo.PostVO;
import com.shaonian.search.model.vo.SearchVO;
import com.shaonian.search.model.vo.UserVO;
import com.shaonian.search.service.PictureService;
import com.shaonian.search.service.PostService;
import com.shaonian.search.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * 聚合搜索
 * @author 少年
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request){

        String searchText = searchRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 20);

        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        Page<UserVO> userVOPage = userService.listUserVoByPage(userQueryRequest);

        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        Page<PostVO> postVOPage = postService.listPostVoPage(postQueryRequest, request);

        SearchVO searchVO = new SearchVO();
        searchVO.setPostList(postVOPage.getRecords());
        searchVO.setUserList(userVOPage.getRecords());
        searchVO.setPictureList(picturePage.getRecords());
        return ResultUtils.success(searchVO);
    }

}
