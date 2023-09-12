package com.ridelo.management.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class ExceptionResponse {
    private String message;
    private LocalDateTime dateTime;

}
