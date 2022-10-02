package am.itspace.companyemployeespring.repository;

import am.itspace.companyemployeespring.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {



}