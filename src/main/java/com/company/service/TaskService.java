package com.company.service;
import com.company.dto.ProjectDTO;
import com.company.dto.TaskDTO;
import com.company.entity.User;
import com.company.enums.Status;
import java.util.List;

public interface TaskService {

    TaskDTO findById(Long id);
    List<TaskDTO> listAllTasks();
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id);
    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);
    void deleteByProject(ProjectDTO project);
    void completeByProject(ProjectDTO project);
    List<TaskDTO> listAllTasksByStatusIsNot(Status status);
    void updateStatus(TaskDTO task);
    List<TaskDTO> listAllTasksByStatus(Status status);
    List<TaskDTO> readAllByAssignedEmployee(User assignedEmployee);




}
