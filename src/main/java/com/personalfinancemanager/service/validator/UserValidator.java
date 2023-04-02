package com.personalfinancemanager.service.validator;

import com.personalfinancemanager.domain.model.UserModel;
import com.personalfinancemanager.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    public static void validate(UserModel model) {

        String errorMessage = "";

        if (StringUtils.isBlank(model.getFirstname())) {
            errorMessage += "Invalid first name!\n";
        }

        if (StringUtils.isBlank(model.getLastname())) {
            errorMessage += "Invalid last name!\n";
        }

        if (model.getPassword().length() < 8) {
            errorMessage += "Invalid password!\n";
        }

        if (StringUtils.isBlank(model.getEmailAddress())) {
            errorMessage += "Invalid email address!\n";
        } else {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(model.getEmailAddress());
            boolean matchFound = matcher.find();
            if (!matchFound)
                errorMessage += "Invalid email address!\n";
        }

        if (!errorMessage.equals("")) {
            throw new ValidationException(errorMessage);
        }
    }
}
