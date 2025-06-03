package org.sid.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.sid.springboot.exception.ResourceNotFoundException;
import org.sid.springboot.model.Employee;
import org.sid.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value="/api/v1/*")
public class EmployeeController {
	@Autowired
	private EmployeeRepository employRepo;

	//get employees
	@GetMapping(path = "employees")
	public List<Employee> getEmployees(){
		return this.employRepo.findAll();
	}
	
	//get employee by id
	@GetMapping("employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable(value="id") Long id) throws ResourceNotFoundException{
		
		Employee empl = employRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found for this id: "+id));
		return ResponseEntity.ok().body(empl);
		
	}
	//Save employee
	@PostMapping("employees")
	public Employee saveEmployee(@RequestBody Employee employ) {
		return this.employRepo.save(employ);
		
		
	}
	//update employee
	@PutMapping("employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value="id") Long id, @RequestBody Employee employDetails) throws ResourceNotFoundException {
		Employee empl = employRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found for this id: "+id));
		empl.setNom(employDetails.getNom());
		empl.setPrenom(employDetails.getPrenom());
		empl.setEmail(employDetails.getEmail());
		
		return ResponseEntity.ok().body(this.employRepo.save(empl));
		
	}
	//Delete employee
	@DeleteMapping("employees/{id}")
	public Map<String, Boolean> deleteEmployee(@PathVariable(value="id") Long id) throws ResourceNotFoundException {
	
		Employee empl = employRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not found for this id"+id));
		
		this.employRepo.delete(empl);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted", Boolean.TRUE);
		
		return response;
		
	}

}
