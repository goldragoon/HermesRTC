package org.hermesrtc.videochat;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AppKey {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long appKey_id; // primary key of Key Entity
	
	@Column
	private String name;
	private String value;
	/*
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp; // timestamp is updated when appKey is created
	*/
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="developer_id")
	private Developer developer;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="appKey")
	private List<CustomSignal> keys;
	
	public AppKey() {}
	public Long getAppKey_id() {
		return appKey_id;
	}

	public void setAppKey_id(Long key_id) {
		this.appKey_id = key_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	/*
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
*/
	public Developer getDeveloper() {
		return developer;
	}

	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
	
	
}
