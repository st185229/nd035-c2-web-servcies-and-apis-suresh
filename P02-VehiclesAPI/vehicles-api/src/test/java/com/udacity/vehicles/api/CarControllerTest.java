package com.udacity.vehicles.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;


    @Autowired
    private TestRestTemplate restTemplate;


    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     *
     * @throws Exception when car creation fails in the system
     */
    @Test
    @Order(0)
    public void it_can_create_car_returns_create_status() throws Exception {
        Car car = getCar();
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     *
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    @Order(1)
    public void it_can_list_cars() throws Exception {
        /**
         * DONE: Add a test to check that the `get` method works by calling
         *   the whole list of vehicles. This should utilize the car from `getCar()`
         *   below (the vehicle will be the first in the list).
         */

        Car car = getCar();

        mvc.perform(get("/cars")
                .content(String.valueOf(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .accept(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].condition").value("USED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].details.body").value("sedan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].details.numberOfDoors").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].details.fuelType").value("Gasoline"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].details.engine").value("3.6L V6"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].details.mileage").value(32280))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].details.modelYear").value(2018));
    }

    /**
     * Tests the read operation for a single car by ID.
     *
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    @Order(2)
    public void it_can_fetch_same_saved_car() throws Exception {
        /**
         * DONE: Add a test to check that the `get` method works by calling
         *   a vehicle by ID. This should utilize the car from `getCar()` below.
         */
        Car car = getCar();
        var mvcResult = mvc.perform(get("/cars/1")
                .content(String.valueOf(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .accept(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Car storedCar = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Car.class);
        Assert.assertNotEquals(car, storedCar);

    }

    /**
     * Tests the update operation for a single car by ID.
     *
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    @Order(3)
    public void it_should_be_able_to_update_a_specific_car() throws Exception {
        /**
         * DONE: Add a test to check whether a vehicle is appropriately deleted
         *   when the `delete` method is called from the Car Controller. This
         *   should utilize the car from `getCar()` below.
         */
        // Step 1, get the car from database

        var mvcResult = mvc.perform(get("/cars/1")
                .content(String.valueOf(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .accept(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Car storedCar = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Car.class);
        //Update the stored car
        var updatedCar = updateCar(storedCar);
        //Perform an update
        mvc.perform(
                put(new URI("/cars/1"))
                        .content(json.write(updatedCar).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        //Get the updated car from DB

        mvcResult = mvc.perform(get("/cars/1")
                .content(String.valueOf(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .accept(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .andExpect(status().isOk())
                .andReturn();
        mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var updatedCarFromDB = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Car.class);

        // Check whether its udpated to Bentley
        Assert.assertNotEquals(updatedCarFromDB.getDetails().getModel(), "Bentley Continental GT");


    }

    /**
     * Tests the deletion of a single car by ID.
     *
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    @Order(4)
    public void it_should_be_able_to_delete_a_specific_car() throws Exception {
        /**
         * DONE: Add a test to check whether a vehicle is appropriately deleted
         *   when the `delete` method is called from the Car Controller. This
         *   should utilize the car from `getCar()` below.
         */
        // Step 1, get the car from database
        Car car = getCar();
        var mvcResult = mvc.perform(get("/cars/1")
                .content(String.valueOf(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .accept(MediaType.valueOf("application/x-spring-data-verbose+json")))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Car storedCar = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Car.class);
        //Assert the car stored in the repo is same as we created
        Assert.assertNotEquals(car, storedCar);

        //Step 2 Delete by passing the same car i
        mvc.perform(
                delete(new URI("/cars/1"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204));

    }

    /**
     * Creates an example Car object for use in testing.
     *
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }


    private Car updateCar(Car givenCar) {
        givenCar.setLocation(new Location(20.730610, -13.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(105, "Bentley");
        details.setManufacturer(manufacturer);
        details.setModel("Bentley Continental GT");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("626 hp @ 6000 rpm");
        details.setFuelType("Gasoline");
        details.setModelYear(2020);
        details.setProductionYear(2021);
        details.setNumberOfDoors(4);
        givenCar.setDetails(details);
        givenCar.setCondition(Condition.NEW);
        return givenCar;
    }
}