package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Commento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToOne(fetch = FetchType.EAGER)
	private User leavedBy;
	
	@Column(nullable = false, length = 1000)
	private String testoCommento;

	
	public Commento() {}
	
	public Commento(User user, String testo) {
		this.leavedBy = user;
		this.testoCommento = testo;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getLeavedBy() {
		return leavedBy;
	}

	public void setLeavedBy(User leavedBy) {
		this.leavedBy = leavedBy;
	}

	public String getTestoCommento() {
		return testoCommento;
	}

	public void setTestoCommento(String testCommento) {
		this.testoCommento = testCommento;
	}
	
}
