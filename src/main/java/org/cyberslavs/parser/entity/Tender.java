package org.cyberslavs.parser.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;
import java.util.Set;

@DynamicInsert
@Entity
@Table(name = "tenders")
public class Tender {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
     private String code;
     @Column(name="datestart")
     private String dateStart;
    @Column(name="datepublish")
     private String datePublish;
    @Column(name="dateend")
     private String dateEnd;
     private String price;
     private String href;

     @OneToMany(mappedBy = "tender", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<Additional> additional;
    protected Tender() {
    }

    public Tender(String name, String code, String dateEnd, String dateStart, String datePublish, String price, String href) {
        this.name = name;
        this.code = code;
        this.dateStart=dateStart;
        this.datePublish=datePublish;
        this.price=price;
        this.dateEnd=dateEnd;
        this.href = href;
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

    public String getHref() {
        return href;
    }

    public List<Additional> getAdditional() {
        return additional;
    }

    public void setAdditional(List<Additional> additional) {
        this.additional = additional;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}