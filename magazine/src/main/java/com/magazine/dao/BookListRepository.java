package com.magazine.dao;

import com.magazine.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookListRepository extends JpaRepository<Book, Long> {
    List<Book> findByName(String name);
}
