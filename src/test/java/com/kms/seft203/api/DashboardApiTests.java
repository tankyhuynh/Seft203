package com.kms.seft203.api;

import static com.kms.seft203.utils.UrlConstraint.DASHBOARDS_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kms.seft203.config.security.jwt.JwtUtils;
import com.kms.seft203.config.security.services.UserDetailsServiceImpl;
import com.kms.seft203.domain.dashboard.Dashboard;
import com.kms.seft203.domain.dashboard.DashboardRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DashboardApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DashboardRepository dashboadRepo;

    @Autowired
	JwtUtils jwtUtils;

    @Autowired
	private UserDetailsServiceImpl userDetailsService;

    private static final String USERNAME = "admin";

   
    @Test
	void testGetAllDashboard() throws Exception {
        String token = generateToken();
		this.mockMvc.perform(
                get(DASHBOARDS_URL)
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
	}

    @Test
	void testGetAllWrongUrl() throws Exception {
        String token = generateToken();
		this.mockMvc.perform(
                get("/dashboard")
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
	}

    @Test
    void testSaveDashboard() throws Exception {
        String token = generateToken();
        Dashboard dashboard = new Dashboard("1", "1", "Title test", "LayoutType test", new ArrayList<>());

        this.mockMvc.perform(
                    post(DASHBOARDS_URL)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(dashboard)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title test"))
                .andExpect(jsonPath("$.layoutType").value("LayoutType test"));
    }
    

    @Test
    void testUpdateDashboard() throws Exception {
        String token = generateToken();
        String dashboardId = dashboadRepo.findOneByTitle("Title test").getId();
        Dashboard dashboardUpdate = new Dashboard();
        dashboardUpdate.setTitle("Title test (update)");
        dashboardUpdate.setLayoutType("LayoutType update");

        this.mockMvc.perform(
                    put(DASHBOARDS_URL + "/" + dashboardId, dashboardUpdate)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(dashboardUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title test (update)"))
                .andExpect(jsonPath("$.layoutType").value("LayoutType update"))
                .andReturn();
    }
    @Test
    void testUpdateDashboardWithDashboardIdNotFound() throws Exception {
        String token = generateToken();
        String dashboardId = "1";
        Dashboard dashboardUpdate = new Dashboard();
        dashboardUpdate.setTitle("Title test (update)");
        dashboardUpdate.setLayoutType("LayoutType update");

        this.mockMvc.perform(
                    put(DASHBOARDS_URL + "/" + dashboardId, dashboardUpdate)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(dashboardUpdate)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testDeleteDashboard() throws Exception {
        String token = generateToken();
        String dashboardId = dashboadRepo.findOneByTitle("Title test (update)").getId();

        this.mockMvc.perform(
                    delete(DASHBOARDS_URL + "/" + dashboardId)
                            .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void testDeleteDashboardkWithDashboardIdNotFound() throws Exception {
        String token = generateToken();
        String dashboardId = "76686453-3e40-4e94-9e13-ea1047a8dbb8";

        this.mockMvc.perform(
                    delete(DASHBOARDS_URL + "/" + dashboardId)
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
