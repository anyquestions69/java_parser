package org.cyberslavs.parser.controllers;

import org.cyberslavs.parser.service.Parser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/parse")
public class ParseController {
    private Parser parser = new Parser();
    @GetMapping
    public String parse() throws InterruptedException {
        parser.parse();
        return "Successfully parsed";
    }
}
