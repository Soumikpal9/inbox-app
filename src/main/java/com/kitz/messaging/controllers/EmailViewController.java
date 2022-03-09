package com.kitz.messaging.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.kitz.messaging.email.Email;
import com.kitz.messaging.email.EmailService;
import com.kitz.messaging.folders.Folder;
import com.kitz.messaging.folders.FolderService;

@Controller
public class EmailViewController {
	
	private FolderService folderService;
	
	private EmailService emailService;
	
	@Autowired
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@GetMapping("/emails/{id}")
	public String homePage(@RequestParam(required = false) String folder, @PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {
		
		if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return "index";
		}
		
		//Fetch User ID
		String userId = principal.getAttribute("login");
		String userName = principal.getAttribute("name");
		
		//Fetch Folders
		List<Folder> userFolders = this.folderService.findAllById(userId);
		List<Folder> defaultFolders = this.folderService.fetchDefaultFolders(userId);
		
		//Fetch Single Email
		Optional<Email> optionalEmail = this.emailService.findById(id);
		if(!optionalEmail.isPresent()) {
			return "inbox-page";
		}
		Email email = optionalEmail.get();
		String toIds = String.join(",", email.getTo());
		
		//Update Email Status Read
		if(!StringUtils.hasText(folder)) {
			folder="Inbox";
		}
		if(folder.equals("Inbox")) {
			this.emailService.updateEmailAsRead(false, userId, "Inbox", email.getId());
		}
		
		model.addAttribute("user", userName);
		model.addAttribute("userFolders", userFolders);
		model.addAttribute("defaultFolders", defaultFolders);
		model.addAttribute("email", email);
		model.addAttribute("toIds", toIds);
		model.addAttribute("folderName", folder);
		model.addAttribute("unreadCount", this.folderService.mapCountToLabel(userId));
		
		return "email-page";
		
	}
	
}
