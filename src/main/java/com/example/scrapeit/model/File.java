package com.example.scrapeit.model;

import com.example.scrapeit.exception.LicenseDuplicateException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "id", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<License> licenses;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private FileData fileData;

    public File() {
    }

    public File(String name) {
        this.name = name;
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

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
        fileData.setFile(this);
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    public void addLicense(License license) {
        if (this.licenses == null) {
            licenses = new ArrayList<>();
        }
        if (!this.licenses.contains(license)) {
            this.licenses.add(license);
            license.setFile(this);
        } else {
            throw new LicenseDuplicateException("License duplication");
        }
    }
}
