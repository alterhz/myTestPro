package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.Collator;
import java.util.*;

@Controller
@RequestMapping("/")
public class SearchBookController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String search(@RequestParam("name") String name, Model model) {
        final List<String> fields = ControllerUtils.getBookSchemaFields(redisTemplate);
        model.addAttribute("fields", fields);

        final String searchField = ControllerUtils.getConfigSearchField(redisTemplate);
        model.addAttribute("searchField", searchField);

        List<Map<String, Object>> bookList = new ArrayList<>();
        Set keys = redisTemplate.keys("book:*");

        for (Object key : keys) {
            final Map keyValues = (Map)redisTemplate.opsForValue().get(key);
            assert keyValues != null;
            final String nameValue = (String)keyValues.get(searchField);
            if (StringUtils.isEmpty(name)) {
                bookList.add(keyValues);
            } else if (StringUtils.contains(nameValue, name)) {
                bookList.add(keyValues);
            }
        }

        ControllerUtils.sortByField(bookList, searchField);

        model.addAttribute("books", bookList);
        model.addAttribute("name", name);
        return "searchBook";
    }




}
