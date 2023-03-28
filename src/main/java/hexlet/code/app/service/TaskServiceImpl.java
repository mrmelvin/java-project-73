package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Status;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private StatusRepository statusRepository;
    private UserRepository userRepository;
    private UserServiceImpl userService;

    @Override
    public Task createNewTask(TaskDto taskDto) {
//        final Task task = new Task();
//        task.setName(taskDto.getName());
//        task.setDescription(taskDto.getDescription());
//        task.setAuthor(userService.getCurrentUser());
//        Status statusFromDto = statusRepository.findById(taskDto.getStatusId()).get();
//        task.setStatus(statusFromDto);
//        if (taskDto.getExecutorId() != null) {
//            User executorFromDto = userRepository.findById(taskDto.getExecutorId()).get();
//            task.setExecutor(executorFromDto);
//        }
        Task newTask = fromDto(taskDto);
        return taskRepository.save(newTask);
    }

    @Override
    public Task updateTask(Long taskId, TaskDto taskDto) {
        final Task taskToUpdate = taskRepository.findById(taskId).get();
        taskToUpdate.setName(taskDto.getName());
        taskToUpdate.setDescription(taskDto.getDescription());
        Status statusFromDto = statusRepository.findById(taskDto.getStatusId()).get();
        taskToUpdate.setStatus(statusFromDto);
        taskToUpdate.setAuthor(userService.getCurrentUser());
        if (taskDto.getExecutorId() != null) {
            User executorFromDto = userRepository.findById(taskDto.getExecutorId()).get();
            taskToUpdate.setExecutor(executorFromDto);
        }
        return taskRepository.save(taskToUpdate);
    }

    private Task fromDto(TaskDto taskDto) {
        User author = userService.getCurrentUser();
        User executorFromDto = null;
        Status statusFromDto = null;
        if (taskDto.getExecutorId() != null) {
            executorFromDto = userRepository.findById(taskDto.getExecutorId()).get();
        }
        if (taskDto.getStatusId() != null) {
            statusFromDto = statusRepository.findById(taskDto.getStatusId()).get();
        }
        return Task.builder()
                    .author(author)
                    .name(taskDto.getName())
                    .description(taskDto.getDescription())
                    .executor(executorFromDto)
                    .status(statusFromDto).build();
    }
}
