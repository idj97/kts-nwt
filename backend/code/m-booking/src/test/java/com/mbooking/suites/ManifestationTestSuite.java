package com.mbooking.suites;

import com.mbooking.repository.ManifestationRepositoryIntegrationTests;
import com.mbooking.service.ManifestationServiceIntegrationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ManifestationServiceIntegrationTests.class,
        ManifestationRepositoryIntegrationTests.class
})
public class ManifestationTestSuite {
}
