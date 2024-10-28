package com.CS490.sakila.controller;

import com.CS490.sakila.dto.CustomerDTO;
import com.CS490.sakila.dto.RentalDTO;
import com.CS490.sakila.model.Address;
import com.CS490.sakila.model.City;
import com.CS490.sakila.model.Customer;
import com.CS490.sakila.model.Store;
import com.CS490.sakila.repository.AddressRepository;
import com.CS490.sakila.repository.CityRepository;
import com.CS490.sakila.repository.CustomerRepository;
import com.CS490.sakila.repository.PaymentRepository;
import com.CS490.sakila.repository.RentalRepository;
import com.CS490.sakila.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RentalRepository rentalRepository;

    // Add Customer
    @PostMapping("/add")
    public String addCustomer(@RequestBody CustomerDTO customerDTO) {
        City city = cityRepository.findById(customerDTO.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        Address address = new Address();
        address.setAddress(customerDTO.getAddress());
        address.setAddress2(customerDTO.getAddress2());
        address.setDistrict(customerDTO.getState());
        address.setCity(city);
        address.setPostalCode(customerDTO.getPostalCode());
        address.setPhone(customerDTO.getPhone());
        address.setLastUpdate(LocalDateTime.now());

        Address savedAddress = addressRepository.save(address);

        Store store = storeRepository.findById(customerDTO.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(savedAddress);
        customer.setStore(store);
        customer.setActive(true);
        customer.setCreateDate(LocalDateTime.now());
        customer.setLastUpdate(LocalDateTime.now());

        customerRepository.save(customer);

        return "Customer " + customer.getFirstName() + " " + customer.getLastName() + " added successfully!";
    }

    // Update Customer
    @PutMapping("/update/{customerId}")
    public String updateCustomer(@PathVariable int customerId, @RequestBody CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setLastUpdate(LocalDateTime.now());

        Address address = customer.getAddress();
        address.setAddress(customerDTO.getAddress());
        address.setAddress2(customerDTO.getAddress2());
        address.setDistrict(customerDTO.getState());
        City city = cityRepository.findById(customerDTO.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));
        address.setCity(city);
        address.setPostalCode(customerDTO.getPostalCode());
        address.setPhone(customerDTO.getPhone());
        addressRepository.save(address);

        if (customer.getStore().getStoreId() != customerDTO.getStoreId()) {
            Store store = storeRepository.findById(customerDTO.getStoreId())
                    .orElseThrow(() -> new RuntimeException("Store not found"));
            customer.setStore(store);
        }

        customerRepository.save(customer);

        return "Customer " + customer.getFirstName() + " " + customer.getLastName() + " updated successfully!";
    }

    // Delete Customer by ID
    @DeleteMapping("/delete/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        // Delete payments related to this customer
        paymentRepository.deleteByCustomerCustomerId(customerId);
       
        // Now delete the customer
        customerRepository.delete(customer);

        return "Customer " + customer.getFirstName() + " " + customer.getLastName() + " deleted successfully!";
    }

    // Retrieve Customer List with Pagination
    @GetMapping
    public Page<Customer> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    // Search Customers by Field (customerId, firstName, or lastName) with Pagination
    @GetMapping("/search")
    public Page<Customer> searchCustomers(@RequestParam String field, @RequestParam String query, Pageable pageable) {
        switch (field) {
            case "firstName":
                return customerRepository.findByFirstNameContainingIgnoreCase(query, pageable);
            case "lastName":
                return customerRepository.findByLastNameContainingIgnoreCase(query, pageable);
            case "customerId":
                try {
                    int id = Integer.parseInt(query);
                    return customerRepository.findByCustomerId(id, pageable);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid customer ID format");
                }
            default:
                throw new IllegalArgumentException("Invalid search field: " + field);
        }
    }

    // Get customer details with rental history
    @GetMapping("/{customerId}/details")
    public CustomerDTO getCustomerDetails(@PathVariable int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<RentalDTO> rentalHistory = rentalRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(rental -> new RentalDTO(
                        rental.getRentalId(),
                        rental.getInventory().getInventoryId(),
                        customerId,
                        rental.getStaff().getStaffId(),
                        rental.getRentalDate(),
                        rental.getReturnDate()
                ))
                .collect(Collectors.toList());

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setAddress(customer.getAddress().getAddress());
        customerDTO.setAddress2(customer.getAddress().getAddress2());
        customerDTO.setState(customer.getAddress().getDistrict());
        customerDTO.setCityId(customer.getAddress().getCity().getCityId());
        customerDTO.setPostalCode(customer.getAddress().getPostalCode());
        customerDTO.setPhone(customer.getAddress().getPhone());
        customerDTO.setStoreId(customer.getStore().getStoreId());
        customerDTO.setRentalHistory(rentalHistory);

        return customerDTO;
    }
}



