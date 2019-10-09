package com.hss.entity;

/**
 * product实体类
 */
public class Product {
    /** 名称 */
    private String name;
    /** 作者 */
    private String author;
    /** 版本 */
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
