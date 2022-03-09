package com.kitz.messaging.emailList;

import java.util.List;

public interface EmailListItemService {

	List<EmailListItem> findAllByIdAndLabel(String userId, String folder);

}
