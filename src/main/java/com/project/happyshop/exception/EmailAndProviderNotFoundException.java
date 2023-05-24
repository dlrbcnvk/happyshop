package com.project.happyshop.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailAndProviderNotFoundException extends UsernameNotFoundException {
    public EmailAndProviderNotFoundException(String msg) {
        super(msg);
    }

    public EmailAndProviderNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
