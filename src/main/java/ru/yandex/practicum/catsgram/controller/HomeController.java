package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String homePage() {
        return "Приветствуем вас, в приложении Котограм";
    }
}
