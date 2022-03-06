package com.kitz.messaging.folders;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl implements FolderService {

	@Override
	public List<Folder> fetchDefaultFolders(String userId) {
		return Arrays.asList(
			
				new Folder(userId, "Inbox", "blue"),
				new Folder(userId, "Sent", "green"),
				new Folder(userId, "Important", "red")
				
		);
	}

}
