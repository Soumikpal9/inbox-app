package com.kitz.messaging.emailList;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailListItemRepository extends CassandraRepository<EmailListItem, EmailListItemKey> {

	List<EmailListItem> findAllByKey_IdAndKey_Label(String id, String label);
	
}
