package ru.ashirobokov.blog.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private int articleId;
    @Column(name = "article_name")
    private String articleName;
    @Column(name = "article_text")
    private String articleText;
    private String keyword;

    public Article(String articleName, String articleText, String keyword) {
        this.articleName = articleName;
        this.articleText = articleText;
        this.keyword = keyword;
    }

}
