package com.example.scrapeit.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class FileData {

    @Id
    private Long fileId;

    private String fileName;

    private int numRows;

    private Date createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private File file;

    public FileData(String fileName, int numRows, Date createdAt, File file) {
        this.fileName = fileName;
        this.numRows = numRows;
        this.createdAt = createdAt;
        this.file = file;
    }

    public FileData() {

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
