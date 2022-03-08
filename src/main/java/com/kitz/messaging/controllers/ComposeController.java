package com.kitz.messaging.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kitz.messaging.folders.Folder;
import com.kitz.messaging.folders.FolderRepository;
import com.kitz.messaging.folders.FolderService;

@Controller
public class ComposeController {
	
	private FolderRepository folderRepository;
	
	private FolderService folderService;
	
	@Autowired
	public void setFolderRepository(FolderRepository folderRepository) {
		this.folderRepository = folderRepository;
	}
	
	@Autowired
	public void setFolderService(FolderService folderService) {
		this.folderService = folderService;
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
		model.addAttribute("user", userName);
		
		//Fetch Folders
		List<Folder> userFolders = this.folderRepository.findAllById(userId);
		model.addAttribute("userFolders", userFolders);
		List<Folder> defaultFolders = this.folderService.fetchDefaultFolders(userId);
		model.addAttribute("defaultFolders", defaultFolders);
		
		//To IDs Cleaning
		if(StringUtils.hasText(to)) {
			String[] splitIds = to.split(",");
			List<String> uniqueToIds = Arrays.asList(splitIds)
										.stream()
										.map(id -> StringUtils.trimWhitespace(id))
										.filter(id -> StringUtils.hasText(id))
										.distinct()
										.collect(Collectors.toList());
			
			model.addAttribute("toIds", String.join(", ", uniqueToIds));
		}
		
		return "compose-page";
		
	}
	
}
