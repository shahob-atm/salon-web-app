package com.sh32bit.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseBody<T> {

    private boolean success;
    private String message;

    private String timestamp;
    private T data;

    public static <T> ApiResponseBody<T> ok(String message, T data) {
        return new ApiResponseBody<>(true, message, data);
    }

    public static <T> ApiResponseBody<T> fail(String message) {
        return new ApiResponseBody<>(false, message, null);
    }

    public ApiResponseBody(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timestamp = ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
        this.data = data;
    }
}
