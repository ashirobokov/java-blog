package ru.ashirobokov.blog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import ru.ashirobokov.blog.cache.Cache;
import ru.ashirobokov.blog.cache.CacheImpl;
import ru.ashirobokov.blog.model.Article;
import ru.ashirobokov.blog.repository.BlogCustomRepository;
import ru.ashirobokov.blog.repository.BlogRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class  BlogServiceImpl implements BlogService {

/**
 *      we are providing a constructor for a field initialization instead of field injection  @Autowired
  */
//  @Autowired
    private final BlogRepository blogRepository;
//    @Autowired
    private final BlogCustomRepository blogCustomRepository;

    Cache cache = CacheImpl.getCache();

    @Override
    public Article createArticle(Article article) {
        log.info("[Service] createArticle article = {}", article);
        cache.put(article.getArticleName(), article);
        return blogRepository.save(article);
    }

    @Override
    public Article findArticleByName(String name) {
        log.info("[Service] findArticleByName with name = {}", name);
        Article foundArticle = (Article) cache.get(name);
        if (foundArticle == null) {
            foundArticle = blogRepository.findByArticleName(name);
            cache.put(name, foundArticle);
        }
        return foundArticle != null ? foundArticle : new Article("","","");
    }

    @Override
    public Article updateArticle(String name, Article changedArticle) {
        log.info("[Service] updateArticle with name = {}", name);
        Article article = blogRepository.findByArticleName(name);
        if (article != null) {
            article.setArticleName(changedArticle.getArticleName());
            article.setArticleText(changedArticle.getArticleText());
            article.setKeyword(changedArticle.getKeyword());
            log.info("[Service] Article with name = {} updated", name);
        } else {
            log.info("[Service] updateArticle with name = {} error. Article not found, update isn't possible", name);
            return new Article("", "", "");
        }
        return blogRepository.save(article);
    }

    @Override
    public void deleteArticle(String name) {
        log.info("[Service] deleteArticle with name = {}", name);
        Article article = blogRepository.findByArticleName(name);
        if (article != null) {
            blogRepository.delete(article);
            cache.remove(article.getArticleName());
        } else {
            log.info("[Service] deleteArticle with name = {} error. Article not found", name);
        }
    }

    @Override
    public List<Article> findArticlesByKeyword(String keyword) {
        log.info("[Service] findArticlesByKeyword keyword = {}", keyword);
        List<Article> foundArticles = (List<Article>) cache.get(keyword);
        if (foundArticles != null) {
            return foundArticles;
        } else {
            foundArticles = blogCustomRepository.findArticlesByKeyword(keyword);
            if (foundArticles != null) {
                log.info("[Service] findArticlesByKeyword keyword = {} found {} article(es)", keyword, foundArticles.size());
                cache.put(keyword, foundArticles);
                return foundArticles;
            } else {
                log.info("[Service] findArticlesByKeyword keyword = {} not found any article)", keyword);
                return new ArrayList<Article>();
            }
        }
    }

}
