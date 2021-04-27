package com.magazine.tools.convertor;

import com.magazine.MagazineApplication;
import com.magazine.dao.SheetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Set;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = MagazineApplication.class)
public class BookConvert {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(BookConvert.class);

    @Autowired
    private SheetRepository sheetRepository;

    @Test
    void run() {
        final Set<String> bookKeys = sheetRepository.getKeys("book");

        for (String bookKey : bookKeys) {
            // 页签名称
            final Map<String, Object> row = sheetRepository.getRow(bookKey);
//            sheetRepository.setRow(bookKey, row);
            logger.info("bookKey = {}, row = {}", bookKey, row);
        }

    }

}
