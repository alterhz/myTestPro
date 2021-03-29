package com.magazine.model;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {

    @Id
    private Long id;
    /** 数据 {@literal 列名称->数据 } */
    private String keyValues;

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(String keyValues) {
        this.keyValues = keyValues;
    }
}
