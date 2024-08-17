package com.example.hogwarts.controllers;

import com.example.hogwarts.services.InfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/port")
//@Profile("!test")
public class InfoController {

    private final InfoService infoService;
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }



    @GetMapping
    public String getPort() {
        return infoService.getPort();
    }


}
