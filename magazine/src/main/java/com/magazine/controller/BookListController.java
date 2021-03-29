package com.magazine.controller;

import com.magazine.dao.BookListRepository;
import com.magazine.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/bookList")
public class BookListController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/{reader}", method = RequestMethod.GET)
    public String books(@PathVariable("reader") String reader, Model model) {

        List<Book> bookList = new ArrayList<>();
        Set keys = redisTemplate.keys("book:*");
        for (Object key : keys) {
            ValueOperations<String, Book> valueOperations = redisTemplate.opsForValue();
            Book book = valueOperations.get(key);
            bookList.add(book);
        }
        model.addAttribute("books", bookList);

        return "bookList";
    }

    @RequestMapping(value = "/{reader}", method=RequestMethod.POST)
    public String addBook(@PathVariable("reader") String reader, Book book) {
        if (!"zl".equals(reader)) {
            return "redirect:/bookList/{reader}?msg=role";
        }

        ValueOperations<String, Book> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("book:" + book.getName(), book);
        return "redirect:/bookList/{reader}";
    }

}
