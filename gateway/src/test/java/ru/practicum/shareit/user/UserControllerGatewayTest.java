package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerGatewayTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "John Doe", "john@example.com");
    }

    @Test
    void create_whenValidUser_thenReturnOk() throws Exception {
        when(userClient.create(any(UserDto.class)))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    void create_whenInvalidUser_thenReturnBadRequest() throws Exception {
        UserDto invalidUser = new UserDto();
        invalidUser.setName("");
        invalidUser.setEmail("invalid-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_whenValidRequest_thenReturnOk() throws Exception {
        when(userClient.update(anyLong(), any(UserDto.class)))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getById_whenValidRequest_thenReturnOk() throws Exception {
        when(userClient.getById(anyLong()))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_whenValidRequest_thenReturnOk() throws Exception {
        when(userClient.getAll())
                .thenReturn(new ResponseEntity<>("[]", HttpStatus.OK));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_whenValidRequest_thenReturnOk() throws Exception {
        when(userClient.delete(anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

}
