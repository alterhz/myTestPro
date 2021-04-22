package com.magazine.tools.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magazine.MagazineApplication;
import com.magazine.dao.SheetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagazineApplication.class)
public class book {

    public static final String BOOK_FILE_NAME = "books.txt";
    @Autowired
    private SheetRepository sheetRepository;

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(book.class);

    @Test
    void exportBook() throws IOException {
        Map<String, String> books = new HashMap<>();
        final Set<String> bookKeys = sheetRepository.getKeys("book");
        for (String bookKey : bookKeys) {
            final Map<String, Object> row = sheetRepository.getRow(bookKey);
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonValue = objectMapper.writeValueAsString(row);
            books.put(bookKey, jsonValue);
            logger.info("读取bookKey = {}, jsonValue = {}", bookKey, jsonValue);
        }

        FileUtils.writeLines(BOOK_FILE_NAME, books.values());

        logger.info("已经写入文件： {}", BOOK_FILE_NAME);
    }

    @Test
    void importBook() throws IOException {
        final List<String> books = FileUtils.readLines(BOOK_FILE_NAME);

        for (String book : books) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final Map<String, Object> b = objectMapper.readValue(book, new TypeReference<>() {});

            if (b.containsKey("id")) {
                final String id = (String)b.get("id");
                final String sheetKey = "book:" + id;
                sheetRepository.setRow(sheetKey, b);

                logger.info("插入redis数据 bookKey = {}, jsonValue = {}", sheetKey, b);
            }
        }

        logger.info("插入redis数据完毕。共 {} 条。", books.size());
    }

}
