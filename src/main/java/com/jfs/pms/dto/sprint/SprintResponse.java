package com.jfs.pms.dto.sprint;

import java.time.LocalDate;

public class SprintResponse {

    private Long id;
    private String name;
    private String goal;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectId;
    private String projectName;
}
