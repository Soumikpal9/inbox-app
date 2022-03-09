package com.kitz.messaging.emailList;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailListItemRepository extends CassandraRepository<EmailListItem, EmailListItemKey> {

	List<EmailListItem> findAllByKey_IdAndKey_Label(String id, String label);
	
	@Query("update messages_by_user_folder "
			+ "set unread = ?0 "
			+ "where user_id = ?1 and label = ?2 and created_timeuuid = ?3")
	void updateEmailAsRead(boolean isUnread, String userId, String label, UUID timeUUID);
	
}
