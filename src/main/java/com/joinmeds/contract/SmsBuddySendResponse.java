package com.joinmeds.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsBuddySendResponse {
    private String status;   // "200"
    private Integer charges;
    private String message;
    private List<SmsBuddyData> data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SmsBuddyData {
        private String id;
        private String mobile;
        private String status;
        private Integer unit;
        private Integer length;
        private Integer charge;
    }
}