package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private TaskStatusRepository taskStatusRepository;
    private UserRepository userRepository;

    private LabelRepository labelRepository;
    private UserServiceImpl userService;

    @Override
    public Task createNewTask(TaskDto taskDto) {
        Task newTask = fromDto(taskDto);
        return taskRepository.save(newTask);
    }

    @Override
    public Task updateTask(Long taskId, TaskDto taskDto) {
        final Task taskToUpdate = taskRepository.findById(taskId).get();

        return taskRepository.save(taskToUpdate);
    }

    private Task fromDto(TaskDto taskDto) {
        User author = userService.getCurrentUser();
        User executorFromDto = null;
        TaskStatus taskStatusFromDto = null;
        if (taskDto.getExecutorId() != null) {
            executorFromDto = userRepository.findById(taskDto.getExecutorId()).get();
        }
        if (taskDto.getTaskStatusId() != null) {
            taskStatusFromDto = taskStatusRepository.findById(taskDto.getTaskStatusId()).get();
        }
        return Task.builder()
                .author(author)
                .name(taskDto.getName())
                .description(taskDto.getDescription())
                .executor(executorFromDto)
                .taskStatus(taskStatusFromDto)
                .labels(getLabels(taskDto.getLabelIds()))
                .build();
    }

    private void merge(final Task task, final TaskDto taskDto) {
        final Task updatedTask = fromDto(taskDto);
        task.setName(updatedTask.getName());
        task.setTaskStatus(updatedTask.getTaskStatus());
        task.setAuthor(updatedTask.getAuthor());
        task.setDescription(updatedTask.getDescription());
        task.setExecutor(updatedTask.getExecutor());
        task.setLabels(updatedTask.getLabels());
    }

    private Set<Label> getLabels(Set<Long> ids) {
        Set<Label> labels = new HashSet<>();
        for (Long id: ids) {
            labels.add(labelRepository.findById(id).get());
        }
        return labels;
    }
}
