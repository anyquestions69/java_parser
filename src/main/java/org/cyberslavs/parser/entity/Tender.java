package org.cyberslavs.parser.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tenders")
public class Tender {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String code;
    private String address;
    @ManyToOne
    @JoinColumn(name="customer_id", nullable = false)
    private Customer customer;
    protected Tender() {}

    public Tender(String name, String code, String address, Customer customer) {
        this.name = name;
        this.code = code;
        this.address=address;
        this.customer = customer;
    }
}