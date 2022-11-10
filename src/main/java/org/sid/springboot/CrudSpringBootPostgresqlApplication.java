package org.sid.springboot;

import org.sid.springboot.model.Employee;
import org.sid.springboot.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//@ComponentScan("org.sid.springboot.repository.EmployeeRepository")
public class CrudSpringBootPostgresqlApplication implements CommandLineRunner{
	
	@Autowired
	EmployeeRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(CrudSpringBootPostgresqlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Employee data = new Employee("Mamadou","Dia", "m.dia@gmail.com");
////	"Zeus2000", "zal@Keurgui"
	repository.save(data);
	repository.findAll().forEach(p->{
	System.out.println(p);
	});
	}

}
