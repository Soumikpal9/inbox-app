package com.kitz.messaging.email;

import java.util.List;
import java.util.UUID;

public interface EmailService {

	void sendEmail(String from, List<String> toIds, String subject, String body);

	void updateEmailAsRead(boolean isUnread, String userId, String label, UUID timeUUID);

}
