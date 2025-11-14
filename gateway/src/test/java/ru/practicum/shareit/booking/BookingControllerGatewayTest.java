package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerGatewayTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private BookItemRequestDto bookItemRequestDto;

    @BeforeEach
    void setUp() {
        bookItemRequestDto = new BookItemRequestDto(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
    }

    @Test
    void create_whenValidBooking_thenReturnOk() throws Exception {
        when(bookingClient.bookItem(anyLong(), any(BookItemRequestDto.class)))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookItemRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void create_whenInvalidBooking_thenReturnBadRequest() throws Exception {
        BookItemRequestDto invalidBooking = new BookItemRequestDto(1L,
                LocalDateTime.now().minusDays(1), // past start date
                LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBooking)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookings_whenValidRequest_thenReturnOk() throws Exception {
        when(bookingClient.getBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>("[]", HttpStatus.OK));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getByOwnerId_whenValidRequest_thenReturnOk() throws Exception {
        when(bookingClient.getBookingsByOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>("[]", HttpStatus.OK));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking_whenValidRequest_thenReturnOk() throws Exception {
        when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_whenValidRequest_thenReturnOk() throws Exception {
        when(bookingClient.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new ResponseEntity<>("{}", HttpStatus.OK));

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void create_whenInvalidDates_thenReturnBadRequest() throws Exception {
        BookItemRequestDto invalidBooking = new BookItemRequestDto(1L,
                LocalDateTime.now().minusDays(1), // past start date
                LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBooking)))
                .andExpect(status().isBadRequest());
    }

}
