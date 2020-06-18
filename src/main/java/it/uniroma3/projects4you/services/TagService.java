package it.uniroma3.projects4you.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.projects4you.model.Project;
import it.uniroma3.projects4you.model.Tag;
import it.uniroma3.projects4you.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;
	
	
	@Transactional
	public Tag saveTag(Tag tag) {
		return this.tagRepository.save(tag);
	}
	
	@Transactional
	public Tag saveTag(Tag tag, Project project) {
		tag.setProject(project);
		return this.tagRepository.save(tag);
	}
	
	@Transactional
	public Tag getTag(Long id) {
		Optional<Tag> result = this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public void deleteTag(Tag tag) {
		tag.deleteSelf();
		tagRepository.delete(tag);
	}
}
