package ru.ashirobokov.blog.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ashirobokov.blog.model.Article;

public interface BlogRepository extends CrudRepository<Article, String> {

    Article findByArticleName(String name);

}
