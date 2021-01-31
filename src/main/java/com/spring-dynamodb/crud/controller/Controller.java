package com.javasampleapproach.dynamodb.controller;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.javasampleapproach.dynamodb.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    List<String> customerTenantTables = Arrays.asList(new String[]{ "Tenant1","Tenant2","Tenant3"});

    @Autowired
    AmazonDynamoDB client;

    @Autowired
    @Qualifier("consistentRead")
    DynamoDBMapperConfig consistentRead;

    @Autowired
    DynamoDBMapper mapper;


    @GetMapping("/")
    public ModelAndView welcomePage(){
        return new ModelAndView("wc");
    }

    @GetMapping("{tenantId}/list/")
    public ModelAndView courseList(@PathVariable("tenantId") String tenantId){
        if (tenantId == null || !customerTenantTables.contains(tenantId)){
            tenantId = "Tenant1";
        }
        String table = tenantId+"Customer";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Customer> customers = mapper.scan(Customer.class, scanExpression, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(table)));
        ModelAndView model =new ModelAndView();
        model.addObject("customers", customers);
        model.addObject("tenantId", tenantId);
        String hrefAddCustomerPage = "/"+tenantId+"/addCustomerPage";
        model.addObject("hrefAddCustomerPage", hrefAddCustomerPage);
        model.setViewName("customer_list");
        return model;
    }

    @GetMapping("/{tenantId}/addCustomerPage")
    public ModelAndView addCoursePage(@PathVariable("tenantId") String tenantId){
        Customer customer = new Customer();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("customerForm",customer);
        modelAndView.addObject("tenantId", tenantId);
        String  addCustomer= "/"+tenantId+"/addCustomer";
        modelAndView.addObject("addCustomer", addCustomer);
        modelAndView.setViewName("form");
        return modelAndView;
    }

    @PostMapping("{tenantId}/addCustomer")
    public ModelAndView addCustomer(@ModelAttribute("customerForm") Customer customer, @PathVariable("tenantId") String tenantId ){
        String table = tenantId+"Customer";
        mapper.save(customer, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(table)));
        String redirectUrl = tenantId+"/list/";
        return new ModelAndView("redirect:/"+ redirectUrl);
    }

    @GetMapping("/{tenantId}/deleteCustomer/{id}")
    public ModelAndView deleteCourse(@PathVariable("tenantId") String tenantId, @PathVariable("id") String id){
        String table = tenantId+"Customer";
        Customer customer = mapper.load(Customer.class, id, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(table)));
        mapper.delete( customer, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(table)));
        String redirectUrl = tenantId+"/list/";
        return new ModelAndView("redirect:/"+ redirectUrl);
    }

    @GetMapping("/{tenantId}/updateCustomerPage/{id}")
    public ModelAndView updateCourse(@PathVariable("tenantId") String tenantId, @PathVariable("id") String id){
        String table = tenantId+"Customer";
        Customer customer = mapper.load(Customer.class, id, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(table)));
        ModelAndView model =new ModelAndView();
        model.addObject("customerForm", customer);
        model.addObject("tenantId", tenantId);
        String  updateCustomer= "/"+tenantId+"/updateCustomer";
        model.addObject("updateCustomer", updateCustomer);
        model.setViewName("update_form");
        return model;
    }


    @PostMapping("{tenantId}/updateCustomer")
    public ModelAndView updateCustomer(@ModelAttribute("customerForm") Customer customer, @PathVariable("tenantId") String tenantId ){
        String table = tenantId+"Customer";
        mapper.save(customer, new DynamoDBMapperConfig(new DynamoDBMapperConfig.TableNameOverride(table)));
        String redirectUrl = tenantId+"/list/";
        return new ModelAndView("redirect:/"+ redirectUrl);
    }

}
