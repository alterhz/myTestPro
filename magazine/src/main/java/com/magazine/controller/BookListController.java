package com.magazine.controller;

import com.magazine.dao.BookListRepository;
import com.magazine.model.Book;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Controller
@RequestMapping("/bookList")
public class BookListController {

    private BookListRepository bookListRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    public BookListController(BookListRepository bookListRepository) {
        this.bookListRepository = bookListRepository;
    }

    @RequestMapping(value = "/{reader}", method = RequestMethod.GET)
    public String books(@PathVariable("reader") String reader, Model model) {

//        final List<Book> bookList = bookListRepository.findAll();
        final List<Book> bookList = redisTemplate.opsForList().range("bookList", 0, -1);
            // hash
//        final Map<String, Book> bookMap = redisTemplate.opsForHash().entries("bookMap");
//        final Set keys = redisTemplate.opsForHash().keys("bookMap");
//        final List<Book> bookList = redisTemplate.opsForHash().multiGet("bookMap", keys);
        model.addAttribute("books", bookList);


        return "bookList";
    }

    @RequestMapping(value = "/{reader}", method=RequestMethod.POST)
    public String addBook(@PathVariable("reader") String reader, Book book) {
        if (!"zl".equals(reader)) {
            return "redirect:/bookList/{reader}?msg=role";
        }

        redisTemplate.opsForList().rightPush("bookList", book);
//        redisTemplate.opsForHash().put("bookMap", book.getName(), book);
//        bookListRepository.save(book);
        return "redirect:/bookList/{reader}";
    }



}
