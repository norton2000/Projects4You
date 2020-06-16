package com.example.demo.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Project;
import com.example.demo.model.Tag;
import com.example.demo.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	private TagRepository tagRepository;
	
	
	public Tag saveTag(Tag tag) {
		return this.tagRepository.save(tag);
	}
	
	public Tag saveTag(Tag tag, Project project) {
		tag.setProject(project);
		return this.tagRepository.save(tag);
	}
	
	public Tag getTag(Long id) {
		Optional<Tag> result = this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
}
