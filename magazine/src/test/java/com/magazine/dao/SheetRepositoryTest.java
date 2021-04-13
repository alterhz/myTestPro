package com.magazine.dao;

import com.magazine.MagazineApplication;
import com.magazine.model.SheetFieldValue;
import com.magazine.model.SheetRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension. class)
@SpringBootTest(classes = MagazineApplication.class)
class SheetRepositoryTest {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SheetRepositoryTest.class);

    @Autowired
    private SheetRepository sheetRepository;

    @Test
    void addRowAndGet() {
        SheetRow sheetRow = SheetRow.create("test", 1001L);
        sheetRow.addFieldValue(SheetFieldValue.of("id", 1000L));
        sheetRow.addFieldValue(SheetFieldValue.of("期刊名称", "上下五千年"));

        if (sheetRepository.hasRow(sheetRow.sheetKey())) {
            logger.warn("SheetRow已存在。 sheetRow = {}", sheetRow);
        }

        sheetRepository.setRow(sheetRow);

        final SheetRow row = sheetRepository.getRow(sheetRow.sheetKey());
        if (row != null) {
            logger.info("row = " + row);
        }
    }

    @Test
    void testGetRows() {
        for (int i = 0; i < 3; i++) {
            final long id = 1001L + i;
            SheetRow sheetRow = SheetRow.create("test", id);
            sheetRow.addFieldValue(SheetFieldValue.of("id", id));
            sheetRow.addFieldValue(SheetFieldValue.of("期刊名称", "上下五千年"));
            sheetRepository.setRow(sheetRow);
        }

        final List<SheetRow> rows = sheetRepository.getRows("test");
        rows.forEach(System.out::println);
    }
}