package ru.ashirobokov.blog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ArticleDto {
    private String articleName;
    private String articleText;
    private String keyword;
}
