package com.magazine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 用于匿名访问表格数据，传入访问类型，不同访问类型，不同数据显示
 * <p>使用REST方式访问数据，返回json格式数据</p>
 * @author Ziegler
 * date 2021/4/2
 */

@Controller
@RequestMapping("/view")
public class VisitController {

    /**
     * 根据配置的那些字段可以访问，去获取对应的数据值。
     * @param type 配置类型名称
     * @return 字段和
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String get(@RequestParam("type") String type) {

        return "";
    }

}
