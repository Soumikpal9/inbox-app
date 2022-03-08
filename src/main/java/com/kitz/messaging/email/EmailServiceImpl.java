package com.kitz.messaging.email;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.kitz.messaging.emailList.EmailListItem;
import com.kitz.messaging.emailList.EmailListItemKey;
import com.kitz.messaging.emailList.EmailListItemRepository;

@Service
public class EmailServiceImpl implements EmailService {
	
	private EmailRepository emailRepository;
	
	private EmailListItemRepository emailListItemRepository; 
	
	@Autowired
	public void setEmailRepository(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}
	
	@Autowired
	public void setEmailListItemRepository(EmailListItemRepository emailListItemRepository) {
		this.emailListItemRepository = emailListItemRepository;
	}

	@Override
	public void sendEmail(String from, List<String> toIds, String subject, String body) {
		
		Email email = new Email();
		email.setId(Uuids.timeBased());
		email.setFrom(from);
		email.setTo(toIds);
		email.setSubject(subject);
		email.setBody(body);
		this.emailRepository.save(email);
		
		for(String recipient : toIds) {
			EmailListItem emailItem = this.createEmailItemForOwner(toIds, subject, email, recipient, "Inbox");
			this.emailListItemRepository.save(emailItem);
		}
		
		EmailListItem sentItem = this.createEmailItemForOwner(toIds, subject, email, from, "Sent");
		this.emailListItemRepository.save(sentItem);
		
	}

	private EmailListItem createEmailItemForOwner(List<String> toIds, String subject, Email email, String itemOwner,
			String folder) {
		EmailListItemKey key = new EmailListItemKey();
		key.setId(itemOwner);
		key.setLabel(folder);
		key.setTimeuuid(email.getId());
		
		EmailListItem item = new EmailListItem();
		item.setKey(key);
		item.setSubject(subject);
		item.setTo(toIds);
		item.setUnread(true);
		
		return item;
	}
	
	
	
}
