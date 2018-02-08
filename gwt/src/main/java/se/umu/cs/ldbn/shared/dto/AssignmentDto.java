package se.umu.cs.ldbn.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

// fields must be public otherwise RestyGWT does not work
public class AssignmentDto {

    public Integer id;

    public String name;

    public Date modifiedOn;

    public String xml;

    public UserDto author;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }
}
