package com.englishbookshop.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.englishbookshop.entity.Customer;

public class CustomerDAOTest {
	private static CustomerDAO customerDao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		customerDao = new CustomerDAO();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		customerDao.close();
	}

	@Test
	public void testGet() {
		Integer customerId = 1;
		Customer customer = customerDao.get(customerId);
		
		assertTrue(customer != null);
	}

	@Test
	public void testCreateCustomer() {
		Customer customer = new Customer();
		customer.setEmail("minhtan@mail.com");
		customer.setFullName("Minh Tan");
		customer.setCity("Ho Chi Minh");
		customer.setAddress("123 Phan Van Hon");
		customer.setCountry("Viet Nam");
		customer.setPassword("123456");
		customer.setPhone("0906664444");
		customer.setZipCode("700000");
		
		Customer savedCustomer = customerDao.create(customer);
	
		assertTrue(savedCustomer.getCustomerId() > 0);
	}

	@Test
	public void testDeleteCustomer() {
		Integer customerId = 1;
		customerDao.delete(customerId);
		
		Customer customer = customerDao.get(customerId);
		assertNull(customer);
	}
	
	@Test
	public void testUpdateCustomer() {
		Customer customer = customerDao.get(1);
		String fullName="Thao Le";
		customer.setFullName(fullName);;
		Customer updatedCustomer = customerDao.update(customer);
		assertTrue(updatedCustomer.getFullName().contentEquals(fullName));
	}
	
	@Test
	public void testListAll() {
		List<Customer> listCustomers = customerDao.listAll();
		
		assertFalse(listCustomers.isEmpty());
	}
	
	@Test
	public void testCount() {
		long totalCustomers = customerDao.count();
		
		assertTrue(totalCustomers == 1);
	}
	
	@Test
	public void testFindByEmail() {
		String email = "minhtan@mail.com";
		Customer customer = customerDao.findByEmail(email);
		assertTrue(customer.getCustomerId() == 2);
	}
	
	@Test
	public void testUpdateNoPassword() {
		Customer customer = customerDao.get(5);
		System.out.println(customer);
		customer.setEmail("changedEmail@gmail.com");
		customer.setFullName("changed Full Name");
		customer.setPhone("1112223334");
		customer.setAddress("changed Address");
		customer.setCity("changed city");
		customer.setZipCode("changed zipcode");
		customer.setCountry("changed country");
	
		boolean result = customerDao.updateNoPassword(customer);
			
		assertTrue(result);
	}
	
	@Test
	public void testUpdate() {
		Customer customer = customerDao.get(5);
		System.out.println(customer);
		customer.setEmail("new@gmail.com");
		customer.setFullName("new Full Name");
		customer.setPhone("new phone");
		customer.setAddress("new Address");
		customer.setCity("new city");
		customer.setZipCode("new zipcode");
		customer.setCountry("new country");
		customer.setPassword("28101999");
		
		Customer updatedCus = customerDao.update(customer);
		System.out.println(updatedCus);
		assertNotNull(updatedCus);
	}

}