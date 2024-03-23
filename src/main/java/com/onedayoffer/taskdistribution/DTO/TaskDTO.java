package com.onedayoffer.taskdistribution.DTO;

import com.onedayoffer.taskdistribution.repositories.entities.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Integer id;
    private String name;
    private TaskType taskType;
    private TaskStatus status;
    private Integer priority;
    private Integer leadTime;

    public static TaskDTO convert(Task task){
        return new TaskDTO(task.getId(),task.getName(),task.getTaskType(),task.getStatus(),task.getPriority(),task.getLeadTime());
    }
}
