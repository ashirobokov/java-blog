package ru.ashirobokov.blog.service;

import ru.ashirobokov.blog.model.Article;

import java.util.List;

public interface BlogService {

    Article createArticle(Article article);

    Article findArticleByName(String name);

    Article updateArticle(String name, Article article);

    void deleteArticle(String name);

    List<Article> findArticlesByKeyword(String keyword);

}
