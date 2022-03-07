package com.kitz.messaging.emailList;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

@PrimaryKeyClass
public class EmailListItemKey {
	
	@PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String id;
	
	@PrimaryKeyColumn(name = "label", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private String label;
	
	@PrimaryKeyColumn(name = "created_timeuuid", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	@CassandraType(type = Name.TIMEUUID)
	private UUID timeuuid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public UUID getTimeuuid() {
		return timeuuid;
	}

	public void setTimeuuid(UUID timeuuid) {
		this.timeuuid = timeuuid;
	}
	
}
