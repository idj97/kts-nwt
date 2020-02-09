package com.mbooking.suites;

import com.mbooking.controller.LayoutControllerIntegrationTest;
import com.mbooking.repository.LayoutRepositoryIntegrationTest;
import com.mbooking.service.LayoutServiceIntegrationTest;
import com.mbooking.service.LayoutServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LayoutServiceUnitTest.class,
        LayoutControllerIntegrationTest.class,
        LayoutServiceIntegrationTest.class,
        LayoutRepositoryIntegrationTest.class,
})
public class LayoutTestSuite {
}
