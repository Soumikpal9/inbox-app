package com.kitz.messaging.folders;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;

@Table(value = "folders_by_user")
public class Folder {

	@PrimaryKeyColumn(name = "fbu_user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String id;
	
	@PrimaryKeyColumn(name = "fbu_label", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String label;
	
	@Column(value = "fbu_color")
	@CassandraType(type = Name.TEXT)
	private String color;
	
	public Folder() {}

	public Folder(String userId, String label, String color) {
		super();
		this.id = userId;
		this.label = label;
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public void setId(String userId) {
		this.id = userId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
