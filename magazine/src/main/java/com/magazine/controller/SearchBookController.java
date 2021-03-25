package com.magazine.controller;

import com.magazine.model.Book;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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
        final Set keys = redisTemplate.opsForHash().keys(KEY_HASH_BOOK);
        final List<Book> bookList = redisTemplate.opsForHash().multiGet(KEY_HASH_BOOK, keys);
        model.addAttribute("books", bookList);

        return "searchBook";
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST)
    public String search(@RequestParam("name") String name, Model model) {
        System.out.println("name = " + name);

        if (StringUtils.isEmpty(name)) {
            return "redirect:/searchBook/1";
        }

        final List<Book> bookList = new ArrayList<>();
//        final Object o = redisTemplate.opsForHash().get(KEY_HASH_BOOK, name);
//        if (o != null) {
//            bookList.add((Book) o);
//        }

        // 模糊搜索
        final Cursor<Map.Entry<String, Book>> cursor = redisTemplate.opsForHash().scan(KEY_HASH_BOOK,
                ScanOptions.scanOptions().match("*" + name + "*").count(10).build());
        while (cursor.hasNext()) {
            final Map.Entry<String, Book> next = cursor.next();
            final Book book = next.getValue();
            bookList.add(book);
        }


        model.addAttribute("books", bookList);

        return "searchBook";
    }


}
