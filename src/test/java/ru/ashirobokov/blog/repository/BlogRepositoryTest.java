package ru.ashirobokov.blog.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ashirobokov.blog.model.Article;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class BlogRepositoryTest {
    @Autowired
    BlogRepository repository;

    @BeforeAll
    public static void setup() {
        log.info("[BlogRepositoryTest] @BeforeAll setup");
    }

    @AfterAll
    public static void tearDown() {
        log.info("[BlogRepositoryTest] @AfterAll tearDown");
    }

    @Test
    @Transactional
    public void findByArticleNameTest() {
        log.info("[BlogRepositoryTest] findByArticleNameTest");
        repository.save(new Article("Test Article", "Test text", "test#test"));
        Article testArticle = repository.findByArticleName("Test Article");
        assertEquals("Test text", testArticle.getArticleText(), () -> "Article text is: " + testArticle.getArticleText());
        assertEquals("test#test", testArticle.getKeyword(), () -> "Article keyword is: " + testArticle.getKeyword());
    }

}
