package com.magazine.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Book {

    @Id
    private String name;
    private String level;
    private String takeOn;
    private String issueDate;
    private String issueMonth;
    private String character;
    private String price;
    private String draw;
    private String company;
    private String isbn;
    private String mailNumber;
    private String issueInterval;
    private String pages;
    private String note;

    public Book() {
    }

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTakeOn() {
        return takeOn;
    }

    public void setTakeOn(String takeOn) {
        this.takeOn = takeOn;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueMonth() {
        return issueMonth;
    }

    public void setIssueMonth(String issueMonth) {
        this.issueMonth = issueMonth;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getMailNumber() {
        return mailNumber;
    }

    public void setMailNumber(String mailNumber) {
        this.mailNumber = mailNumber;
    }

    public String getIssueInterval() {
        return issueInterval;
    }

    public void setIssueInterval(String issueInterval) {
        this.issueInterval = issueInterval;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("name", name)
                .append("level", level)
                .append("takeOn", takeOn)
                .append("issueDate", issueDate)
                .append("issueMonth", issueMonth)
                .append("character", character)
                .append("price", price)
                .append("draw", draw)
                .append("company", company)
                .append("isbn", isbn)
                .append("mailNumber", mailNumber)
                .append("issueInterval", issueInterval)
                .append("pages", pages)
                .append("note", note)
                .toString();
    }
}
