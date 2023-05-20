package com.shaonian.search.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 少年
 */
@Data
public class Picture implements Serializable {

    private static final long serialVersionUID = -1534907732591874610L;
    private String title;
    private String url;

}
