package com.udacity.vehicles.domain.manufacturer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManufacturerRepositoryTest {

    @Autowired
    private ManufacturerRepository manufacturerRepository;



    @Test
    public void it_can_find_the_manufacturer_after_save_it() {

        List<Manufacturer> manufacturers = manufacturerRepository.findAll();

        var initialNumberOfManufacturers = manufacturers.size();

        Manufacturer manufacturer = new Manufacturer(110, "Vauxhall");
        manufacturerRepository.save(manufacturer);

        manufacturers = manufacturerRepository.findAll();

        //It should be one more
        assertEquals(initialNumberOfManufacturers+1, manufacturers.size());

    }
}
