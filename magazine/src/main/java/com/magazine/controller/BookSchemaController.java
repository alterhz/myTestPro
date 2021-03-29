package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.ListUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bookSchema")
public class BookSchemaController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String schema(Model model) {
        final List<String> fields = ControllerUtils.getBookSchemaFields(redisTemplate);
        model.addAttribute("fields", fields);
        return "bookSchema";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String saveSchema(@RequestParam("fields") String[] fields, Model model) {
        redisTemplate.delete(RedisConsts.BOOK_SCHEMA_KEY);
        redisTemplate.opsForList().leftPushAll(RedisConsts.BOOK_SCHEMA_KEY, fields);
        return "redirect:/bookSchema/";
    }

}
