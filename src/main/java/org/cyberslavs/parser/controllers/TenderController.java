package org.cyberslavs.parser.controllers;

import org.apache.coyote.Response;
import org.cyberslavs.parser.entity.Tender;
import org.cyberslavs.parser.repo.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/tender")
public class TenderController {
    @Autowired
    TenderRepository tenderRepository;
    @GetMapping()
    public List<Tender> findByName(@RequestParam(name="name", required = false) String name){


        String[] keywords={"метал", "сталь",
                "нефтегаз", "арматур",
                "прокат", "уголок",
                "проволо", "крепеж",
                "гвозд", "профил",
                "чугун", "шлак",
                "желез","руд",
                "труб"
        };
        if(name==null){
            List<Tender>first=new ArrayList<>();
            for(int i=0;i<keywords.length;i++){
                List<Tender> td = tenderRepository.findByNameLike("%"+keywords[i]+"%");
                for(Tender tender:td){
                    first.add(tender);
                    System.out.println(tender.getAdditional());
                }
            }
            return first;
        }
        System.out.println(name);
        name=name.toLowerCase(Locale.ROOT);
        return tenderRepository.findByNameLike("%"+name+"%");
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
