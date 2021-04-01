package com.magazine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.magazine.constant.RedisConsts;
import com.magazine.dao.RedisSequenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/bookList")
public class BookListController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String bookAll(Model model) {
        final List<String> fields = ControllerUtils.getBookSchemaFields(redisTemplate);
        model.addAttribute("fields", fields);

        final String searchField = ControllerUtils.getConfigSearchField(redisTemplate);
        model.addAttribute("searchField", searchField);

        List<Map> bookList = new ArrayList<>();
        Set keys = redisTemplate.keys("book:*");
        for (Object key : keys) {
            final Map keyValues = (Map)redisTemplate.opsForValue().get(key);
            bookList.add(keyValues);
        }

        ControllerUtils.sortByField(bookList, searchField);

        model.addAttribute("books", bookList);
        return "bookList";
    }

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String addBook(@RequestParam() Map keyValue) throws JsonProcessingException {
        final String id;
        if (keyValue.containsKey(RedisConsts.ID)) {
            id = (String)keyValue.get(RedisConsts.ID);
        } else {
            final long genID = redisSequenceFactory.generate("id", 1);
            id = String.valueOf(genID);
            keyValue.put(RedisConsts.ID, id);
        }
        String key = RedisConsts.BOOK_VALUE_KEY_PRXFIX + id;
        redisTemplate.opsForValue().set(key, keyValue);
        return "redirect:/bookList/";
    }

    @RequestMapping(value = "/edit", method=RequestMethod.GET)
    public String edit(@RequestParam("id") String id, Model model) {
        final List<String> fields = ControllerUtils.getBookSchemaFields(redisTemplate);
        model.addAttribute("fields", fields);

        String key = RedisConsts.BOOK_VALUE_KEY_PRXFIX + id;
        final Object book = redisTemplate.opsForValue().get(key);
        if (book == null) {
            System.out.println("edit book is null. key = " + key);
            return "error";
        } else {
            model.addAttribute("book", book);
        }
        return "editBook";
    }

    @RequestMapping(value = "/del", method=RequestMethod.GET)
    public String del(@RequestParam("id") String id, Model model) {
        String key = RedisConsts.BOOK_VALUE_KEY_PRXFIX + id;
        final Boolean delete = redisTemplate.delete(key);
        if (delete) {
            model.addAttribute("msg", "删除成功！");
        } else {
            System.out.println("del book is null. key = " + key);
            return "error";
        }

        return "redirect:/bookList/";
    }




}
