package com.shaonian.search;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.shaonian.search.model.entity.Picture;
import com.shaonian.search.model.entity.Post;
import com.shaonian.search.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    void testFetchPicture() throws IOException {
        String url = "https://www.bing.com/images/search?q=%e6%9d%8e%e6%b2%81&form=HDRSC3&first=1";
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.varh.isv");
        List<Picture> pictureList = new ArrayList<>();
        for(Element element:elements){
            //图片地址
            Picture picture = new Picture();
            String m = element.select(".iusc").get(0).attr("m");
            Map map = JSONUtil.toBean(m, Map.class);
            String mUrl =(String) map.get("murl");

            //图片标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            picture.setTitle(title);
            picture.setUrl(mUrl);
            pictureList.add(picture);
        }
    }


    @Test
    void testFetchPassage(){

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
        Assertions.assertTrue(b);
    }
}
