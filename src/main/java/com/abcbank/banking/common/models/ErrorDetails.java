package com.abcbank.banking.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDetails extends ApiResponse {

    private String title;
    private String detail;
    private String status;
    private String code;

    public ErrorDetails(Exception e) {
        title = e.getClass().getSimpleName();
        detail = e.getMessage();
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            if (stackTraceElement.getClassName().contains("com.abcbank.banking"))
                stackTrace.append(String.format("In %s.%s at line %d (%s)",
                        stackTraceElement.getClassName(),
                        stackTraceElement.getMethodName(),
                        stackTraceElement.getLineNumber(),
                        stackTraceElement.getFileName()))
                        .append(System.lineSeparator());
        }
        Map<String, String> meta = new HashMap<>();
        meta.put("stackTrace", stackTrace.toString());
        setMeta(meta);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
