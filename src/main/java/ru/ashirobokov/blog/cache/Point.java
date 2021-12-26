package ru.ashirobokov.blog.cache;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Point {

    private Object key;
    private long savedTime;
    private long accessCounter;

}
