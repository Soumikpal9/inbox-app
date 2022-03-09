package com.kitz.messaging.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kitz.messaging.email.EmailService;
import com.kitz.messaging.folders.Folder;
import com.kitz.messaging.folders.FolderRepository;
import com.kitz.messaging.folders.FolderService;

@Controller
public class ComposeController {
	
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
	
	@GetMapping("/compose")
	public String getComposePage(
			@RequestParam(required = false) String to,
			@AuthenticationPrincipal OAuth2User principal, Model model) {
		
		if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return "index";
		}
		
		//Fetch User ID
		String userId = principal.getAttribute("login");
		String userName = principal.getAttribute("name");
		
		
		//Fetch Folders
		List<Folder> userFolders = this.folderService.findAllById(userId);
		List<Folder> defaultFolders = this.folderService.fetchDefaultFolders(userId);
		
		//Fetch Recipient IDs
		List<String> uniqueToIds = this.splitToIds(to);
		
		model.addAttribute("user", userName);
		model.addAttribute("userFolders", userFolders);
		model.addAttribute("defaultFolders", defaultFolders);
		model.addAttribute("toIds", String.join(", ", uniqueToIds));
		model.addAttribute("unreadCount", this.folderService.mapCountToLabel(userId));
		
		return "compose-page";
		
	}
	
	@PostMapping("/sendEmail")
	public ModelAndView sendEmail(
			@RequestBody MultiValueMap<String, String> formData,
			@AuthenticationPrincipal OAuth2User principal) {
		if(principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
			return new ModelAndView("redirect:/");
		}
		
		String from = principal.getAttribute("login");
		List<String> toIds = this.splitToIds(formData.getFirst("toIds"));
		String subject = formData.getFirst("subject");
		String body = formData.getFirst("body");
		
		this.emailService.sendEmail(from, toIds, subject, body);
		
		return new ModelAndView("redirect:/");
	}
	
	//To IDs Cleaning
	private List<String> splitToIds(String to){
		if(!StringUtils.hasText(to)) {
			return new ArrayList<String>();
		}
		String[] splitIds = to.split(",");
		return Arrays.asList(splitIds)
				.stream()
				.map(id -> StringUtils.trimWhitespace(id))
				.filter(id -> StringUtils.hasText(id))
				.distinct()
				.collect(Collectors.toList());
	}
	
}
