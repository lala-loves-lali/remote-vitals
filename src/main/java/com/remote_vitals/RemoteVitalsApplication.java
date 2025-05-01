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

import com.remote_vitals.reportGenerator.ReportGenerator;
import java.time.LocalDateTime;
import com.remote_vitals.mailSender.EmailHandler;

@SpringBootApplication
public class RemoteVitalsApplication {

	@Autowired
	private Test test;

	@Autowired
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		EmailHandler.sendPasswordSetMail("muneebahmed115@outlook.com", "12345");

		//SpringApplication.run(RemoteVitalsApplication.class, args);

//		int[] year2017 = {170, 80, 110, 90, 90, 60, 30, 70, 40, 140, 90, 40};
//		int[] year2018 = {60, 60, 50, 40, 130, 120, 40, 90, 70, 150, 160, 110};
//		int[][] data = {year2017, year2018};
//		String[] names = {"Year 2dfs17", "Year 2027"};
//		ReportGenerator reportGenerator=new ReportGenerator("ali","sdfds","415213","541545","hdfdkjdshfjhsdfkjhdskjfhdfjhdsfjkadshlfjhdsjfhds",data,names);
//		reportGenerator.createReport();




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
