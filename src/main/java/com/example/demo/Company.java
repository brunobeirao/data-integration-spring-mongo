package com.example.demo;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companies")
public class Company {

    @Id
    public String id;
    public String name;
    public String zip;
	public String website;
    
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Company(String name, String zip, String website) {
        this.name = name;
        this.zip = zip;
        this.website = website;
    }

    @Override
    public String toString() {
        return String.format(
                "{"+ '\"' + "id" + '\"' + ":" + '\"' + "%s" + '\"' + "," + '\"' + "name" + '\"' + ":" + '\"' + "%s" + '\"' + "," + '\"' + "zip" + '\"' + ":" + '\"' + "%s" + '\"' + "}",
                id, name, zip);
    }
}