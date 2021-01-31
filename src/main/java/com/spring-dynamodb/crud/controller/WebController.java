package com.javasampleapproach.dynamodb.controller;

import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.dynamodb.model.Customer;
import com.javasampleapproach.dynamodb.repo.CustomerRepository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

@RestController
public class WebController {

	List<String> customerTenantTables = Arrays.asList(new String[]{ "Tenant1Customer","Tenant2Customer"});

	@Autowired
	CustomerRepository repository;

	@Autowired
	AmazonDynamoDB client;

	@Autowired
	@Qualifier("consistentRead")
	DynamoDBMapperConfig consistentRead;

	@Autowired
	DynamoDBMapper mapper;



	@RequestMapping("/delete")
	public String delete() {
		repository.deleteAll();
		return "Done";
	}

	@RequestMapping("/save")
	public String save() {
		Customer item = new Customer("JSA-1", "Jack", "Smith");
		mapper.save(item, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride("Tenant3Customer")));
		return "Done";
	}

	@RequestMapping("/findall")
	public String findAll() {
		String result = "";
		//Iterable<Customer> customers = repository.findAll();
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List< Customer > scanResult = mapper.scan(Customer.class, scanExpression, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride("Tenant3Customer")));
		for (Customer cust : scanResult) {
			result += cust.toString() + "<br>";
		}
		return result;
	}

	@RequestMapping("/findbyid")
	public Customer findById(@RequestParam("id") String id) {
		Customer result = repository.findOne(id);
		return result;
	}

	@RequestMapping("/findbylastname")
	public List< Customer > fetchDataByLastName(@RequestParam("lastname") String lastName) {
		List<Customer> list = repository.findByLastName(lastName) ;
		return list;
	}
}
