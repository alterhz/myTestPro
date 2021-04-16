package com.magazine.dao;

import com.magazine.MagazineApplication;
import com.magazine.model.SheetFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension. class)
@SpringBootTest(classes = MagazineApplication.class)
class FilterRepositoryTest {
    public static final String BOOK_FILTER = "bookFilter";
    @Autowired
    private FilterRepository filterRepository;

    @Test
    void setAndGetFilter() {
        final SheetFilter sheetFilter = new SheetFilter();
        sheetFilter.setFilterName(BOOK_FILTER);
        sheetFilter.setSheetName("book");
        sheetFilter.addField("id");
        sheetFilter.addField("期刊名称");
        sheetFilter.addField("类别");
        sheetFilter.addField("级别");
        sheetFilter.addField("网站");
        filterRepository.setFilter(sheetFilter.getFilterName(), sheetFilter);

        final SheetFilter filter = filterRepository.getFilter(BOOK_FILTER);
        System.out.println("filter = " + filter);
    }

    @Test
    void setEmptyFilter() {
        final SheetFilter sheetFilter = new SheetFilter();
        sheetFilter.setFilterName(BOOK_FILTER);
        sheetFilter.setSheetName("book");
        filterRepository.setFilter(sheetFilter.getFilterName(), sheetFilter);

        final SheetFilter filter = filterRepository.getFilter(BOOK_FILTER);
        System.out.println("filter = " + filter);
    }
}