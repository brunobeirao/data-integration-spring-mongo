package com.example.demo;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

    public List<Company> findByName(String name);
    public List<Company> findByZip(String zip);
    public void save(List<Company> firstName);
    public List<Company> insert(List<Company> firstName);
	public List<Company> findByNameLike(String names);
	public List<Company> findByNameAndZip(String name, String zip);
}