package org.cyberslavs.parser.dto;

import org.cyberslavs.parser.entity.Customer;
import org.cyberslavs.parser.entity.Tender;
import org.cyberslavs.parser.repo.CustomerRepository;
import org.cyberslavs.parser.repo.TenderRepository;

public class TenderDto {
    TenderRepository tenderRepository;
    CustomerRepository customerRepository;
    public Tender create(String name, String code, String address, String dateStart, String datePublish, String price){
        Tender tender = tenderRepository.save(new Tender(name, code, address, dateStart, datePublish, price));
        return tender;
    }
}
