package com.example.paymybuddy.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class ControllerTests {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * First we are testing the user unauthentified.
	 * 
	 * 
	 * 
	 * 
	 * @throws Exception
	 */

	@Test
	public void testGetLoginPage_WhenUserIsNotAuthentified() throws Exception {

		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));

	}

	@Test
	public void testGetRegisteringPage_WhenUserIsNotAuthentified() throws Exception {

		
		mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("register"));

	}

	@Test
	public void testGetRegisteringSucessfullyPage_WhenUserIsNotAuthentified() throws Exception {

		mockMvc.perform(get("/registersucessfully")).andExpect(status().isOk())
				.andExpect(view().name("register_successfully"));

	}

	@Test
	public void testGetTransferPage_WhenUserIsNotAuthentified_ShouldReturnLoginPage() throws Exception {

		mockMvc.perform(get("/transfer")).andExpect(status().isFound());

	}
	
	@Test
	@WithMockUser( username= "barack.obama@gmail.com", authorities="USER" )
	public void userShouldNotBeAbleToAccessDashboard() throws Exception {
		
		mockMvc.perform(get("/dashboard")).andExpect(status().isForbidden());

	}
	
	
	/**
	 * Now testing controller when user is authentified
	 * 
	 * 
	 * 
	 * 
	 * @throws Exception
	 */
	
	@Test
	@WithMockUser( username= "barack.obama@gmail.com", authorities="ADMIN" )
	public void userShouldBeAbleToAccessAllInformation() throws Exception {
		
		mockMvc.perform(get("/dashboard")).andExpect(status().isOk()).andExpect(view().name("dashboard"));

	}
	
	

}
