package org.cyberslavs.parser.repo;

import java.util.List;

import org.cyberslavs.parser.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenderRepository extends JpaRepository<Tender, Long> {

    Tender findByName(String name);
    Tender findById(long id);

}