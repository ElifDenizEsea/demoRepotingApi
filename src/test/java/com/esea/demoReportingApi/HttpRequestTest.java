package com.esea.demoReportingApi;

import com.esea.model.User;
import com.esea.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


        import com.jayway.jsonpath.JsonPath;
        import org.junit.Assert;
        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.mockito.Mock;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.boot.test.web.client.TestRestTemplate;
        import org.springframework.boot.web.server.LocalServerPort;
        import org.springframework.test.context.junit4.SpringRunner;

        import java.util.stream.Stream;

        import static org.assertj.core.api.Assertions.assertThat;
        import static org.hamcrest.Matchers.hasSize;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
/*
    @Test
    public void shouldReturnPersonDetails() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith/mary",
                        String.class
                )
        ).contains("Mary");
    }

    @Test
    public void shouldReturnPersonList() throws Exception {
        assertThat(
                this.restTemplate.getForObject(
                        "http://localhost:" + port + "/person/smith",
                        String.class
                )
        ).contains("Brian");

    }
*/
    @Autowired
    private UserService userService;
    @Test
    public void login() throws Exception {
        User u=new User("elif","deniz");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(u );
        assertThat(
                this.restTemplate.postForObject(
                        "http://localhost:" + port + "api/v1/merchant/user/login", u, String.class)).contains("token");
    }


}