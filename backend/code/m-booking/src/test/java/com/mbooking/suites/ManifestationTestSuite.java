package com.mbooking.suites;

import com.mbooking.controller.ManifestationControllerIntegrationTests;
import com.mbooking.repository.ManifestationDayRepositoryIntegrationTests;
import com.mbooking.repository.ManifestationRepositoryIntegrationTests;
import com.mbooking.repository.ManifestationSectionRepositoryIntegrationTests;
import com.mbooking.service.ManifestationServiceIntegrationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ManifestationServiceIntegrationTests.class,
        ManifestationRepositoryIntegrationTests.class,
        ManifestationDayRepositoryIntegrationTests.class,
        ManifestationSectionRepositoryIntegrationTests.class,
        ManifestationControllerIntegrationTests.class
})
public class ManifestationTestSuite {
}
