package ru.ashirobokov.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ashirobokov.blog.model.Article;

import java.util.List;

public interface BlogCustomRepository extends JpaRepository<Article, String> {
    @Query(value="SELECT * FROM article WHERE keyword LIKE %:keyword%", nativeQuery = true)
    List<Article> findArticlesByKeyword(@Param("keyword") String keyword);
}
