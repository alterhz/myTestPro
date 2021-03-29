package com.magazine.controller;

import com.magazine.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
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
@RequestMapping("/searchBook")
public class SearchBookController {

    public static final String KEY_HASH_BOOK = "hash:book";

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public String home(@PathVariable("name") String name, Model model) {
        List<Book> bookList = new ArrayList<>();
        Set keys = redisTemplate.keys("book:*");
        for (Object key : keys) {
            ValueOperations<String, Book> valueOperations = redisTemplate.opsForValue();
            Book book = valueOperations.get(key);
            bookList.add(book);
        }
        model.addAttribute("books", bookList);
        return "searchBook";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST)
    public String search(@RequestParam("name") String name, Model model) {
        if (StringUtils.isEmpty(name)) {
            return "redirect:/searchBook/1";
        }
        List<Book> bookList = new ArrayList<>();
        Set keys = redisTemplate.keys("book:*" + name + "*");
        for (Object key : keys) {
            ValueOperations<String, Book> valueOperations = redisTemplate.opsForValue();
            Book book = valueOperations.get(key);
            bookList.add(book);
        }
        model.addAttribute("books", bookList);
        return "searchBook";
    }



}
