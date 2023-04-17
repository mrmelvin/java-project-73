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
    public TaskStatus createNewTaskStatus(TaskStatusDto dto) {
        final TaskStatus taskStatus = fromDto(dto);
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTaskStatus(Long statusId, TaskStatusDto dto) {
        final TaskStatus taskStatus = taskStatusRepository.findById(statusId).get();
        merge(taskStatus, dto);
        return taskStatusRepository.save(taskStatus);
    }

    private void merge(final TaskStatus taskStatus, final TaskStatusDto dto) {
        final TaskStatus newTaskStatus = fromDto(dto);
        taskStatus.setName(newTaskStatus.getName());
    }

    private TaskStatus fromDto(final TaskStatusDto dto) {
        return TaskStatus.builder()
                .name(dto.getName())
                .build();
    }
}
