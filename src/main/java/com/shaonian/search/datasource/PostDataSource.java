package com.shaonian.search.datasource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.search.model.dto.post.PostQueryRequest;
import com.shaonian.search.model.vo.PostVO;
import com.shaonian.search.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 *
 * @author 少年
 * 
 */
@Service
@Slf4j
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setCurrent(pageNum);
        postQueryRequest.setPageSize(pageSize);
        postQueryRequest.setSearchText(searchText);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        return postService.listPostVoPage(postQueryRequest,request);
    }
}




