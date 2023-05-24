package com.ercanbeyen.casestudy.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class CustomPage<T, V> {
    private List<V> content;
    private int number;
    private int size;
    private Sort sort;
    private int totalPages;
    private Long totalElements;

    public CustomPage(Page<T> page, List<V> content) {
        this.content = content;
        this.number = page.getNumber();
        this.size = page.getSize();
        this.sort = page.getSort();
        this.totalElements = page.getTotalElements();
    }
}
