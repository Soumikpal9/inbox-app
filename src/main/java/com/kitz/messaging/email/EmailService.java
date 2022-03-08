package com.kitz.messaging.email;

import java.util.List;

public interface EmailService {

	void sendEmail(String from, List<String> toIds, String subject, String body);

}
