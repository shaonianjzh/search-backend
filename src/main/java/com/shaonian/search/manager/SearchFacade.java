package com.shaonian.search.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.search.datasource.*;
import com.shaonian.search.model.dto.post.PostQueryRequest;
import com.shaonian.search.model.dto.search.SearchRequest;
import com.shaonian.search.model.dto.user.UserQueryRequest;
import com.shaonian.search.model.entity.Picture;
import com.shaonian.search.model.enums.SearchTypeEnum;
import com.shaonian.search.model.vo.PostVO;
import com.shaonian.search.model.vo.SearchVO;
import com.shaonian.search.model.vo.UserVO;
import com.shaonian.search.model.vo.VideoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author 少年
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    @Resource
    private VideoDataSource videoDataSource;


    public SearchVO searchAll(SearchRequest searchRequest, HttpServletRequest request){
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
//        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        String searchText = searchRequest.getSearchText();

        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();



        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(sra, true);
        if (searchTypeEnum == null) {
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                return pictureDataSource.doSearch(searchText, 1, 20);
            });
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                return userDataSource.doSearch(searchText,current,pageSize);
            });
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setSearchText(searchText);
                return postDataSource.doSearch(searchText,current,pageSize);
            });
            CompletableFuture<Page<VideoVo>> videoTask = CompletableFuture.supplyAsync(() -> {
                return videoDataSource.doSearch(searchText,current,pageSize);
            });

            CompletableFuture.allOf(pictureTask, userTask, pictureTask).join();

            SearchVO searchVO = new SearchVO();
            try {
                searchVO.setPostList(postTask.get().getRecords());
                searchVO.setUserList(userTask.get().getRecords());
                searchVO.setPictureList(pictureTask.get().getRecords());
                searchVO.setVideoList(videoTask.get().getRecords());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return searchVO;
//            Page<Picture> picturePage = pictureDataSource.doSearch(searchText, 1, 20);
//
//            UserQueryRequest userQueryRequest = new UserQueryRequest();
//            userQueryRequest.setUserName(searchText);
//            Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
//
//            PostQueryRequest postQueryRequest = new PostQueryRequest();
//            postQueryRequest.setSearchText(searchText);
//            Page<PostVO> postVOPage = postDataSource.doSearch(searchText, current, pageSize);
//
//            Page<VideoVo> videoVoPage = videoDataSource.doSearch(searchText, current, pageSize);
//            SearchVO searchVO = new SearchVO();
//            searchVO.setPictureList(picturePage.getRecords());
//            searchVO.setUserList(userVOPage.getRecords());
//            searchVO.setPostList(postVOPage.getRecords());
//            searchVO.setVideoList(videoVoPage.getRecords());
//            return searchVO;
        } else {
            DataSource<?> dataSource = dataSourceRegistry.getDataSource(searchTypeEnum.getValue());
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            SearchVO searchVO = new SearchVO();
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
