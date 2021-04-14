package com.magazine.dao;

import com.magazine.MagazineApplication;
import com.magazine.constant.RedisConsts;
import com.magazine.model.SchemaField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = MagazineApplication.class)
@ExtendWith(SpringExtension.class)
class SchemaFieldRepositoryTest {

    @Autowired
    private SchemaFieldRepository schemaFieldRepository;

    @Test
    void testGetSchemaFields() {
        final List<SchemaField> schemaFields = schemaFieldRepository.getSchemaFields(RedisConsts.BOOK_SCHEMA_KEY);
        for (SchemaField schemaField : schemaFields) {
            System.out.println("schemaField = " + schemaField);
        }
    }

    @Test
    void saveSchemaFields() {
        final List<SchemaField> schemaFields = new ArrayList<>();
        schemaFields.add(SchemaField.of("name", "期刊名称", 0));
        schemaFields.add(SchemaField.of("category", "类别", 1));
        schemaFields.add(SchemaField.of("level", "级别", 2));
        schemaFields.add(SchemaField.of("employ", "收录", 3));
        schemaFields.add(SchemaField.of("date", "刊期", 4));
        schemaFields.add(SchemaField.of("month", "出刊月", 5));
        schemaFields.add(SchemaField.of("characters", "字符", 6));
        schemaFields.add(SchemaField.of("price", "卖价", 7));
        schemaFields.add(SchemaField.of("note", "备注", 8));

        schemaFieldRepository.saveSchemaFields(RedisConsts.BOOK_SCHEMA_KEY, schemaFields);

        final List<SchemaField> readSchemaFields = schemaFieldRepository.getSchemaFields(RedisConsts.BOOK_SCHEMA_KEY);
        for (SchemaField schemaField : readSchemaFields) {
            System.out.println("schemaField = " + schemaField);
        }
    }
}