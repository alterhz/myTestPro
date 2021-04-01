package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String config(Model model) {
        final List<String> fields = ControllerUtils.getBookSchemaFields(redisTemplate);
        model.addAttribute("fields", fields);
        final String searchField = ControllerUtils.getConfigSearchField(redisTemplate);
        model.addAttribute("searchField", searchField);
        return "config";
    }

    @RequestMapping(value = "/saveSchema", method = RequestMethod.POST)
    public String saveSchema(@RequestParam("fields") String[] fields, RedirectAttributes model) {
        redisTemplate.delete(RedisConsts.BOOK_SCHEMA_KEY);
        redisTemplate.opsForList().leftPushAll(RedisConsts.BOOK_SCHEMA_KEY, Arrays.asList(fields));
        model.addFlashAttribute("msg", "字段列表，保存成功！");
        return "redirect:/config/";
    }

    @RequestMapping(value = "/saveConfig", method= RequestMethod.POST)
    public String saveConfig(@RequestParam() Map keyValue, RedirectAttributes model) {
        keyValue.forEach((k, v) -> {
            redisTemplate.opsForHash().put(RedisConsts.CONFIG_KEY, k, v);
        });
        model.addFlashAttribute("msg", "搜索字段，保存成功！");
        return "redirect:/config/";
    }
}
