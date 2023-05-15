package com.shaonian.search.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaonian.search.model.entity.Picture;

/**
 * @author 少年
 */
public interface PictureService {

    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
