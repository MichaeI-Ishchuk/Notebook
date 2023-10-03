package com.service.notebook.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@JsonPropertyOrder({ "errorCode", "errorMessage" })
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class ExceptionResponseData {
    private String errorCode;
    private String errorMessage;
}
