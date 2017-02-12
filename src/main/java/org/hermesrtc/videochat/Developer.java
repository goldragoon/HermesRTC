package org.hermesrtc.videochat;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
@Entity
public class Developer {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long developer_id;
	
	@Column
	private String id;
	private String pw;
	private int enabled;
	

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)  
    @JoinTable(joinColumns = @JoinColumn(name = "developer_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))  
    private Set<Role> roles;  
  
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="developer")
	private List<AppKey> keys;

	public Developer() {}

	public Long getDeveloper_id() {
		return developer_id;
	}

	public void setDeveloper_id(Long developer_id) {
		this.developer_id = developer_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	
}
