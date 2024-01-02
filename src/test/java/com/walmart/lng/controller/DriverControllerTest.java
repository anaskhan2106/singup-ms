package com.walmart.lng.controller;

import com.controller.DriverController;
import com.dto.DriverProfileDto;
import com.dto.LoginDto;
import com.dto.LoginMesage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.UserRegistrationService;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith( SpringRunner.class )
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {DriverController.class} )
@EnableWebMvc
@ContextConfiguration(
        classes = {},
        loader = AnnotationConfigWebContextLoader.class )
public class DriverControllerTest {

    public static final String RETURN_ACTIVE_ONLY = "returnActiveOnly";

    @MockBean
    UserRegistrationService userRegistrationService;


    @Autowired
    WebApplicationContext webApplicationContext;

    private DriverProfileDto driverProfileDto;

    private MockMvc mvc;
    private String mockEncodedUserContext;

    private MockMvc mockMvc;
    JSONObject json = new JSONObject();
    private LoginDto loginDto;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup( webApplicationContext ).build();
        this.mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        loginDto = LoginDto.builder().email("abc@gmail.com").username("username").password("password").build();
        driverProfileDto = DriverProfileDto.builder().userId("username").password("password").email("abc@gmail.com")
                .firstName("Peter").lastName("Parker").phone("9591222190").address("HSR").country("India").build();
    }

    @Test
    public void testDriverLoginWithValidData() throws Exception {
        String jsonContent = new ObjectMapper().writeValueAsString(loginDto);
        when( userRegistrationService.loginDriver( any() ) ).thenReturn(
                new LoginMesage("Login success",true));
        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/driver/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(jsonContent))
                .andExpect(status().is(200) );
        verify( userRegistrationService, times( 1 ) ).loginDriver( any() );
    }

    @Test
    public void testDriverLoginWithWrongUrl() throws Exception {
        String jsonContent = new ObjectMapper().writeValueAsString(loginDto);
        when( userRegistrationService.loginDriver( any() ) ).thenReturn(
                new LoginMesage("Login success",true));
        mvc.perform( MockMvcRequestBuilders.post( "/api/v1/driver/logi" )
                .contentType( MediaType.APPLICATION_JSON ).content(jsonContent))
                .andExpect(status().is(404) );
        verify( userRegistrationService, times( 1 ) ).loginDriver( any() );
    }

    @Test
    public void testDriverLoginWithWrongCredentials() throws Exception {
        String jsonContent = new ObjectMapper().writeValueAsString(loginDto);
        when( userRegistrationService.loginDriver( any())).thenReturn(
                new LoginMesage("Login failed",false));
        MvcResult result =  mvc.perform( MockMvcRequestBuilders.post( "/api/v1/driver/login" )
                .contentType( MediaType.APPLICATION_JSON ).content(jsonContent))
                .andExpect(status().isOk())
                .andReturn();
        verify( userRegistrationService, times( 1 ) ).loginDriver( any() );
    }

    @Test
    public void testDriverSignUpWithCorrectCredentials() throws Exception {
        String jsonContent = new ObjectMapper().writeValueAsString(loginDto);
        doNothing().when( userRegistrationService).addDriver( any() ) ;
        MvcResult result =  mvc.perform( MockMvcRequestBuilders.post( "/api/v1/driver/signup" )
                .contentType( MediaType.APPLICATION_JSON ).content(jsonContent))
                .andExpect(status().is(204))
                .andReturn();
        verify( userRegistrationService, times( 1 ) ).loginDriver( any() );
    }

    @Test(expected = NestedServletException.class)
    public void testDriverSignUpWithInCorrectCredentials() throws Exception {
        String jsonContent = new ObjectMapper().writeValueAsString(loginDto);
        doThrow(new DuplicateKeyException("dd")).when(userRegistrationService).addDriver(any());
        MvcResult result =  mvc.perform( MockMvcRequestBuilders.post( "/api/v1/driver/signup" )
                .contentType( MediaType.APPLICATION_JSON ).content(jsonContent))
                .andReturn();
        verify( userRegistrationService, times( 1 ) ).loginDriver( any() );
    }
}
