package com.kitz.messaging;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.kitz.messaging.email.Email;
import com.kitz.messaging.email.EmailRepository;
import com.kitz.messaging.email.EmailService;
import com.kitz.messaging.emailList.EmailListItem;
import com.kitz.messaging.emailList.EmailListItemKey;
import com.kitz.messaging.emailList.EmailListItemRepository;
import com.kitz.messaging.folders.Folder;
import com.kitz.messaging.folders.FolderRepository;
import com.kitz.messaging.folders.UnreadEmailStatsRepository;

@SpringBootApplication
@Controller
public class InboxAppApplication {
	
	private FolderRepository folderRepository;
	
	private EmailService emailService;
	
	@Autowired
	public void setFolderRepository(FolderRepository folderRepository) {
		this.folderRepository = folderRepository;
	}
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public static void main(String[] args) {
		SpringApplication.run(InboxAppApplication.class, args);
	}
	
	
	/**
	 * 
	 * This is necessary to have the Spring Boot app use the astra secure bundle
	 * to connect to the database
	 */
	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}
	
	@GetMapping("/user")
	public String user(@AuthenticationPrincipal OAuth2User principal) {
		System.out.println(principal);
		return principal.getAttribute("name");
	}
	
	@PostConstruct
	public void init() {
		Folder folder1 = new Folder("Soumikpal9", "Work", "blue");
		Folder folder2 = new Folder("Soumikpal9", "Family", "green");
		Folder folder3 = new Folder("Soumikpal9", "Home", "yellow");
		folderRepository.save(folder1);
		folderRepository.save(folder2);
		folderRepository.save(folder3);
		
		for(int i = 0; i < 10; i++) {
			
			this.emailService.sendEmail("Soumikpal9", Arrays.asList("Soumikpal9"), "Hello " + i, "Still in beta! Hold tight!");
			
		}
	}

}
