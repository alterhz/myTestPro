package com.magazine.controller;

import com.magazine.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/bookHash")
public class BookHashController {

    public static final String KEY_HASH_BOOK = "hash:book";
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/{reader}", method = RequestMethod.GET)
    public String books(@PathVariable("reader") String reader, Model model) {

            // hash
        final Set keys = redisTemplate.opsForHash().keys(KEY_HASH_BOOK);
        final List<Book> bookList = redisTemplate.opsForHash().multiGet(KEY_HASH_BOOK, keys);
        model.addAttribute("books", bookList);

        return "bookList";
    }

    @RequestMapping(value = "/{reader}", method=RequestMethod.POST)
    public String addBook(@PathVariable("reader") String reader, Book book) {
        if (!"zl".equals(reader)) {
            return "redirect:/bookList/{reader}?msg=role";
        }

        redisTemplate.opsForHash().put(KEY_HASH_BOOK, book.getName(), book);
        return "redirect:/bookHash/{reader}";
    }

    @RequestMapping(value = "/search", method=RequestMethod.GET)
    public String search(@RequestParam("name") String name, Model model) {

        System.out.println("name = " + name);


        final List<Book> bookList = new ArrayList<>();
        final Object o = redisTemplate.opsForHash().get(KEY_HASH_BOOK, name);
        if (o != null) {
            bookList.add((Book) o);
        }

        model.addAttribute("books", bookList);

        return "bookList";
    }

}
