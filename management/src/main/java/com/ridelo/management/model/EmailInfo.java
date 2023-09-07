package com.ridelo.management.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailInfo {
    private String subject;
    private String body;
}
