package com.mbooking.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test") // -> specify properties file by name
//@TestPropertySource("classpath:application-test.properties") // -> alternative to directly specify properties file
public class ManifestationServiceIntegrationTests {

    @Test
    public void test() {

        assertEquals(1, 1);
    }
}
