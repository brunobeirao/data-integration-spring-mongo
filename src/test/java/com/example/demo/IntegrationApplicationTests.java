package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationApplicationTests {

	@Test
	public void contextLoads() {
	}

	@MockBean
	private IntegracaoController integracaoController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(integracaoController).build();
	}

	@Test
	public void load() throws Exception {
		this.mockMvc.perform(get("/companies/loader").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void integration() throws Exception {
		this.mockMvc.perform(get("/companies/integrator").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void testParam() throws Exception {
		this.mockMvc.perform(get("/companies/search").param("name", "DIRECTV").param("zip", "38006")).andExpect(status().isOk());
	}
	
	@Test
	public void testWithoutParam() throws Exception {
		this.mockMvc.perform(get("/companies/search")).andExpect(status().isOk());
	}
	
	@Test
	public void testPost() throws Exception {
		this.mockMvc.perform(post("/companies/search").param("name", "DIRECTV").param("zip", "38006")).andExpect(status().isMethodNotAllowed());
	}
}
