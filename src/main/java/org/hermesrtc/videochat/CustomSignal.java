package org.hermesrtc.videochat;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CustomSignal {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long customSignal_id; // primary key of Key Entity
	
	public Long getCustomSignal_id() {
		return customSignal_id;
	}

	public void setCustomSignal_id(Long customSignal_id) {
		this.customSignal_id = customSignal_id;
	}

	@Column
	private String name;
	private String value;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="appKey_id")
	private AppKey appKey;

	
	public AppKey getAppKey() {
		return appKey;
	}

	public void setAppKey(AppKey appKey) {
		this.appKey = appKey;
	}

	public CustomSignal() {}
	
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
}
