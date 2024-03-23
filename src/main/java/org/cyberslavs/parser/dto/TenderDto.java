package org.cyberslavs.parser.dto;

import org.cyberslavs.parser.entity.Customer;
import org.cyberslavs.parser.entity.Tender;
import org.cyberslavs.parser.repo.CustomerRepository;
import org.cyberslavs.parser.repo.TenderRepository;

public class TenderDto {
    TenderRepository tenderRepository;
    CustomerRepository customerRepository;
    Tender create(String name, String code, String address, String customerName){
        Customer exist = customerRepository.findByName(customerName);
        if(exist!=null){
            exist = customerRepository.save(new Customer(customerName));
        }
        Tender tender = tenderRepository.save(new Tender(name, code, address, exist));
        return tender;
    }
}
