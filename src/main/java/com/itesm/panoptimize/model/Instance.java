package com.itesm.panoptimize.model;


import jakarta.persistence.*;

@Entity
@Table(name = "instance", indexes = {
        @Index(name = "instance_name_index", columnList = "name", unique = true)
})
public class Instance {
    @Id
    @Column(name = "instance_id", nullable = false, length = 36)
    private String id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false, foreignKey = @ForeignKey(name = "company_has_instance"))
    private Company company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

