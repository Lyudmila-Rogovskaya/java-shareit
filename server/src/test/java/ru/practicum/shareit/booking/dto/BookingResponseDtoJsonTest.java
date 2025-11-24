package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingResponseDtoJsonTest {

    @Autowired
    private JacksonTester<BookingResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingResponseDto.Booker booker = new BookingResponseDto.Booker();
        booker.setId(1L);
        booker.setName("John Doe");

        BookingResponseDto.Item item = new BookingResponseDto.Item();
        item.setId(2L);
        item.setName("Drill");

        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(LocalDateTime.of(2023, 1, 1, 10, 0));
        bookingResponseDto.setEnd(LocalDateTime.of(2023, 1, 2, 10, 0));
        bookingResponseDto.setStatus(BookingStatus.WAITING);
        bookingResponseDto.setBooker(booker);
        bookingResponseDto.setItem(item);

        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-01-01T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-01-02T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Drill");
    }

}
