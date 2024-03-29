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
     private String dateStart;
     private String datePublish;
     private String dateEnd;
     private String price;
    protected Tender() {}

    public Tender(String name, String code, String dateEnd, String dateStart, String datePublish, String price) {
        this.name = name;
        this.code = code;
        this.dateStart=dateStart;
        this.datePublish=datePublish;
        this.price=price;
        this.dateEnd=dateEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDatePublish() {
        return datePublish;
    }

    public void setDatePublish(String datePublish) {
        this.datePublish = datePublish;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}