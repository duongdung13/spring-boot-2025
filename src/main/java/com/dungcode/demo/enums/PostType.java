package com.dungcode.demo.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PostType {
    @JsonProperty("POST")
    POST,
    @JsonProperty("PAGE")
    PAGE,
    @JsonProperty("NONE")
    NONE;
}
