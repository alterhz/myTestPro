package com.magazine.dao;

import com.magazine.constant.RedisConsts;
import com.magazine.model.SchemaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SchemaFieldRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SchemaFieldRepository.class);

    /**
     * 获取表格结构列表
     * @param schemaName 表格结构名称
     * @return 表格结构列表
     */
    public List<SchemaField> getSchemaFields(String schemaName) {
        if (!redisTemplate.hasKey(schemaName)) {
            return Collections.emptyList();
        }

        try {
            final List<SchemaField> schemaFields = (List<SchemaField>) redisTemplate.opsForValue().get(schemaName);
            return schemaFields;
        } catch (Exception e) {
            logger.error("从Redis获取表格结构列表失败。schemaName = {}, e = {}", schemaName, e.getMessage());
        }

        try {
            final List<String> fields = redisTemplate.opsForList().range(schemaName, 0, -1);
            List<SchemaField> schemaFields = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) {
                final String fieldName = fields.get(i);
                schemaFields.add(SchemaField.of(fieldName, fieldName, i));
            }
            return schemaFields;
        } catch (Exception e) {
            logger.error("从Redis获取【旧的】表格结构列表失败。schemaName = {}, e = {}", schemaName, e.getMessage());
        }

        return Collections.emptyList();
    }

    /**
     * 这个方法之后需要去掉
     * @return
     */
    @Deprecated
    public List<String> getSchemaFields() {
        final List<SchemaField> schemaFields = getSchemaFields(RedisConsts.BOOK_SCHEMA_KEY);
        final List<String> fields = schemaFields.stream().map(schemaField -> schemaField.getField()).collect(Collectors.toList());
        return fields;
    }

    /**
     * 保存表格结构
     * @param schemaName 表格结构名称
     * @param schemaFields 结构字段列表详情
     */
    public void saveSchemaFields(String schemaName, List<SchemaField> schemaFields) {
        redisTemplate.opsForValue().set(schemaName, schemaFields);
    }


}
