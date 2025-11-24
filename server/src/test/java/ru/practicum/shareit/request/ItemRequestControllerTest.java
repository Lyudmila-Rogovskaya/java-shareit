package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDto;
    private ItemRequestResponseDto itemRequestResponseDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Need a drill");

        itemRequestResponseDto = new ItemRequestResponseDto();
        itemRequestResponseDto.setId(1L);
        itemRequestResponseDto.setDescription("Need a drill");
        itemRequestResponseDto.setCreated(LocalDateTime.now());
    }

    @Test
    void create_whenValidRequest_thenReturnItemRequest() throws Exception {
        when(itemRequestService.create(any(), anyLong())).thenReturn(itemRequestResponseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Need a drill")));
    }

    @Test
    void getByRequestor_whenRequestsExist_thenReturnRequests() throws Exception {
        when(itemRequestService.getByRequestor(anyLong())).thenReturn(List.of(itemRequestResponseDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getAll_whenRequestsExist_thenReturnRequests() throws Exception {
        when(itemRequestService.getAll(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestResponseDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getById_whenRequestExists_thenReturnRequest() throws Exception {
        when(itemRequestService.getById(anyLong(), anyLong())).thenReturn(itemRequestResponseDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

}
