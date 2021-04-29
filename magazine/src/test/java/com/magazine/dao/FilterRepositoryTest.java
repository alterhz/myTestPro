package com.magazine.dao;

import com.magazine.MagazineApplication;
import com.magazine.model.SheetFilter;
import com.magazine.model.ShowField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension. class)
@SpringBootTest(classes = MagazineApplication.class)
class FilterRepositoryTest {

    public static final String TEST_FILTER = "testFilter";

    @Autowired
    private FilterRepository filterRepository;

    @Test
    void getAndSet() {
        final SheetFilter sheetFilter = new SheetFilter();
        sheetFilter.setFilterName(TEST_FILTER);
        sheetFilter.setSheetName("test");
        sheetFilter.addField(ShowField.of("id", 0));
        sheetFilter.addField(ShowField.of("期刊名称", 0, true, true));
        sheetFilter.addField(ShowField.of("类别", 0, true, false));
        sheetFilter.addField(ShowField.of("级别", 0, true, false));
        sheetFilter.addField(ShowField.of("网站", 0));
        filterRepository.setFilter(sheetFilter.getFilterName(), sheetFilter);

        final SheetFilter filter = filterRepository.getFilter(TEST_FILTER);
        Assertions.assertNotEquals(filter, sheetFilter);

        filterRepository.delete(TEST_FILTER);
    }


}