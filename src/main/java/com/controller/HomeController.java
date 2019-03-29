package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/20
 */
@Controller
public class HomeController {
    @RequestMapping("/pages/home")
    public String home() throws IOException {
        return "/index";
    }
}
