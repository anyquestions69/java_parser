package org.cyberslavs.parser.entity;
import com.github.dockerjava.core.dockerfile.DockerfileStatement;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.Set;

@Entity
@DynamicInsert
public class Additional {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private Integer count;
    @ManyToOne
    @JoinColumn(name = "tender_id")
    private Tender tender;
    protected Additional(){}
    public Additional(String name, Integer count){
        this.name=name;
        this.count=count;
    }


    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
