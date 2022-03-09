package com.kitz.messaging.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kitz.messaging.emailList.EmailListItem;
import com.kitz.messaging.emailList.EmailListItemService;
import com.kitz.messaging.folders.Folder;
import com.kitz.messaging.folders.FolderService;

@Controller
public class InboxController {
	
	private FolderService folderService;
	
	private EmailListItemService emailListItemService;
	
	@Autowired
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
	}
	
	@Autowired
	public void setEmailListItemService(EmailListItemService emailListItemService) {
		this.emailListItemService = emailListItemService;
	}
	
	@GetMapping("/")
	public String homePage(@RequestParam(required = false) String folder, @AuthenticationPrincipal OAuth2User principal, Model model) {
		
		if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return "index";
		}
		
		//Fetch User ID
		String userId = principal.getAttribute("login");
		String userName = principal.getAttribute("name");
		
		//Fetch Folders
		List<Folder> userFolders = this.folderService.findAllById(userId);
		List<Folder> defaultFolders = this.folderService.fetchDefaultFolders(userId);
		
		//Fetch Emails
		if(!StringUtils.hasText(folder)) {
			folder = "Inbox";
		}
		List<EmailListItem> emailList = this.emailListItemService.findAllByIdAndLabel(userId, folder);
		
		//Fetch Email Read Condition
		
		
		model.addAttribute("userFolders", userFolders);
		model.addAttribute("defaultFolders", defaultFolders);
		model.addAttribute("user", userName);
		model.addAttribute("emailList", emailList);
		model.addAttribute("folderName", folder);
		
		model.addAttribute("unreadCount", this.folderService.mapCountToLabel(userId));
		
		return "inbox-page";
		
	}

	
	
}
