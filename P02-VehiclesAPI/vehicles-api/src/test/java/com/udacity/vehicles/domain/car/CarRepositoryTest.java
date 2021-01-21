package com.udacity.vehicles.domain.car;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Test
    @Order(0)
    public void it_can_create_and_save_new_car() {
        var initialCarCount = carRepository.findAll().size();
        var car = new Car();
        car.setId(1L);
        car.setCreatedAt(LocalDateTime.now());
        car.setModifiedAt(LocalDateTime.now());
        car.setCondition(Condition.NEW);
        Details details = new Details();
        details.setBody("Optical Fibre");
        details.setEngine("10 BHP");
        details.setFuelType("Petrol");
        details.setMileage(10000);
        details.setManufacturer(manufacturerRepository.findById(100).get());
        details.setModel("LXI");
        details.setEngine("RollsRoyce");
        details.setModelYear(2021);
        details.setNumberOfDoors(5);
        details.setExternalColor("Metallic Black");
        details.setProductionYear(2021);
        car.setDetails(details);
        car.setPrice("10000 USD");
        carRepository.save(car);
        assertEquals(initialCarCount + 1, carRepository.findAll().size());

    }
}
