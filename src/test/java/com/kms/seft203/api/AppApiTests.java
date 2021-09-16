package com.kms.seft203.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kms.seft203.domain.app.AppVersionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppApiTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AppVersionRepository appVersionRepo;

	// @Test
	// void testGetCurrentVersion() throws Exception {
	// 	this.mockMvc.perform(get("/app/version"))
	// 			.andDo(print())
	// 			.andExpect(status().isOk())
	// 			.andExpect(jsonPath("$.name").value("SEFT Program"));
	// }
}
