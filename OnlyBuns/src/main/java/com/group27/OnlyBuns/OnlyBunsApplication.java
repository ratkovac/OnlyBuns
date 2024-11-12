package com.group27.OnlyBuns;

import com.group27.OnlyBuns.model.User;
import com.group27.OnlyBuns.service.EmailSenderService;
import com.group27.OnlyBuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@SpringBootApplication
@EnableScheduling
public class OnlyBunsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlyBunsApplication.class, args);
	}

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	UserService userService;

	@EventListener(ApplicationReadyEvent.class)
	public void sendEmail() {
		//emailSenderService.sendEmail("nik.letvencuk@gmail.com", "Verifikacija OnlyBuns profila","Vas kod za verifikaciju je KOD");
		/*System.out.println("Pokrenuo se sendEmail");
		User user = new User();
		user.setUsername("user1");
		user.setPassword("password");
		user.setName("user1");
		user.setSurname("surname");
		user.setRole("user");
		user.setActive(false);
		user.setEmail("nik.letvencuk@gmail.com");
		user.setAddress("Adresa1");
		userService.registerUser(user);*/
	}



}
