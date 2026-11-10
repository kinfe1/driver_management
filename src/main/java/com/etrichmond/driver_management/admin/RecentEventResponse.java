package com.etrichmond.driver_management.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecentEventResponse {
    private String event;
    private String driver;
    private String time;
    private String status;
}
