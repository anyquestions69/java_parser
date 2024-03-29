package org.cyberslavs.parser.repo;

import org.cyberslavs.parser.entity.Customer;
import org.cyberslavs.parser.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByName(String name);

}
