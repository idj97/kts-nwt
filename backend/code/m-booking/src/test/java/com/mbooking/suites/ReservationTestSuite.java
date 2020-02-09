package com.mbooking.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mbooking.controller.ReservationControllerIntegrationTests;
import com.mbooking.repository.ReservationDetailsRepositoryIntegrationTests;
import com.mbooking.repository.ReservationRepositoryIntegrationTests;
import com.mbooking.service.ReservationServiceIntegrationTests;
import com.mbooking.service.ReservationServiceUnitTests;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	ReservationDetailsRepositoryIntegrationTests.class,
	ReservationRepositoryIntegrationTests.class,
	ReservationServiceUnitTests.class,
	ReservationServiceIntegrationTests.class,
	ReservationControllerIntegrationTests.class
})

public class ReservationTestSuite {

}
