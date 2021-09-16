package com.kms.seft203.api;

import static com.kms.seft203.utils.UrlConstraint.CONTACTS_URL;
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
import com.kms.seft203.domain.contact.Contact;
import com.kms.seft203.domain.contact.ContactRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ContactApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepo;

    @Autowired
	JwtUtils jwtUtils;

    @Autowired
	private UserDetailsServiceImpl userDetailsService;

    private static final String USERNAME = "admin";

   
    @Test
	void testGetAllContacts() throws Exception {
        String token = generateToken();
		this.mockMvc.perform(
                get(CONTACTS_URL)
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
	}

    @Test
	void testGetAllWrongUrl() throws Exception {
        String token = generateToken();
		this.mockMvc.perform(
                get("/contact")
                    .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
	}

    @Test
    void testSaveContact() throws Exception {
        String token = generateToken();
        Contact contact = new Contact("1", "LastName", "FirstName", "Title", "department", "project", "avatar", 1);

        this.mockMvc.perform(
                    post(CONTACTS_URL)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(contact)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("department"))
                .andExpect(jsonPath("$.title").value("Title"));
    }
    

    @Test
    void testUpdateContact() throws Exception {
        String token = generateToken();
        String contactId = contactRepo.findOneByDepartment("department").getId();
        Contact contactUpdate = new Contact();
        contactUpdate.setDepartment("Department (Update)");
        contactUpdate.setTitle("Title Update");

        this.mockMvc.perform(
                    put(CONTACTS_URL + "/" + contactId, contactUpdate)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(contactUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("Department (Update)"))
                .andExpect(jsonPath("$.title").value("Title Update"))
                .andReturn();
    }
    @Test
    void testUpdateContactWithContactIdNotFound() throws Exception {
        String token = generateToken();
        String contactId = "1";
        Contact contactUpdate = new Contact();
        contactUpdate.setDepartment("department (Update)");
        contactUpdate.setTitle("Title Update");

        this.mockMvc.perform(
                    put(CONTACTS_URL + "/" + contactId, contactUpdate)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(toJSONString(contactUpdate)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testDeleteContact() throws Exception {
        String token = generateToken();
        String contactId = contactRepo.findOneByDepartment("Department (Update)").getId();

        this.mockMvc.perform(
                    delete(CONTACTS_URL + "/" + contactId)
                            .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void testDeleteContactWithContactIdNotFound() throws Exception {
        String token = generateToken();
        String contactId = "1";

        this.mockMvc.perform(
                    delete(CONTACTS_URL + "/" + contactId)
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
