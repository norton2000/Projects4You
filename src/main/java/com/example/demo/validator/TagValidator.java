package com.example.demo.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.demo.model.Tag;

@Component
public class TagValidator implements Validator{

	public static final int MAX_NAME_SIZE = 100;
	public static final int MAX_DESCRIPTION_SIZE = 1000;
	public static final int MIN_NAME_SIZE = 2;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Tag tag = (Tag) target;
		if(tag.getName().trim().isEmpty())
			errors.rejectValue("name", "required");
		else if(tag.getName().length() < MIN_NAME_SIZE || tag.getName().length() > MAX_NAME_SIZE)
			errors.rejectValue("name",  "size");
		
		
		if(tag.getDescription().length() > MAX_DESCRIPTION_SIZE)
			errors.rejectValue("description", "size");
	}

}
