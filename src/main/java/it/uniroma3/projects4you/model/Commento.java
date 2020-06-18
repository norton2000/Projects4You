package it.uniroma3.projects4you.model;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leavedBy == null) ? 0 : leavedBy.hashCode());
		result = prime * result + ((testoCommento == null) ? 0 : testoCommento.hashCode());
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
		Commento other = (Commento) obj;
		if (leavedBy == null) {
			if (other.leavedBy != null)
				return false;
		} else if (!leavedBy.equals(other.leavedBy))
			return false;
		if (testoCommento == null) {
			if (other.testoCommento != null)
				return false;
		} else if (!testoCommento.equals(other.testoCommento))
			return false;
		return true;
	}
	
}
