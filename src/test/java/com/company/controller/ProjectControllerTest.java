package com.company.controller;
import com.company.dto.ProjectDTO;
import com.company.dto.ResponseDTO;
import com.company.dto.RoleDTO;
import com.company.dto.UserDTO;
import com.company.enums.Gender;
import com.company.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
 * 🦋 Integration Testing
 * · Once different modules are developed and integrated, integration testing is carried out. Its primary goal is to identify
 *   problems that arise during end-to-end user request processing when several modules interact with one another.
 * · In terms of a typical Spring Boot Crud application, unit tests can be written to test REST controllers, DAO layers,
 *   etc. separately. It will not require even the embedded server.
 * · In integration testing, we shall focus on testing complete request processing from the controller to the persistence layer.
 *   The application shall run inside an embedded server to create the application context and all beans. Some of these beans may be overridden to mock certain behaviors.
 *
 * 🖍️...
 * · The spring-boot-starter-test is the primary dependency that contains the majority of elements required for our tests.
 *
 * 🦋 MockMvc
 * · The Spring MockMvc class is part of the Spring test framework and helps in testing the controllers by explicitly starting a Servlet container.
 * · MockMvc is a mocked servlet environment that we can use to test our HTTP controller endpoints without the need to launch our embedded servlet (Tomcat) container.
 * · While MockMvc is a mocked environment, it still comes with HTTP semantics so that we can test the serialization, HTTP status codes, and return types of our endpoints.
 *
 * 🦋 Annotations
 * 🔹 @WebMvcTest: It is used Spring MVC controllers. It allows us to test the behavior of controllers, request mappings, and HTTP responses in a controlled and isolated
 *    environment. By using @WebMvcTest, we can narrow down the scope of testing to just the web layer, without loading the entire application context.
 *    @WebMvcTest annotation-based test runs faster because it will load only the specified controller and its dependencies without loading the entire application.
 *    Includes both the @AutoConfigureWebMvc and the @AutoConfigureMockMvc, among other functionality.
 * 🔹 @SpringBootTest: This annotation creates an application context and loads the full application context. It is used for integration testing.
 *    It will bootstrap the full application context, which means we can @Autowire any bean that's picked up by component scanning into our test.
 *    It starts the embedded server, creates a web environment, and then enables @Test methods to do integration testing.
 *    It will use SpringApplication to load the ApplicationContext. By default, it will not start a server, but it will provide a mock environment.
 * 🔹 @AutoConfigureMockMvc: It autoconfigures the MockMvc. It allows us to add a MockMvc instance to our ApplicationContext,
 *    so we can make HTTP requests against our controller.
 * 🔹 @MockBean: We use it to add mock objects to the Spring application context. The mock will replace any existing bean of the same type in the application context.
 *    If no bean of the same type is defined, a new one will be added. This annotation is useful in integration tests where a particular bean,
 *    like an external service, needs to be mocked.
 *
 * ❗️@SpringBootTest vs @WebMvcTest
 * · @SpringBootTest will load the complete application and autowire all the beans, which makes it a bit slow.
 * · @WebMvcTest is only going to scan the controller you've defined and the MVC infrastructure. So if our controller has some dependency
 *   to other beans from our service layer, the test won't start until we either load that config ourselves or provide a mock for it.
 *   This is much faster as we only load a tiny portion of your app. This annotation uses slicing.
 *
 * 🖍️...
 * · MockMvcRequestBuilders class: We build our requests by using the static methods of this class.
 * · perform(RequestBuilder requestBuilder): We execute requests by calling this method.
 * · MockMvcResultMatchers class: We write assertions for the received response by using the static methods of this class.
 * · MockMvcResultHandlers class: We use it for assertion as well.
 *
 */
@SpringBootTest // it will give us all the beans in the application context before starting the test
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;
    static UserDTO userDTO;
    static ProjectDTO projectDTO;
    static String token;

    @BeforeAll
    static void setUp() { // Data to post
        token = "Bearer " + makeRequest();
        userDTO = UserDTO.builder()
                .id(2L)
                .firstName("ozzy")
                .lastName("ozzy")
                .userName("ozzy")
                .passWord("Abc1")
                .confirmPassWord("Abc1")
                .role(new RoleDTO(2L, "Manager"))
                .gender(Gender.MALE)
                .build();
        projectDTO = ProjectDTO.builder()
                .projectCode("Api1")
                .projectName("Api-ozzy")
                .assignedManager(userDTO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .projectDetail("Api Test")
                .projectStatus(Status.OPEN)
                .build();
    }




    @Test
    public void givenNoToken_whenGetRequest() throws Exception { // without a token, so the status code should be 4xx. Test should pass.
        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/project"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenToken_whenGetRequest() throws Exception { // 2. get
        mvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/project")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectCode").exists()) // Response Body: ResponseWrapper -> data [{index0}...]
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].assignedManager.userName").isNotEmpty());
    }

    @Test
    public void givenToken_createProject() throws Exception { // 1. post
        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/project")
                .header("Authorization", token)
                .content(toJsonString(projectDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

    }

    @Test
    public void givenToken_updateProject() throws Exception { // 3. update
        projectDTO.setProjectName("Api-company");
        mvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/project")
                        .header("Authorization", token)
                        .content(toJsonString(projectDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Project is successfully updated"));
    }

    @Test
    public void givenToken_deleteProject() throws Exception { // 4. delete
        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/project/" + projectDTO.getProjectCode())
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }





    // Ready method. We will send our object as a parameter, and the method will return us JSON as a string object.
    private static String toJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Ready code. This method will return a token.
    private static String makeRequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "ticketing-app");
        map.add("client_secret", "RRFjRddGFpohQvmo2oSCPI4FJ0r5QzU9");
        map.add("username", "Mike");
        map.add("password", "abc1");
        map.add("scope", "openid");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<ResponseDTO> response =
                restTemplate.exchange("http://localhost:8080/auth/realms/company-dev/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        ResponseDTO.class);
        if (response.getBody() != null) {
            return response.getBody().getAccess_token();
        }
        return "";
    }















}