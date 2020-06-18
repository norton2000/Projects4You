package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 100, unique = true)
	private String nickname;
	
	@Column(nullable=false, length = 100)
	private String firstname;
	
	@Column(nullable=false, length = 100)
	private String lastname;
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
	private List<Project> ownedProjects;
	
	@ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
	private List<Project> visibleProjects;
	
	@Column(updatable = false)
	private LocalDateTime creationTimestamp;
	
	@UpdateTimestamp
	private LocalDateTime lastUpdateTimestamp;
	
	public User() {
		ownedProjects = new ArrayList<>();
		visibleProjects = new ArrayList<>();
		creationTimestamp = LocalDateTime.now();
		lastUpdateTimestamp = creationTimestamp;
	}
	
	public User(String firstname, String lastname) {
		this();
		this.firstname = firstname;
		this.lastname = lastname;
		creationTimestamp = LocalDateTime.now();
		lastUpdateTimestamp = creationTimestamp;
	}
	
	
	
	
	public void deleteSelf() {
		for (Project project : visibleProjects) {
			project.removeMember(this);
		}
		
	}
	
	
	
	
	
	
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public List<Project> getOwnedProjects() {
		return ownedProjects;
	}

	public void setOwnedProjects(List<Project> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}

	public List<Project> getVisibleProjects() {
		return visibleProjects;
	}

	public void setVisibleProjects(List<Project> visibleProjects) {
		this.visibleProjects = visibleProjects;
	}

	public LocalDateTime getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp() {
		this.lastUpdateTimestamp = LocalDateTime.now();
	}

	public LocalDateTime getCreationTimestamp() {
		return creationTimestamp;
	}

	
	public void setCreationTimestamp(LocalDateTime creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		return true;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void deleteOwnedProject(Project project) {
		this.ownedProjects.remove(project);
	}
	public void deleteVisibleProject(Project project) {
		this.visibleProjects.remove(project);
	}


}
