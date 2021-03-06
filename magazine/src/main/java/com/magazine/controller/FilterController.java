package com.magazine.controller;

import com.magazine.constant.RedisConsts;
import com.magazine.dao.ConfigRepository;
import com.magazine.dao.FilterRepository;
import com.magazine.dao.SchemaFieldRepository;
import com.magazine.dao.SheetRepository;
import com.magazine.model.Sheet;
import com.magazine.model.SheetFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/filters")
public class FilterController {
    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(FilterController.class);

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private ConfigRepository configRepository;


    @RequestMapping(method = RequestMethod.GET)
    public List<SheetFilter> filters() {
        return filterRepository.getFilters();
    }

    @RequestMapping(value="/{name}", method = RequestMethod.GET)
    public SheetFilter getByName(@PathVariable String name) {
        return filterRepository.getFilter(name);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<SheetFilter> save(@RequestBody SheetFilter sheetFilter) {
        if (StringUtils.isEmpty(sheetFilter.getFilterName())) {
            // 保存失败
            return new ResponseEntity<>(HttpStatus.OK);
        }
        filterRepository.setFilter(sheetFilter.getFilterName(), sheetFilter);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value="/{name}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String name) {
        filterRepository.delete(name);
    }

}
