package com.crediya.model.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.crediya.model.exception.Constants.VERIFY_YOUR_DATA;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {
    TOTAL_KEY_NOT_EXISTS(
            "BUSS_ERR_001", "Total key not exists", VERIFY_YOUR_DATA
    );

    private final String code;
    private final String description;
    private final String message;

    @Override
    public String toString(){
        return code + ": " + description + ": " + message;
    }
}
