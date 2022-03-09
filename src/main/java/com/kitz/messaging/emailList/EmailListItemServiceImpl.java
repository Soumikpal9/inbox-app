package com.kitz.messaging.emailList;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;

@Service
public class EmailListItemServiceImpl implements EmailListItemService {
	
	private EmailListItemRepository emailListItemRepository;
	
	@Autowired
	public void setEmailListItemRepository(EmailListItemRepository emailListItemRepository) {
		this.emailListItemRepository = emailListItemRepository;
	}

	@Override
	public List<EmailListItem> findAllByIdAndLabel(String userId, String folder) {
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
		
		return emailList;
	}

}
