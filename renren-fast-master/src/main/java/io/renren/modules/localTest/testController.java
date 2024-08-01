package io.renren.modules.localTest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/test")
public class testController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
