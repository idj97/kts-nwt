package com.mbooking.suites;

import com.mbooking.controller.LocationControllerIntegrationTest;
import com.mbooking.repository.LocationRepositoryIntegrationTest;
import com.mbooking.service.LocationServiceIntegrationTest;
import com.mbooking.service.LocationServiceUnitTest;
import com.mbooking.service.PaymentServiceIntegrationTest;
import com.mbooking.service.PaymentServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PaymentServiceIntegrationTest.class,
        PaymentServiceUnitTest.class,
})
public class PaymentTestSuite {
}
