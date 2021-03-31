package com.magazine;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestMatch {

    @Test
    void testChineseMatch() {
        List<String> list = List.of("百科论坛·教育科研", "成功·学校体音美", "大学·研究管理");
        for (String s : list) {
            if (StringUtils.contains(s, "科研")) {
                System.out.println("s = " + s);
            }
        }
    }
}
