package com.campus.delivery;  // 注意：与启动类同一个包

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/simple")
public class SimpleTestController {

    @GetMapping("/hello")
    public String hello() {
        System.out.println("=== SimpleTestController.hello() 被调用 ===");
        return "Hello from same package";
    }
}