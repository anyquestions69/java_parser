package org.cyberslavs.parser.controllers;

import org.apache.coyote.Response;
import org.cyberslavs.parser.entity.Tender;
import org.cyberslavs.parser.repo.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tender")
public class TenderController {
    @Autowired
    TenderRepository tenderRepository;
    @GetMapping
    public List<Tender> findAll(){
        return tenderRepository.findAll();
    }
    @GetMapping("/{id}")
    public Tender findOne(@RequestParam long id){
        Tender tender = tenderRepository.findById(id);
        return tender;
    }
    @DeleteMapping("/{id}")
    public String deleteOne(@RequestParam long id){
        Tender tender = tenderRepository.findById(id);
        tenderRepository.delete(tender);
        return "Successfully deleted";
    }
}
