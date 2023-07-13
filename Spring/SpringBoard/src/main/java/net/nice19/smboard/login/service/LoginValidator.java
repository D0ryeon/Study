package net.nice19.smboard.login.service;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import net.nice19.smboard.login.model.LoginSessionModel;

public class LoginValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginSessionModel.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LoginSessionModel loginModel = (LoginSessionModel) target;
		
		// check userId filed
		if(loginModel.getUserId()==null||loginModel.getUserId().trim().isEmpty()) {
			errors.rejectValue("userId", "required");
		}
		
		// check userPw field
		if(loginModel.getUserId()==null||loginModel.getUserId().trim().isEmpty()) {
			errors.rejectValue("userPw", "required");
		}
	}

}
