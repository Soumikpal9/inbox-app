package com.kitz.messaging.folders;

import java.util.List;

public interface FolderService {
	
	List<Folder> fetchDefaultFolders(String userId);
	
}
