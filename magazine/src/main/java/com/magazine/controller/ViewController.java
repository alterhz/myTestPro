package com.magazine.controller;

import com.magazine.dao.SheetFilterRepository;
import com.magazine.dao.SheetRepository;
import com.magazine.model.Sheet;
import com.magazine.model.SheetFilter;
import com.magazine.model.SheetRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/view")
public class ViewController {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(ViewController.class);

    @Autowired
    private SheetFilterRepository sheetFilterRepository;

    @Autowired
    private SheetRepository sheetRepository;

//    @GetMapping(produces = "application/json")
    @GetMapping()
    public Sheet view(@RequestParam("filterName") String filterName) {
        final SheetFilter sheetfilter = sheetFilterRepository.getFilter(filterName);
        if (sheetfilter == null) {
            logger.error("过滤器错误. filterName = {}", filterName);
            return null;
        }

        final String sheetName = sheetfilter.getSheetName();

        final List<SheetRow> sheetRows = sheetRepository.getRows(sheetName);

        final Sheet resultSheet = Sheet.createSheet(sheetName, sheetRows, sheetfilter);
        return resultSheet;
    }
}
