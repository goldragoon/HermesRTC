package org.hermesrtc.videochat;

import javax.persistence.*;  
import java.util.Set;  
  
@Entity  
public class Role {  
  
    @Id  
    @GeneratedValue(strategy = GenerationType.AUTO)  
    private long id;  
  
    private String role;  
  
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)  
    private Set<Developer> developers;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<Developer> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<Developer> developers) {
		this.developers = developers;
	}  
  
    // GETTERS and SETTERS  
}  