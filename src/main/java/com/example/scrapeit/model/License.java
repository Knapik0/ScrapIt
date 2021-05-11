package com.example.scrapeit.model;

import javax.persistence.*;

@Entity
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String licenseNumber;

    private String lastName;

    private String firstName;

    private String middleName;

    private String city;

    private String state;

    private String status;

    private String issueDate;

    private String expirationDate;

    private String boardAction;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    private File file;

    public License(String licenseNumber, String lastName, String firstName, String middleName, String city, String state, String status, String issueDate, String expirationDate, String boardAction) {
        this.licenseNumber = licenseNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.city = city;
        this.state = state;
        this.status = status;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.boardAction = boardAction;
    }

    public License() {
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getBoardAction() {
        return boardAction;
    }

    public void setBoardAction(String boardAction) {
        this.boardAction = boardAction;
    }
}
