package ru.ashirobokov.blog.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ashirobokov.blog.cache.Cache;
import ru.ashirobokov.blog.model.Article;
import ru.ashirobokov.blog.repository.BlogCustomRepository;
import ru.ashirobokov.blog.repository.BlogRepository;

import java.util.ArrayList;
import java.util.List;

//using AssertionsForClassTypes.assertThat
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private BlogCustomRepository blogCustomRepository;

    private BlogService blogService;
    private Article article;
    private String testArticleName;
    private List<Article> testArticleList;
    private List<Article> test2ArticleList;

    @BeforeEach
    public void setup() {
        testArticleName = "Test Article";
        blogService = new BlogServiceImpl(blogRepository, blogCustomRepository);
        article = new Article(1,testArticleName, "Test text text", "test#test");

        testArticleList = new ArrayList<>();
        testArticleList.add(new Article("Test Article 1", "Test-text1", "unit#test1"));
        testArticleList.add(new Article("Test Article 2", "Test-text2", "unit#test2"));
        testArticleList.add(new Article("Test Article 3", "Test-text3", "unit#test3"));

        test2ArticleList = new ArrayList<>();
        test2ArticleList.add(new Article("Test Article 2", "Test-text2", "unit#test2"));

    }

    @Test
    public void createArticleTest() {
        log.info("[BlogServiceTest] createArticleTest");
        when(blogRepository.save(any(Article.class))).then(returnsFirstArg());

        Article savedArticle = blogService.createArticle(article);
//  with using assertJ framework
//        assertThat(savedArticle.getArticleName().length() > 0);
        assertEquals(testArticleName, savedArticle.getArticleName());
    }

    @Test
    public void findArticleByNameTest() {
        log.info("[BlogServiceTest] findArticleByNameTest");
        when(blogRepository.findByArticleName(testArticleName)).thenReturn(article);

        Article foundArticle = blogService.findArticleByName(testArticleName);
        assertEquals(testArticleName, foundArticle.getArticleName());

        Article notFoundArticle = blogService.findArticleByName("Noname Article");
        assertEquals(true, notFoundArticle.getArticleName().isEmpty(), () -> "NotFoundArticle " + notFoundArticle);
    }

    @Test
    public void updateArticleTest() {
        log.info("[BlogServiceTest] updateArticleTest");
        when(blogRepository.findByArticleName(testArticleName)).thenReturn(article);
        when(blogRepository.save(any(Article.class))).then(returnsFirstArg());

        Article changedArticle = new Article(1,testArticleName, "Test text updated text", "test#test");
        Article updatedArticle = blogService.updateArticle(testArticleName, changedArticle);

        assertEquals("Test text updated text", updatedArticle.getArticleText());
    }

    @Test
    public void deleteArticleTest() {
        log.info("[BlogServiceTest] deleteArticleTest");
        when(blogRepository.findByArticleName(testArticleName)).thenReturn(article);

        blogService.deleteArticle(article.getArticleName());

        verify(blogRepository, times(1)).delete(article);
    }

    @Test
    public void findArticlesByKeywordTest() {
        log.info("[BlogServiceTest] findArticlesByKeywordTest");
// #1
        when(blogCustomRepository.findArticlesByKeyword("test")).thenReturn(testArticleList);
        List<Article> found3Articles = blogService.findArticlesByKeyword("test");
        assertEquals(3, found3Articles.size(), () -> "List size = " + found3Articles.size());
// #2
        when(blogCustomRepository.findArticlesByKeyword("test2")).thenReturn(test2ArticleList);
        List<Article> found1Article = blogService.findArticlesByKeyword("test2");
        assertEquals(1, found1Article.size(), () -> "List size = " + found1Article.size());
// #3
        List<Article> found0Article = blogService.findArticlesByKeyword("test125");
        assertEquals(0, found0Article.size(), () -> "List size = " + found0Article.size());
    }

}
