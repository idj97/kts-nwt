package com.mbooking.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
public class LocationRepositoryIntegrationTest {

    @Test
    public void test() {
        assertEquals(1,1);
    }
}
