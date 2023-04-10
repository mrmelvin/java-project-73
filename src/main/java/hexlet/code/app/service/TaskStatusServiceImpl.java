package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;


    @Override
    public TaskStatus createNewTaskStatus(TaskStatusDto taskStatusDto) {
        final TaskStatus status = new TaskStatus();
        status.setName(taskStatusDto.getName());
        return taskStatusRepository.save(status);
    }

    @Override
    public TaskStatus updateTaskStatus(Long statusId, TaskStatusDto taskStatusDto) {
        final TaskStatus status = taskStatusRepository.findById(statusId).get();
        status.setName(taskStatusDto.getName());
        return taskStatusRepository.save(status);
    }
}
