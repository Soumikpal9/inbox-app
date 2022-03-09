package com.kitz.messaging.controllers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.kitz.messaging.email.EmailService;
import com.kitz.messaging.emailList.EmailListItem;
import com.kitz.messaging.emailList.EmailListItemRepository;
import com.kitz.messaging.folders.Folder;
import com.kitz.messaging.folders.FolderRepository;
import com.kitz.messaging.folders.FolderService;
import com.kitz.messaging.folders.UnreadEmailStats;
import com.kitz.messaging.folders.UnreadEmailStatsRepository;

@Controller
public class InboxController {

	private FolderRepository folderRepository;
	
	private FolderService folderService;
	
	private EmailListItemRepository emailListItemRepository;
	
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
	public void setEmailListItemRepository(EmailListItemRepository emailListItemRepository) {
		this.emailListItemRepository = emailListItemRepository;
	}
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@GetMapping("/")
	public String homePage(@RequestParam(required = false) String folder, @AuthenticationPrincipal OAuth2User principal, Model model) {
		
		if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return "index";
		}
		
		//Fetch User ID
		String userId = principal.getAttribute("login");
		String userName = principal.getAttribute("name");
		model.addAttribute("user", userName);
		
		//Fetch Folders
		List<Folder> userFolders = this.folderRepository.findAllById(userId);
		model.addAttribute("userFolders", userFolders);
		List<Folder> defaultFolders = this.folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);
		
		//Fetch Emails
		if(!StringUtils.hasText(folder)) {
			folder = "Inbox";
		}
		
		List<EmailListItem> emailList = this.emailListItemRepository.findAllByKey_IdAndKey_Label(userId, folder);
		PrettyTime p = new PrettyTime();
		for(EmailListItem emailItem : emailList) {
			UUID timeUuid = emailItem.getKey().getTimeuuid();
			Date emailDateTime = new Date(Uuids.unixTimestamp(timeUuid));
			emailItem.setAgoTimeString(p.format(emailDateTime));
			if(folder.equals("Inbox")) {
				emailItem.setInbox(true);
			}
			else {
				emailItem.setInbox(false);
			}
		}
		model.addAttribute("emailList", emailList);
		model.addAttribute("folderName", folder);
		
		model.addAttribute("unreadCount", this.folderService.mapCountToLabel(userId));
		
		return "inbox-page";
		
	}
	
}
