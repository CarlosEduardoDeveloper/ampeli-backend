package com.api.ampeli.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 10)
    @Column(name = "zip_code", length = 10)
    private String zipCode; // CEP

    @Size(max = 200)
    @Column(length = 200)
    private String street; // Rua

    @Size(max = 10)
    @Column(length = 10)
    private String number; // Número

    @Size(max = 100)
    @Column(length = 100)
    private String complement; // Complemento

    @Size(max = 100)
    @Column(length = 100)
    private String city; // Cidade

    @Size(max = 50)
    @Column(length = 50)
    private String state; // Estado

    public Address(String zipCode, String street, String number, String complement, String city, String state) {
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.city = city;
        this.state = state;
    }
}
