package com.project.happyshop.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameAndProviderNotFoundException extends UsernameNotFoundException {
    public UsernameAndProviderNotFoundException(String msg) {
        super(msg);
    }

    public UsernameAndProviderNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
