package com.magazine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.magazine.constant.RedisConsts;
import com.magazine.dao.ConfigRepository;
import com.magazine.dao.RedisSequenceFactory;
import com.magazine.dao.SchemaFieldRepository;
import com.magazine.dao.SheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bookList")
public class BookListController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisSequenceFactory redisSequenceFactory;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private SchemaFieldRepository schemaFieldRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String bookAll(Model model) {
        final List<String> schemaFields = schemaFieldRepository.getSchemaFields();
        model.addAttribute("fields", schemaFields);

        final String searchField = configRepository.getConfig(RedisConsts.CONFIG_KEY_SEARCH_FIELD);
        model.addAttribute("searchField", searchField);

        final List<Map<String, Object>> bookList = sheetRepository.getRows("book");
        ControllerUtils.sortByField(bookList, searchField);

        model.addAttribute("books", bookList);
        return "bookList";
    }

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String addOrUpdateBook(@RequestParam() Map keyValue) {
        final String id;
        if (keyValue.containsKey(RedisConsts.ID)) {
            id = (String)keyValue.get(RedisConsts.ID);
        } else {
            final long genID = redisSequenceFactory.generate("id", 1);
            id = String.valueOf(genID);
            keyValue.put(RedisConsts.ID, id);
        }
        String key = RedisConsts.BOOK_VALUE_KEY_PREFIX + id;
        sheetRepository.setRow(key, keyValue);
        return "redirect:/bookList/";
    }

    @RequestMapping(value = "/edit", method=RequestMethod.GET)
    public String edit(@RequestParam("id") String id, Model model) {
        final List<String> schemaFields = schemaFieldRepository.getSchemaFields();
        model.addAttribute("fields", schemaFields);

        String key = RedisConsts.BOOK_VALUE_KEY_PREFIX + id;
        final Map<String, Object> row = sheetRepository.getRow(key);
        model.addAttribute("book", row);
        return "edit";
    }

    @RequestMapping(value = "/del", method=RequestMethod.GET)
    public String delete(@RequestParam("id") String id, Model model) {
        String key = RedisConsts.BOOK_VALUE_KEY_PREFIX + id;
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
