package com.mbooking.repository;

import com.mbooking.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test_h2")
public class CustomerRepositoryIntegrationTest {

    @Autowired
    private CustomerRepository customerRepo;

    @Test
    public void when_findByEmail_NotExists() {
        User user = customerRepo.findByEmail("142123123");
        Assert.assertNull(user);
    }

    @Test
    public void when_findByEmail_Exists() {
        String email = "ktsnwt.customer@gmail.com";
        User user = customerRepo.findByEmail(email);
        Assert.assertNotNull(user);
        Assert.assertEquals(email, user.getEmail());
    }


}
