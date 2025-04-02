package com.company.SkillSyncAI.exceptions;

import java.lang.RuntimeException;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
