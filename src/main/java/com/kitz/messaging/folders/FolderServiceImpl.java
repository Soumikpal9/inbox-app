package com.kitz.messaging.folders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl implements FolderService {
	
	private UnreadEmailStatsRepository unreadEmailStatsRepository;
	
	private FolderRepository folderRepository;
	
	@Autowired
	public void setUnreadEmailStatsRepository(UnreadEmailStatsRepository unreadEmailStatsRepository) {
		this.unreadEmailStatsRepository = unreadEmailStatsRepository;
	}
	
	@Autowired
	public void setFolderRepository(FolderRepository folderRepository) {
		this.folderRepository = folderRepository;
	}

	@Override
	public List<Folder> fetchDefaultFolders(String userId) {
		return Arrays.asList(
			
				new Folder(userId, "Inbox", "blue"),
				new Folder(userId, "Sent", "green"),
				new Folder(userId, "Important", "red")
				
		);
	}

	@Override
	public Map<String, Long> mapCountToLabel(String userId) {
		List<UnreadEmailStats> stats = this.unreadEmailStatsRepository.findAllById(userId);
		return stats.stream().collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
	}

	@Override
	public List<Folder> findAllById(String userId) {
		return this.folderRepository.findAllById(userId);
	}

}
