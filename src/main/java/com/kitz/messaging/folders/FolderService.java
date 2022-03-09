package com.kitz.messaging.folders;

import java.util.List;
import java.util.Map;

public interface FolderService {
	
	List<Folder> fetchDefaultFolders(String userId);

	Map<String, Long> mapCountToLabel(String userId);

	List<Folder> findAllById(String userId);
	
}
