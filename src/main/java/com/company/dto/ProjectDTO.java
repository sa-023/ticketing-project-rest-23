package com.company.dto;
import com.company.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProjectDTO {

    private Long id;
    private String projectName;
    private String projectCode;
    private UserDTO assignedManager;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectDetail;
    private Status projectStatus;
    /*
     * 🖍️...
     * · @JsonProperty(access = JsonProperty.Access.READ_ONLY):
     *   Jackson will ignore the field in the request body when we post a project. But we will see the field in the response body.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int completeTaskCounts;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int unfinishedTaskCounts;



}
