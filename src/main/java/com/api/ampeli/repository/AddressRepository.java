package com.api.ampeli.repository;

import com.api.ampeli.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCity(String city);
    List<Address> findByState(String state);
    List<Address> findByZipCode(String zipCode);
    List<Address> findByCityAndState(String city, String state);
}
