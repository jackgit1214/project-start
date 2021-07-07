package com.project.core.mybatis.model;

public class Carousel implements Comparable<Carousel> {
    String filepath;
    String title;
    String description;
    String href;
    String articleId;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }


    public int compareTo(Carousel o) {
        return o.getArticleId().compareTo(articleId);
    }

}
