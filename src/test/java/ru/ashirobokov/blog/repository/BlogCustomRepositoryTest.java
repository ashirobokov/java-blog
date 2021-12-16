package ru.ashirobokov.blog.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ashirobokov.blog.model.Article;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class BlogCustomRepositoryTest {
    @Autowired
    BlogCustomRepository customRepository;

    @Test
    @Transactional
    public void findArticlesByKeywordTest() {
        log.info("[BlogCustomRepositoryTest] findArticlesByKeyword");
        customRepository.save(new Article("Test Article 1", "Test-text1", "unit#test1"));
        customRepository.save(new Article("Test Article 2", "Test-text2", "unit#test2"));
        customRepository.save(new Article("Test Article 3", "Test-text3", "unit#test3"));
        List<Article> found3articles = customRepository.findArticlesByKeyword("test");
        assertEquals(3, found3articles.size(), () -> "List size = " + found3articles.size());
        List<Article> found1article = customRepository.findArticlesByKeyword("test2");
        assertEquals(1, found1article.size(), () -> "List size = " + found1article.size());
    }

}
