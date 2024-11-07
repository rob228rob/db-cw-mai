//package com.mai.db_cw_rbl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mai.db_cw_rbl.LawyerPackage.Dtos.LawyerCreationRequest;
//import com.mai.db_cw_rbl.LawyerPackage.Dtos.LawyerResponse;
//import com.mai.db_cw_rbl.LawyerPackage.Lawyer;
//import com.mai.db_cw_rbl.LawyerPackage.LawyerController;
//import com.mai.db_cw_rbl.LawyerPackage.LawyerService;
//import com.mai.db_cw_rbl.UserPackage.Dto.UserRegistrationRequest;
//import com.mai.db_cw_rbl.UserPackage.Dto.UserResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(LawyerController.class)
//public class LawyerControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private LawyerService lawyerService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private LawyerCreationRequest validCreationRequest;
//
//    private LawyerResponse sampleLawyerResponse;
//
//    @MockBean
//    private ModelMapper modelMapper;
//
//    @BeforeEach
//    public void setup() {
//        UserRegistrationRequest userRegistrationRequest = UserRegistrationRequest.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .password("passswd")
//                .confirmPassword("passswd")
//                .role("ROLE_LAWYER")
//                .build();
//
//        validCreationRequest = LawyerCreationRequest.builder()
//                .userData(userRegistrationRequest)
//                .licence("ABCD")
//                .experienceYears(1)
//                .specialization("CRIMINAL_LAW")
//                .build();
//
//        sampleLawyerResponse = LawyerResponse.builder()
//                .userData(modelMapper.map(validCreationRequest, UserResponse.class))
//                .specName("CRIMINAL_LAW")
//                .licenceNumber("ABCD")
//                .yearsExperience(1)
//                .build();
//
//        when(modelMapper.map(any(UserRegistrationRequest.class), eq(UserResponse.class)))
//                .thenReturn(UserResponse.builder()
//                        .id(UUID.randomUUID())
//                        .firstName("John")
//                        .lastName("Doe")
//                        .email("john.doe@example.com")
//                        .build());
//    }
//
//    @Test
//    @WithMockUser(roles = "LAWYER")
//    public void testCreateLawyer_Success() throws Exception {
//
//        when(lawyerService.createLawyer(ArgumentMatchers.any(LawyerCreationRequest.class)))
//                .thenReturn(sampleLawyerResponse);
//
//        mockMvc.perform(post("/lawyers/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(validCreationRequest)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.lawyerId").value(sampleLawyerResponse.getId().toString()))
//                .andExpect(jsonPath("$.firstName").value("John"))
//                .andExpect(jsonPath("$.lastName").value("Doe"))
//                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
//                .andExpect(jsonPath("$.specialization").value("CRIMINAL_LAW"));
//
//        verify(lawyerService, times(1)).createLawyer(ArgumentMatchers.any(LawyerCreationRequest.class));
//    }
//
//    @Test
//    @WithMockUser(roles = "LAWYER")
//    public void testCreateLawyer_InvalidInput() throws Exception {
//
//        LawyerCreationRequest invalidRequest = new LawyerCreationRequest(validCreationRequest);
//        invalidRequest.setExperienceYears(-10); // Негативное значение
//
//        mockMvc.perform(post("/lawyers/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest());
//
//        verify(lawyerService, times(0)).createLawyer(ArgumentMatchers.any(LawyerCreationRequest.class));
//    }
//
//    @Test
//    @WithMockUser(roles = "LAWYER")
//    public void testCreateLawyer_ServiceException() throws Exception {
//
//        when(lawyerService.createLawyer(ArgumentMatchers.any(LawyerCreationRequest.class)))
//                .thenThrow(new RuntimeException("Service Error"));
//
//        mockMvc.perform(post("/lawyers/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(validCreationRequest)))
//                .andExpect(status().isInternalServerError());
//
//        verify(lawyerService, times(1)).createLawyer(ArgumentMatchers.any(LawyerCreationRequest.class));
//    }
//
//    @Test
//    @WithMockUser(roles = "LAWYER")
//    public void testGetLawyer_Success() throws Exception {
//        UUID lawyerId = sampleLawyerResponse.getId();
//
//        when(lawyerService.findLawyerById(lawyerId)).thenReturn(sampleLawyerResponse);
//
//        mockMvc.perform(get("/lawyers/get/{lawyerId}", lawyerId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.lawyerId").value(lawyerId.toString()))
//                .andExpect(jsonPath("$.firstName").value("John"))
//                .andExpect(jsonPath("$.lastName").value("Doe"))
//                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
//                .andExpect(jsonPath("$.specialization").value("CRIMINAL_LAW"));
//
//        verify(lawyerService, times(1)).findLawyerById(lawyerId);
//    }
//
//    @Test
//    @WithMockUser(roles = "LAWYER")
//    public void testGetLawyer_NotFound() throws Exception {
//        UUID lawyerId = UUID.randomUUID();
//
//        when(lawyerService.findLawyerById(lawyerId)).thenThrow(new RuntimeException("Lawyer not found"));
//
//        mockMvc.perform(get("/lawyers/get/{lawyerId}", lawyerId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError());
//
//        verify(lawyerService, times(1)).findLawyerById(lawyerId);
//    }
//
//    @Test
//    @WithMockUser(roles = "LAWYER")
//    public void testGetLawyer_InvalidUUID() throws Exception {
//        String invalidLawyerId = "invalid-uuid";
//
//        mockMvc.perform(get("/lawyers/get/{lawyerId}", invalidLawyerId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        verify(lawyerService, times(0)).findLawyerById(any(UUID.class));
//    }
//}