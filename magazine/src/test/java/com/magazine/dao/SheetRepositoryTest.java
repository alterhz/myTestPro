package com.magazine.dao;

import com.magazine.MagazineApplication;
import com.magazine.model.KeyValue;
import com.magazine.model.SheetRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@ExtendWith(SpringExtension. class)
//@SpringBootTest(classes = MagazineApplication.class)
class SheetRepositoryTest {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SheetRepositoryTest.class);

    @Autowired
    private SheetRepository sheetRepository;

    @Test
    void addRowAndGet() {
        Map<String, Object> keyValues = new HashMap<>();
        keyValues.put("id", 1000L);
        keyValues.put("期刊名称", "上下五千年");
        keyValues.put("类别", "文学");
        keyValues.put("级别", "中");
        keyValues.put("网站", "http://www.shangxiawu.com");

        final String sheetKey = "test:" + 1000L;
        sheetRepository.setRow(sheetKey, keyValues);

        final Map<String, Object> row = sheetRepository.getRow(sheetKey);
        logger.info("row = " + row);
    }

    @Test
    void testGetRows() {
        {
            Map<String, Object> keyValues = new HashMap<>();
            final long id = 1001L;
            keyValues.put("id", id);
            keyValues.put("期刊名称", "中国魂");
            keyValues.put("类别", "文学");
            keyValues.put("级别", "高");
            keyValues.put("网站", "http://www.china.com");

            final String sheetKey = "test:" + id;
            sheetRepository.setRow(sheetKey, keyValues);
        }

        {
            Map<String, Object> keyValues = new HashMap<>();
            keyValues.put("id", 1000L);
            keyValues.put("期刊名称", "上下五千年");
            keyValues.put("类别", "文学");
            keyValues.put("级别", "中");
            keyValues.put("网站", "http://www.shangxiawu.com");

            final String sheetKey = "test:" + 1000L;
            sheetRepository.setRow(sheetKey, keyValues);
        }



        {
            Map<String, Object> keyValues = new HashMap<>();
            final long id = 1002L;
            keyValues.put("id", id);
            keyValues.put("期刊名称", "高等数学A");
            keyValues.put("类别", "数学");
            keyValues.put("级别", "高");
            keyValues.put("网站", "http://www.math.com");

            final String sheetKey = "test:" + id;
            sheetRepository.setRow(sheetKey, keyValues);
        }

        {
            Map<String, Object> keyValues = new HashMap<>();
            final long id = 1003L;
            keyValues.put("id", id);
            keyValues.put("期刊名称", "高等数学B");
            keyValues.put("类别", "数学");
            keyValues.put("级别", "高");
            keyValues.put("网站", "http://www.mathb.com");

            final String sheetKey = "test:" + id;
            sheetRepository.setRow(sheetKey, keyValues);
        }

        final List<Map<String, Object>> rows = sheetRepository.getRows("test");
        rows.forEach(System.out::println);
    }
}