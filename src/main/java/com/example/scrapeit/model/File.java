package com.example.scrapeit.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<License> licenses;

    public File() {
    }

    public File(String name, List<License> licenses) {
        this.name = name;
        this.licenses = licenses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void addLicense(License license) {
        licenses.add(license);
    }
}
