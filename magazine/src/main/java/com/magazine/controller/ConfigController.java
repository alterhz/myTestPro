package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/bookConfig")
public class ConfigController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/", method= RequestMethod.POST)
    public String saveConfig(@RequestParam() Map keyValue) {
        keyValue.forEach((k, v) -> {
            redisTemplate.opsForHash().put(RedisConsts.CONFIG_KEY, k, v);
        });
        return "redirect:/bookSchema/";
    }
}
