package com.mbooking.suites;

import com.mbooking.controller.AuthenticationControllerIntegrationTest;
import com.mbooking.repository.CustomerRepositoryIntegrationTest;
import com.mbooking.service.AuthenticationServiceIntegrationTest;
import com.mbooking.service.AuthenticationServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuthenticationServiceUnitTest.class,
        AuthenticationControllerIntegrationTest.class,
        AuthenticationServiceIntegrationTest.class,
        CustomerRepositoryIntegrationTest.class,
})
public class AuthenticationTestSuite {
}
