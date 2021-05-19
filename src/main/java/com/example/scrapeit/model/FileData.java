package com.example.scrapeit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class FileData {

    @Id
    private Long fileId;

    private String fileName;

    private int numRows;

    private Date createdAt;

    private int duplicates;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    @MapsId
    private File file;

    public FileData(String fileName, int numRows, int duplicates, Date createdAt) {
        this.fileName = fileName;
        this.numRows = numRows;
        this.duplicates = duplicates;
        this.createdAt = createdAt;
    }

    public FileData() {

    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
