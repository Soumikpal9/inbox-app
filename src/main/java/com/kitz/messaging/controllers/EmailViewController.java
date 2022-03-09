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
import com.kitz.messaging.email.EmailRepository;
import com.kitz.messaging.email.EmailService;
import com.kitz.messaging.emailList.EmailListItem;
import com.kitz.messaging.emailList.EmailListItemKey;
import com.kitz.messaging.emailList.EmailListItemRepository;
import com.kitz.messaging.folders.Folder;
import com.kitz.messaging.folders.FolderRepository;
import com.kitz.messaging.folders.FolderService;
import com.kitz.messaging.folders.UnreadEmailStatsRepository;

@Controller
public class EmailViewController {
	
	private FolderRepository folderRepository;
	
	private FolderService folderService;
	
	private EmailRepository emailRepository;
	
	private EmailListItemRepository emailListItemRepository; 
	
	private UnreadEmailStatsRepository unreadEmailStatsRepository;
	
	private EmailService emailService;
	
	@Autowired
	public void setFolderRepository(FolderRepository folderRepository) {
		this.folderRepository = folderRepository;
	}
	
	@Autowired
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	
	@Autowired
	public void setEmailRepository(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}
	
	@Autowired
	public void setEmailListItemRepository(EmailListItemRepository emailListItemRepository) {
		this.emailListItemRepository = emailListItemRepository;
	}
	
	@Autowired
	public void setUnreadEmailStatsRepository(UnreadEmailStatsRepository unreadEmailStatsRepository) {
		this.unreadEmailStatsRepository = unreadEmailStatsRepository;
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
		
		//Fetch Folders
		List<Folder> userFolders = this.folderRepository.findAllById(userId);
		model.addAttribute("userFolders", userFolders);
		List<Folder> defaultFolders = this.folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);
		
		//Fetch Single Email
		Optional<Email> optionalEmail = this.emailRepository.findById(id);
		if(!optionalEmail.isPresent()) {
			return "inbox-page";
		}
		
		Email email = optionalEmail.get();
		String toIds = String.join(",", email.getTo());
		
		model.addAttribute("email", email);
		model.addAttribute("toIds", toIds);
		
		if(!StringUtils.hasText(folder)) {
			folder="Inbox";
		}
		
		model.addAttribute("folderName", folder);
		
		if(folder.equals("Inbox")) {
			this.emailService.updateEmailAsRead(false, userId, "Inbox", email.getId());
		}
		
		model.addAttribute("unreadCount", this.folderService.mapCountToLabel(userId));
		
		return "email-page";
		
	}
	
}
