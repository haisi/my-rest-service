package ch.burak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.*;

/**
 * This is a Spring MVC controller for Web-Pages.
 *
 * @author Burak Kara
 */
@Controller
public class CustomerController {

    private final CustomerRepository repo;


    @Autowired
    public CustomerController(CustomerRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/index")
    String index(Model model) {
        model.addAttribute("customers", this.repo.findAll());
        return "customers";
    }

    @GetMapping("/customerForm")
    String customerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customerForm";
    }

    @PostMapping("/displayCustomer")
    String customerSubmit(@ModelAttribute Customer customer) {
        repo.save(customer);
        System.out.println(customer.getFirstName());

        return "display";
    }

    @GetMapping("/about")
    String about(Model model) {
        model.addAttribute("developers", Arrays.asList("Hasan Kara", "Burak"));
        return "about";
    }

    @RequestMapping("/deleteCustomer/{id}")
    String customerDelete(@PathVariable("id") Long id) {
        Customer customer = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        repo.delete(customer);
        System.out.println("Deleted");
        return "redirect:/index";
    }

    @RequestMapping("/updateCustomer/{id}")
    String update(@PathVariable("id") Long id, Customer editedCustomer) {

        Customer customer = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        customer.setFirstName(editedCustomer.getFirstName());

        repo.save(customer);
        return "redirect:/index";
    }

    @RequestMapping("/editCustomer/{id}")
    String customerEdit(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Customer customer = repo.findById(id).orElseThrow(ResourceNotFoundException::new);
        model.addAttribute("customer", customer);
        redirectAttributes.addFlashAttribute("customer", customer);
        return "redirect:/editCustomer";
    }

    @RequestMapping("/editCustomer")
    String customerEditRedirect(Customer customer, Model model){
        model.addAttribute("customer",customer);
        return "editCustomer";
    }

    @RequestMapping("/findCustomer")
    String findCustomer(@RequestParam("searchName") String searchName, Model model) {

        System.out.println(searchName);

        List<Customer> allCustomers = repo.findAll();
        List<Customer> allContains = new ArrayList<>();

        for (Customer customer : allCustomers) {
            if (customer.getFirstName().contains(searchName)) {
                allContains.add(customer);
            }
        }


//        List<Customer> customers = repo.findAllByFirstName(searchName);
//        document.getElementById("demo").innerHTML = "You wrote :" + x;

        model.addAttribute("customers", allContains);
        return "customers";
    }

}
