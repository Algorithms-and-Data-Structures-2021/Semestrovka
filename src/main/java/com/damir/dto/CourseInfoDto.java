package com.damir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseInfoDto {
    private boolean incourse;
    private boolean isCreator;
    private int studentsCount;
    private boolean homeWorkIsDone;
}
