package com.kms.seft203.api;

import static com.kms.seft203.utils.UrlConstraint.TASKS_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.seft203.config.security.jwt.JwtUtils;
import com.kms.seft203.config.security.services.UserDetailsServiceImpl;
import com.kms.seft203.domain.task.Task;
import com.kms.seft203.domain.task.TaskRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
	JwtUtils jwtUtils;

    @Autowired
	private UserDetailsServiceImpl userDetailsService;

    private static final String USERNAME = "admin";

   
    @Test
	void testGetAllTasks() throws Exception {
        String token = generateToken();
		this.mockMvc.perform(
                get(TASKS_URL)
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
	}

    @Test
	void testGetAllWrongUrl() throws Exception {
        String token = generateToken();
		this.mockMvc.perform(
                get("/task")
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
	}

    @Test
    void testSaveTask() throws Exception {
        String token = generateToken();
        Task task = new Task("1", "Task test", false, "user-1");

        this.mockMvc.perform(
                    post(TASKS_URL)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(task)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Task test"))
                .andExpect(jsonPath("$.isCompleted").value(false));
    }
    

    @Test
    void testUpdateTask() throws Exception {
        String token = generateToken();
        String taskId = taskRepo.findOneByTask("Task test").getId();
        Task taskUpdate = new Task();
        taskUpdate.setTask("Task test (Update)");
        taskUpdate.setIsCompleted(true);

        this.mockMvc.perform(
                    put(TASKS_URL + "/" + taskId, taskUpdate)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(taskUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Task test (Update)"))
                .andExpect(jsonPath("$.isCompleted").value(true))
                .andReturn();
    }
    @Test
    void testUpdateTaskWithTaskIdNotFound() throws Exception {
        String token = generateToken();
        String taskId = "1";
        Task taskUpdate = new Task();
        taskUpdate.setTask("Task test (Update)");
        taskUpdate.setIsCompleted(true);

        this.mockMvc.perform(
                    put(TASKS_URL + "/" + taskId, taskUpdate)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(taskUpdate)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testDeleteTask() throws Exception {
        String token = generateToken();
        String taskId = taskRepo.findOneByTask("Task test (Update)").getId();

        this.mockMvc.perform(
                    delete(TASKS_URL + "/" + taskId)
                            .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void testDeleteTaskWithTaskIdNotFound() throws Exception {
        String token = generateToken();
        String taskId = "1";

        this.mockMvc.perform(
                    delete(TASKS_URL + "/" + taskId)
                            .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    String generateToken(){
        final UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
		final String token = jwtUtils.generateToken(userDetails);

        return token;
    }

    private String toJSONString(Object obj) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(obj);
    }
}
