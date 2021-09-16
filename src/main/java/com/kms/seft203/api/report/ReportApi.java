package com.kms.seft203.api.report;

import static com.kms.seft203.utils.UrlConstraint.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kms.seft203.domain.contact.Contact;
import com.kms.seft203.domain.contact.ContactRepository;
import com.kms.seft203.domain.task.Task;
import com.kms.seft203.domain.task.TaskRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(REPORTS_URL)
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ReportApi {

    /*
     * Count by field in a collection For example: - Number of each title (EM, TE,
     * SE, BA) in Contact collection - Number of completed, not completed tasks in
     * Task collection
     */

    private final TaskRepository taskRepository;
    private final ContactRepository contactRepository;

    @GetMapping(REPORTS_COUNT_BY_FIELD_AND_COLLECTION_URL)
    public Map<?, Integer> countBy(@PathVariable String collection, @PathVariable String field) {
        Map<?, Integer> mapFieldValue = mapFieldValueByCollection(collection, field);

        return mapFieldValue;
    }

    public Map<?, Integer> getDistinctValueOfContactAndField(List<Contact> contacts, String field) {
        Map<String, Integer> byFirstName = new HashMap<>();
        Map<String, Integer> byLastName = new HashMap<>();
        Map<String, Integer> byTitle = new HashMap<>();
        Map<String, Integer> byDepartment = new HashMap<>();
        Map<String, Integer> byProject = new HashMap<>();
        Map<String, Integer> byAvatar = new HashMap<>();
        Map<Integer, Integer> byEmployeeId = new HashMap<>();

        if ("firstName".equals(field)) {
            contacts.forEach(contact -> {
                byFirstName.merge(contact.getFirstName(), 1, Integer::sum);
            });
            return byFirstName;
        }
        if ("lastName".equals(field)) {
            contacts.forEach(contact -> {
                byLastName.merge(contact.getLastName(), 1, Integer::sum);
            });
            return byLastName;
        }
        if ("title".equals(field)) {
            contacts.forEach(contact -> {
                byTitle.merge(contact.getFirstName(), 1, Integer::sum);
            });
            return byTitle;
        }
        if ("department".equals(field)) {
            contacts.forEach(contact -> {
                byDepartment.merge(contact.getFirstName(), 1, Integer::sum);
            });
            return byDepartment;
        }
        if ("project".equals(field)) {
            contacts.forEach(contact -> {
                byProject.merge(contact.getProject(), 1, Integer::sum);
            });
            return byProject;
        }
        if ("avatar".equals(field)) {
            contacts.forEach(contact -> {
                byAvatar.merge(contact.getAvatar(), 1, Integer::sum);
            });
            return byAvatar;
        }
        if ("employeeId".equals(field)) {
            contacts.forEach(contact -> {
                byEmployeeId.merge(contact.getEmployeeId(), 1, Integer::sum);
            });
            return byEmployeeId;
        }

        return new HashMap<>();
    }

    public Map<?, Integer> getDistinctValueOfTaskAndField(List<Task> tasks, String field) {
        Map<String, Integer> byTask = new HashMap<>();
        Map<Boolean, Integer> byIsCompleted = new HashMap<>();
        Map<String, Integer> byUserId = new HashMap<>();

        if ("task".equals(field)) {
            tasks.forEach(task -> {
                byTask.merge(task.getTask(), 1, Integer::sum);
            });
            return byTask;
        }
        if ("isCompleted".equals(field)) {
            tasks.forEach(task -> {
                byIsCompleted.merge(task.getIsCompleted(), 1, Integer::sum);
            });
            return byIsCompleted;
        }
        if ("userId".equals(field)) {
            tasks.forEach(task -> {
                byUserId.merge(task.getUserId(), 1, Integer::sum);
            });
            return byUserId;
        }

        return new HashMap<>();
    }

    public Map<?, Integer> mapFieldValueByCollection(String collection, String field) {

        List<?> list = getEntitiesByCollection(collection);

        if ("Contact".equals(collection)) {
            return getDistinctValueOfContactAndField((List<Contact>) list, field);
        }
        if ("Task".equals(collection)) {
            return getDistinctValueOfTaskAndField((List<Task>) list, field);
        }

        return new HashMap<>();
    }

    public List<?> getEntitiesByCollection(String collection) {

        if ("Contact".equals(collection)) {
            return contactRepository.findAll();
        }
        if ("Task".equals(collection)) {
            return taskRepository.findAll();
        }

        return new ArrayList<>();
    }
}
