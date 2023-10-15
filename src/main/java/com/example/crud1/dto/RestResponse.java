package com.example.crud1.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;


@Getter
@RequiredArgsConstructor
@Schema(hidden = true)
@Jacksonized
@JsonSerialize
public class RestResponse {
    private final Object result;
}
