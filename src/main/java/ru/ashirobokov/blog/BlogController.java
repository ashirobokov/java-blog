package ru.ashirobokov.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ashirobokov.blog.model.Article;
import ru.ashirobokov.blog.model.ArticleDto;
import ru.ashirobokov.blog.service.BlogService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/blog")
public class BlogController {
    @Autowired
    BlogService blogService;

    @GetMapping(value = "test")
    public String test()
    {
        log.info("[Controller] test ... 'test'");
        return "Hello, Test!";
    }


    @PostMapping(value = "/article")
    public ArticleDto createArticle(@RequestBody(required = true) ArticleDto articleDto) {
        log.info("[Controller] createArticle with articleDto = {}", articleDto);
        return mapToDto(blogService.createArticle(mapFromDto(articleDto)));
    }

    @GetMapping(value = "/articles/{name}")
    public ArticleDto getArticle(@PathVariable(value = "name") String name) {
        log.info("[Controller] getArticle with name = {}", name);
        return mapToDto(blogService.findArticleByName(name));
    }

    @GetMapping(value = "/articles/list/{keyword}")
    public List<ArticleDto> getArticlesByKeyword(@PathVariable(value = "keyword") String keyword) {
        log.info("[Controller] getArticlesByKeyword with keyword = {}", keyword);
        List<ArticleDto> articleDtoList = new ArrayList<>();
        blogService.findArticlesByKeyword(keyword)
                .forEach(article -> articleDtoList.add(mapToDto(article)));
        return articleDtoList;
    }

    @PutMapping(value = "/articles/{name}")
    public ArticleDto updateArticle(@RequestBody(required = true) ArticleDto articleDto,
                                    @PathVariable(value = "name") String name) {
        log.info("[Controller] updateArticle with name = {}", name);
        return mapToDto(blogService.updateArticle(name, mapFromDto(articleDto)));
    }

    @DeleteMapping(value = "/articles/{name}")
    public void deleteArticle(@PathVariable(value = "name") String name) {
        log.info("[Controller] deleteArticle with name = {}", name);
        blogService.deleteArticle(name);
    }

    private ArticleDto mapToDto(Article article) {
        return new ArticleDto(article.getArticleName(), article.getArticleText(), article.getKeyword());
    }

    private Article mapFromDto(ArticleDto articleDto) {
        return new Article(articleDto.getArticleName(), articleDto.getArticleText(), articleDto.getKeyword());
    }

}
