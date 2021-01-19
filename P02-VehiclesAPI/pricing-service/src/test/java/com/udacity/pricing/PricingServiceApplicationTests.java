package com.udacity.pricing;

import com.google.gson.Gson;
import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PricingServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PriceRepository priceRepository;

	@Test
	@Order(1)
	public void contextLoads() {
	}

	@Test
	@Order(2)
	public void integration_test_get_that_a_specific_price_return_1_value() {
		var response =
				this.restTemplate.getForEntity("http://localhost:" + port + "/prices/1", Price.class);

		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getBody().getCurrency(),equalTo("USD"));
	}

	@Test
	@Order(3)
	public void integration_test_get_all_prices() {
		var response =
				this.restTemplate.getForEntity("http://localhost:" + port + "/prices", String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

	}

	@Test
	@Order(4)
	public void unit_test_price_repo_is_not_null(){
		assertNotNull(priceRepository);
	}
	@Test
	@Order(5)
	public void unit_test_find_all_prices(){
		assertNotNull(priceRepository);
		Iterable<Price> prices = priceRepository.findAll();
		int requiredOfPrices = 20;
		var actualPrices = StreamSupport.stream(prices.spliterator(), false).count();
		assertEquals(requiredOfPrices,actualPrices);
	}

}
