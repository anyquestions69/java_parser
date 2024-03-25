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
    private String dateStart;
    private String datePublish;
    private String price;
    protected Tender() {}

    public Tender(String name, String code, String address, String dateStart, String datePublish, String price) {
        this.name = name;
        this.code = code;
        this.address=address;
        this.dateStart=dateStart;
        this.datePublish=datePublish;
        this.price=price;
    }
}