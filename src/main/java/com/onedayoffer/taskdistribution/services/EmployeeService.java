package com.onedayoffer.taskdistribution.services;

import com.onedayoffer.taskdistribution.DTO.EmployeeDTO;
import com.onedayoffer.taskdistribution.DTO.TaskDTO;
import com.onedayoffer.taskdistribution.DTO.TaskStatus;
import com.onedayoffer.taskdistribution.repositories.EmployeeRepository;
import com.onedayoffer.taskdistribution.repositories.TaskRepository;
import com.onedayoffer.taskdistribution.repositories.entities.Employee;
import com.onedayoffer.taskdistribution.repositories.entities.Task;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public List<EmployeeDTO> getEmployees(@Nullable String sortDirection) {
        Sort.Direction direction;
        Optional<String> sort = Optional.ofNullable(sortDirection);
        if (sort.isEmpty()) {
            return employeeRepository.findAll().stream().map(EmployeeDTO::convert).collect(Collectors.toList());
        } else {
            try {
                direction = Sort.Direction.valueOf(sortDirection);
            } catch (Exception e) {
                throw new java.lang.UnsupportedOperationException("implement getOneEmployee");
            }
            return employeeRepository.findAllAndSort(Sort.by(direction, "fio"))
                    .stream().map(EmployeeDTO::convert).collect(Collectors.toList());
        }
    }

    @Transactional
    public EmployeeDTO getOneEmployee(Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(EmployeeDTO::convert).orElse(null);
    }

    public List<TaskDTO> getTasksByEmployeeId(Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get().getTasks().stream().map(TaskDTO::convert).toList();
        } else return Collections.EMPTY_LIST;
    }

    @Transactional
    public void changeTaskStatus(Integer taskId, TaskStatus status) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task currentTask = task.get();
            currentTask.setStatus(status);
            taskRepository.save(currentTask);
        } else throw new java.lang.UnsupportedOperationException("task not found");
    }

    @Transactional
    public void postNewTask(Integer employeeId, TaskDTO newTask) {
       Optional<Employee> employee = employeeRepository.findById(employeeId);
       if(employee.isPresent()){
           Task task = modelMapper.map(newTask, Task.class);
           taskRepository.save(task);
           employee.get().addTask(task);
       }
    }
}
