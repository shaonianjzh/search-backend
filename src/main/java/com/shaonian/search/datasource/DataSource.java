package com.shaonian.search.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 *
 * 数据源接口
 * @author 少年
 */
public interface DataSource<T> {

    /**
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T>  doSearch(String searchText, long pageNum, long pageSize);

}
