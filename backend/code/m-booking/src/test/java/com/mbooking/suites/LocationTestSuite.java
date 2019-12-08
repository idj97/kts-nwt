package com.mbooking.suites;

import com.mbooking.controller.LocationControllerUnitTest;
import com.mbooking.repository.LocationRepositoryIntegrationTest;
import com.mbooking.service.ManifestationServiceIntegrationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocationRepositoryIntegrationTest.class,
        LocationControllerUnitTest.class
})

public class LocationTestSuite {
}
