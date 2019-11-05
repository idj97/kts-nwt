package com.mbooking.config;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationStatus;
import com.mbooking.repository.ReservationRepository;

@Configuration
@EnableAsync
@EnableScheduling
public class ScheduledConfig {
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	ReservationRepository resRep;
	
	@Async
	@Scheduled(fixedRate = 60000)
	public void CheckReservationExpiration() {
		System.out.println("Checking for expired reservations.");
		System.out.println("----------------------------------");
		
		List<Reservation> results = resRep.findByExpirationDateBefore(new Date());
		for (Reservation res : results) {
			res.setStatus(ReservationStatus.EXPIRED);
		}
		resRep.saveAll(results);
		
		System.out.println("Number of expired reservations: " + results.size());
		System.out.println("----------------------------------");
	}
}



















