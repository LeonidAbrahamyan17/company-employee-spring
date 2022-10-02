package am.itspace.companyemployeespring.controller;

import am.itspace.companyemployeespring.entity.Company;
import am.itspace.companyemployeespring.entity.Employee;
import am.itspace.companyemployeespring.repository.CompanyRepository;
import am.itspace.companyemployeespring.repository.EmployeeRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Value("${employee.images.folder}")
    private String folderrPath;

    @GetMapping("/employee/add")
    public String addTaskPage(ModelMap modelMap) {
        List<Company> companies = companyRepository.findAll();
        modelMap.addAttribute("companies", companies);
        return "addEmployee";
    }

    @PostMapping("/employee/add")
    public String addEmployees(@ModelAttribute Employee employee,
                               @RequestParam("employeeImage") MultipartFile file) throws IOException {
        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File newFile = new File(folderrPath + File.separator + fileName);
            file.transferTo(newFile);
            employee.setProfilePic(fileName);
        }
        Optional<Company> byId = companyRepository.findById(employee.getCompany().getId());
        employeeRepository.save(employee);
        Company company = byId.get();
        company.setSize(company.getSize() + 1);
        companyRepository.save(company);
        return "redirect:/employee";
    }

    @GetMapping("/employee")
    public String employee(ModelMap modelMap) {
        List<Employee> employees = employeeRepository.findAll();
        modelMap.addAttribute("employees", employees);
        return "employee";
    }

    @GetMapping(value = "/employee/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderrPath + File.separator + fileName);
        return IOUtils.toByteArray(inputStream);
    }

    @GetMapping("/employee/delete")
    public String delete(@ModelAttribute Employee employee, @RequestParam("id") int id) {
        Optional<Employee> byId =employeeRepository.findById(id);
        Company company = byId.get().getCompany();
        company.setSize(company.getSize()-1);
        employeeRepository.deleteById(id);
        return "redirect:/employee";
    }


}