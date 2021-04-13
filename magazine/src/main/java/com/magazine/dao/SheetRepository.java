package com.magazine.dao;

import com.magazine.constant.RedisConsts;
import com.magazine.model.SheetFieldValue;
import com.magazine.model.SheetRow;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 页签数据
 *
 * @author Ziegler
 * date 2021/4/13
 */
@Repository
public class SheetRepository {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(SheetRepository.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取页签数据，传入页签名称。
     * <p>例如获取“book:*”的所有数据行</p>
     * @param sheetName 页签名称
     * @return
     */
    public List<SheetRow> getRows(String sheetName) {
        List<SheetRow> sheetRows = new ArrayList<>();
        Set keys = redisTemplate.keys(sheetName + ":*");
        for (Object key : keys) {
            final SheetRow sheetRow = getRow((String)key);
            assert sheetRow != null : "获取SheetRow失败。key = " + key;
            sheetRows.add(sheetRow);
        }
        return sheetRows;
    }

    /**
     * 是否存在sheet行数据
     * @param sheetKey 行数据键名称
     * @return 存在返回 {@code true}
     */
    public boolean hasRow(String sheetKey) {
        return redisTemplate.hasKey(sheetKey);
    }

    /**
     * 添加 {@link SheetRow}
     * @param sheetRow 行数据
     */
    public void setRow(SheetRow sheetRow) {
        redisTemplate.opsForValue().set(sheetRow.sheetKey(), sheetRow);
    }

    /**
     * 获取 {@link SheetRow}
     * @param sheetRowKey 主键
     * @return 行数据
     */
    public SheetRow getRow(String sheetRowKey) {
        try {
            return (SheetRow)redisTemplate.opsForValue().get(sheetRowKey);
        } catch (Exception e) {
            logger.error("1. json自动转换为SheetRow对象失败。key = {}, e = {}", sheetRowKey, e.getMessage());
            // 如果是旧的数据类型，则转换一下
            try {
                final Map<String, String> oldBook = (Map) redisTemplate.opsForValue().get(sheetRowKey);
                final String id = oldBook.get(RedisConsts.ID);
                final SheetRow sheetRow = SheetRow.create("book", NumberUtils.toLong(id));
                oldBook.forEach((k, v) -> sheetRow.addFieldValue(SheetFieldValue.of(k, v)));
                return sheetRow;
            } catch (Exception e2) {
                logger.error("2. json再次转换为Map<String, String>对象失败。key = {}, e2 = {}", sheetRowKey, e2.getMessage());
            }
        }
        return null;
    }

}
