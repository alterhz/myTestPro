package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import com.magazine.dao.ConfigRepository;
import com.magazine.dao.SchemaFieldRepository;
import com.magazine.dao.FilterRepository;
import com.magazine.dao.SheetRepository;
import com.magazine.model.Sheet;
import com.magazine.model.SheetFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/view")
public class ViewController {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private SchemaFieldRepository schemaFieldRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private SheetRepository sheetRepository;

//    @GetMapping(produces = "application/json")
    @GetMapping()
    public Sheet view(@RequestParam("filter") String filter) {
        final SheetFilter sheetfilter = filterRepository.getFilter(filter);
        if (sheetfilter == null) {
            logger.error("过滤器错误. filter = {}", filter);
            return null;
        }

        final String sheetName = sheetfilter.getSheetName();
        final List<Map<String, Object>> rows = sheetRepository.getRows(sheetName);
        final String searchField = configRepository.getConfig(RedisConsts.CONFIG_KEY_SEARCH_FIELD);
        final Sheet sheet = Sheet.createSheet(sheetName, searchField, rows, sheetfilter);
        return sheet;
    }

}
