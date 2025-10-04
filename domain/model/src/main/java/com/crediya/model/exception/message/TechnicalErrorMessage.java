package com.crediya.model.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.crediya.model.exception.Constants.A_SYSTEM_FAILURE_OCCURRED;

@Getter
@RequiredArgsConstructor
public enum TechnicalErrorMessage {

    TOTAL_FIND(
            "USR_ERR_001", "Error finding total by key", A_SYSTEM_FAILURE_OCCURRED
    ),
    NO_KEY_ERROR(
            "USR_ERR_001", "total key error", A_SYSTEM_FAILURE_OCCURRED
    );

    private final String code;
    private final String description;
    private final String message;

    @Override
    public String toString(){
        return code + ": " + description + ": " + message;
    }
}

