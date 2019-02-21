package com.oasis.hms.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by Toyin on 2/5/19.
 */
public class DocRequestDto {
    @NotNull
    private String request;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
