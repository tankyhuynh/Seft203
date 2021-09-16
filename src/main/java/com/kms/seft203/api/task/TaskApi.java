package com.kms.seft203.api.task;

import static com.kms.seft203.utils.UrlConstraint.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.kms.seft203.domain.task.Task;
import com.kms.seft203.domain.task.TaskRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(TASKS_URL)
@RequiredArgsConstructor
public class TaskApi {

    // private static final Map<String, Task> DATA = new HashMap<>();
    private final TaskRepository taskRepository;

    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping(TASKS_GET_URL)
    public ResponseEntity<Task> getOne(@PathVariable String id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            return ResponseEntity.ok(task);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody SaveTaskRequest request, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            Task task = convert(request);
            return ResponseEntity.ok(taskRepository.save(task));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping(TASKS_PUT_URL)
    public ResponseEntity<Task> update(@PathVariable String id, @RequestBody SaveTaskRequest request) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = convert(request);
            task.setId(id);
            return ResponseEntity.ok(taskRepository.save(task));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping(TASKS_DELETE_URL)
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            taskRepository.delete(taskOptional.get());
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    

    @GetMapping(TASKS_SEARCH_URL)
    public ResponseEntity<List<Task>> search(@RequestParam("keyword") String keyword) {
        List<Task> tasks = taskRepository.findByTaskContaining(keyword);

        return ResponseEntity.ok(tasks);
    }

    public Task convert(SaveTaskRequest request) {
        Task task = new Task();
        task.setId(request.getId());
        task.setIsCompleted(request.getIsCompleted());
        task.setTask(request.getTask());
        task.setUserId(request.getUserId());

        return task;
    }

}
