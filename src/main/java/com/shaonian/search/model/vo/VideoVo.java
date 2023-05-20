package com.shaonian.search.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class VideoVo implements Serializable {
    private String arcurl;
    private String pic;
    private String title;
    private String description;
    private String author;
    private Integer pubdate;
    private String upic;
    private static final long serialVersionUID = 7037843325406822290L;
}
