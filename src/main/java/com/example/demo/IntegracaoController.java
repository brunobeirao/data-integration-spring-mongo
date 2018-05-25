package com.example.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class IntegracaoController {
	
	private CompanyRepository repository;

	private static final Logger LOGGER = Logger.getLogger(IntegracaoController.class.getName());
	public static String pathClientDataCsv = "src/main/csv/q2_clientData.csv";
	public static String pathCatalogCsv = "src/main/csv/q1_catalog.csv";
	public static final String CONSTANT_RETURN = "Data has already been loaded or occurred another error";
	public static final String CONSTANT_BD_OK = "Database load completed";
	public static final String CONSTANT_INTEGRATOR_OK = "Integration completed";
	public static final String CONSTANT_ERROR_FILE = "Error loading file.";
	public static final String CONSTANT_ERROR_REQUEST = "Error in request construction";
	public String cvsSplitBy = ",";
	public String splitBy = ";";

    public IntegracaoController(CompanyRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/loader")
	public ResponseEntity<String> loader() {
		String csvFile = pathCatalogCsv;
		BufferedReader br = null;
		String line = "";
		int lineNumber = 0;
		String[] load = null;

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				if (lineNumber != 0) {
					load = line.split(cvsSplitBy);
					for (String loadString : load) {
						String[] column = loadString.split(splitBy);
						List<Company> listCustomer;
						if (column.length == 1) {
							listCustomer = this.repository.findByName(column[0]);
						} else {
							listCustomer = this.repository.findByNameAndZip(column[0], column[1]);
						}
						if (listCustomer.isEmpty()) {
							Company newCustomer;
							if (column.length == 1) {
								newCustomer = new Company(column[0], null, null);
							} else {
								newCustomer = new Company(column[0], column[1], null);
							}
							repository.insert(newCustomer);
						}
					}
				}
				lineNumber++;
			}
			br.close();
		} catch (Exception e) {
			LOGGER.log(Level.FINE, CONSTANT_ERROR_FILE, e);
			return new ResponseEntity<>(CONSTANT_RETURN, HttpStatus.BAD_GATEWAY);
		}
		return new ResponseEntity<>(CONSTANT_BD_OK, HttpStatus.OK);
	}
    
	@GetMapping("/integrator")
	public ResponseEntity<String> integrator() {
		String csvFile = pathClientDataCsv;
		BufferedReader br = null;
		String line = "";
		int lineNumber = 0;
		String[] load = null;

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				if (lineNumber != 0) {
					load = line.split(cvsSplitBy);
					for (String loadString : load) {
						String[] column = loadString.split(splitBy);
						List<Company> listCustomer = this.repository.findByName(column[0]);
						if (!listCustomer.isEmpty()) {
							for (Company customer : listCustomer) {
								if (!customer.zip.equals(column[1]) && customer.getId().equals(null)) {
									Company newCustomer = new Company(column[0], column[1], column[2]);
									repository.insert(newCustomer);
								} else if (customer.zip.equals(column[1]) && customer.getId().equals(null)) {
									repository.delete(customer);
									Company newCustomer = new Company(column[0], column[1], column[2]);
									repository.insert(newCustomer);
								}
							}
						} else {
							Company newCustomer = new Company(column[0], column[1], column[2]);
							repository.insert(newCustomer);
						}
					}
				}
				lineNumber++;
			}
			br.close();
		} catch (IOException e) {
			LOGGER.log(Level.FINE, CONSTANT_ERROR_FILE, e);
		}
		return new ResponseEntity<>(CONSTANT_INTEGRATOR_OK, HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Company>> search(@RequestParam(value = "name", required = false) String names,
			@RequestParam(value = "zip", required = false) String zips) {
		List<Company> back = null;
		try {
			if (names.isEmpty()) {
				back = repository.findByZip(zips);
			} else if (zips.isEmpty()) {
				back = repository.findByNameLike(names);
			} else {
				back = repository.findByNameAndZip(names, zips);
			}
		} catch (Exception e) {
			LOGGER.log(Level.FINE, CONSTANT_ERROR_REQUEST, e);
			return new ResponseEntity<>(back, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(back, HttpStatus.OK);
	}
}
