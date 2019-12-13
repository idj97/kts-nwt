package com.mbooking.suites;

import com.mbooking.controller.LocationControllerIntegrationTest;
import com.mbooking.repository.LocationRepositoryIntegrationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocationRepositoryIntegrationTest.class,
        LocationControllerIntegrationTest.class
})

public class LocationTestSuite {
}
