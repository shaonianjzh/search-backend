package com.shaonian.search.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shaonian.search.model.entity.Post;
import com.shaonian.search.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 少年
 */
@Slf4j
//@Component
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) throws Exception {
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}\n";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";

        String body = HttpRequest.post(url)
                .body(json)
                .execute()
                .body();
        Map<String,Object> map = JSONUtil.toBean(body, Map.class);
        System.out.println(map);
        JSONObject data = (JSONObject)map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for(Object recode:records){
            Post post = new Post();
            JSONObject tempRecode = (JSONObject)recode;
            post.setTitle( tempRecode.getStr("title"));
            post.setContent( tempRecode.getStr("content"));
            JSONArray tags = tempRecode.getJSONArray("tags");
            List<String> tagsList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagsList));
            post.setUserId(1657380878698496002L);
            postList.add(post);
        }
        //数据入库
        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("获取初始化帖子列表成功，条数 = {}", postList.size());
        } else {
            log.error("获取初始化帖子列表失败");
        }
    }
}
