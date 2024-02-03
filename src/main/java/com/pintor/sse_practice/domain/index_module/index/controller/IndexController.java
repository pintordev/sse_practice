package com.pintor.sse_practice.domain.index_module.index.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping(value = "")
    public String index() {
        return "index/index";
    }
}
