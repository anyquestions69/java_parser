package org.cyberslavs.parser.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    private String name;
    protected Customer(){}
    public Customer(String name){
        this.name=name;
    }
}
