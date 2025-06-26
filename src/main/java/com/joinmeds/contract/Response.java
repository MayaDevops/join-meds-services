package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String message;
    private UUID userId; // ✅ Changed from Long to UUID
    private int statusCode;
}
