package com.mbooking.suites;

import com.mbooking.controller.LocationControllerIntegrationTest;
import com.mbooking.repository.LocationRepositoryIntegrationTest;
import com.mbooking.service.LocationServiceIntegrationTest;
import com.mbooking.service.LocationServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocationServiceUnitTest.class,
        LocationControllerIntegrationTest.class,
        LocationServiceIntegrationTest.class,
        LocationRepositoryIntegrationTest.class,
})
public class LocationTestSuite {
}
