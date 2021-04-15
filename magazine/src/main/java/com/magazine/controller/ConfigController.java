package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import com.magazine.dao.ConfigRepository;
import com.magazine.dao.SchemaFieldRepository;
import com.magazine.dao.SheetRepository;
import com.magazine.model.SchemaField;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private SchemaFieldRepository schemaFieldRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String config(Model model) {
        // 表格字段结构
        final List<SchemaField> schemaFields = schemaFieldRepository.getSchemaFields(RedisConsts.BOOK_SCHEMA_KEY);
        final List<String> fields = schemaFields.stream().map(schemaField -> schemaField.getField()).collect(Collectors.toList());
        model.addAttribute("fields", fields);

        // 搜索字段
        final String searchField = configRepository.getConfig(RedisConsts.CONFIG_KEY_SEARCH_FIELD);
        model.addAttribute("searchField", searchField);

        return "config";
    }

    @RequestMapping(value = "/saveSchema", method = RequestMethod.POST)
    public String saveSchema(@RequestParam("fields") String[] fields, RedirectAttributes model) {
        final List<SchemaField> schemaFields = Arrays.stream(fields)
                .map(s -> SchemaField.of(s, s, 0))
                .collect(Collectors.toList());
        schemaFieldRepository.saveSchemaFields(RedisConsts.BOOK_SCHEMA_KEY, schemaFields);
        model.addFlashAttribute("msg", "字段列表，保存成功！");
        return "redirect:/config/";
    }

    @RequestMapping(value = "/saveConfig", method= RequestMethod.POST)
    public String saveConfig(@RequestParam() Map keyValue, RedirectAttributes model) {
        configRepository.saveConfig(keyValue);
        model.addFlashAttribute("msg", "搜索字段，保存成功！");
        return "redirect:/config/";
    }
}
