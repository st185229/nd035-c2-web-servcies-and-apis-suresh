package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test_that_the_price_api_works() throws Exception{

        var response = restTemplate.getForEntity("http://localhost:" + port + "/prices/", List.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    }
    @Test
    public void get_that_a_specific_price_return_1_value() {
        var response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/prices/1", Price.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }






}
