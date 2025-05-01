package com.remote_vitals;

import com.remote_vitals.user.entities.Patient;
import com.remote_vitals.user.enums.Gender;
import com.remote_vitals.user.enums.Visibility;
import com.remote_vitals.user.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class RemoteVitalsApplication {

	@Autowired
	private Test test;

	@Autowired
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		SpringApplication.run(RemoteVitalsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("************************************************************");
			test.tester();

			System.out.println("************************************************************");

			patientRepository.save(
					Patient.builder()
							.firstName("Taimoor")
							.lastName("Ashraf")
							.gender(Gender.MALE)
							.email("tashraf.bsai24seecs@seecs.edu.pk")
							.password("12345")
							.pnVisibility(Visibility.PRIVATE)
							.eVisibility(Visibility.PUBLIC)
							.bloodGroup("A+")
							.dateOfBirth(LocalDateTime.now())
							.build()
			);
		};
	}
}
