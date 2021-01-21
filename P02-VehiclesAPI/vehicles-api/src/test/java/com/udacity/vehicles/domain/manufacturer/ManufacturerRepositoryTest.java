package com.udacity.vehicles.domain.manufacturer;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManufacturerRepositoryTest {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Test
    @Order(0)
    public void it_can_find_the_manufacturer_after_save_it() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        var initialNumberOfManufacturers = manufacturers.size();
        Manufacturer manufacturer = new Manufacturer(110, "Vauxhall");
        manufacturerRepository.save(manufacturer);
        manufacturers = manufacturerRepository.findAll();
        var manufacturerCountedAddedByOne = initialNumberOfManufacturers + 1;
        assertEquals(manufacturerCountedAddedByOne, manufacturers.size());
    }

    @Test
    @Order(1)
    public void it_can_delete_the_manufacturer_after_save_it() {
        var id = 111;
        Manufacturer manufacturer = new Manufacturer(id, "Vauxhall");
        manufacturerRepository.save(manufacturer);
        var manufacturers = manufacturerRepository.findAll();
        manufacturerRepository.deleteById(id);
        var found = manufacturerRepository.findById(id);
        assertEquals(found, Optional.empty());

    }

    @Test
    @Order(2)
    public void it_return_null_when_not_found() {
        var found = manufacturerRepository.findById(0);
        assertEquals(found, Optional.empty());
    }
}
