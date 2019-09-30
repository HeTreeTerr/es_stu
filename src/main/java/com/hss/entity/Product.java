package com.hss.entity;

import lombok.Data;

/**
 * product实体类
 */
@Data
public class Product {
    /** 名称 */
    private String name;
    /** 作者 */
    private String author;
    /** 版本 */
    private String version;
}
