package com.mbooking.controller;

import com.mbooking.exception.JsonTestException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@GetMapping("/guest")
	public void testUnregistered() {
		System.out.println("guest");
	}
	
	@GetMapping("/customer")
	@Secured({"ROLE_CUSTOMER"})
	public void testCustomer() {
		System.out.println("customer");
	}
	
	@GetMapping("/admin")
	@Secured({"ROLE_ADMIN"})
	public void testAdmin() {
		System.out.println("admin");
	}
	
	@GetMapping("/sys_admin")
	@Secured({"ROLE_SYS_ADMIN"})
	public void testSysAdmin() {
		System.out.println("sysadmin");
	}
}
