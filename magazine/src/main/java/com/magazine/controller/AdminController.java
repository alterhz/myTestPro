package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import com.magazine.dao.ConfigRepository;
import com.magazine.dao.RedisSequenceFactory;
import com.magazine.dao.SchemaFieldRepository;
import com.magazine.dao.SheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/filters")
    public String filters() {
        return "filter";
    }

}
