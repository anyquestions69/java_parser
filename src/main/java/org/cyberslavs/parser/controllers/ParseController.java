package org.cyberslavs.parser.controllers;

import org.cyberslavs.parser.entity.Tender;
import org.cyberslavs.parser.repo.TenderRepository;
import org.cyberslavs.parser.service.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parse")
public class ParseController {
    @Autowired
    TenderRepository tenderRepository;
    private Parser parser = new Parser();
    @GetMapping
    public String parse() throws InterruptedException {
        List<Tender> tenderList = parser.parse();
        for(Tender tender:tenderList){
            System.out.println(tender);
            tenderRepository.save(tender);
        }
        return "Successfully parsed";
    }
}
