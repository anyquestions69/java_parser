package org.cyberslavs.parser.repo;

import java.util.List;

import org.cyberslavs.parser.entity.Tender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TenderRepository extends JpaRepository<Tender, Long> {

    Tender findByName(String name);
    Tender findById(long id);
    List<Tender> findAllByName(String name);
    List<Tender> findByNameLike(String name);

}