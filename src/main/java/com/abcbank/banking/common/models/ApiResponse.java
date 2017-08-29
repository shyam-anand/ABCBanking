package com.abcbank.banking.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;
import java.util.Map;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> extends ResourceSupport {

    private Map<String, Object> meta;
    private T data;
    private List<ErrorDetails> errors;

    public ApiResponse() {
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<ErrorDetails> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDetails> errors) {
        this.errors = errors;
    }
}
