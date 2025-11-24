package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.CommentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemResponseDtoJsonTest {

    @Autowired
    private JacksonTester<ItemResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemResponseDto.BookingInfo lastBooking = new ItemResponseDto.BookingInfo();
        lastBooking.setId(1L);
        lastBooking.setBookerId(2L);

        ItemResponseDto.BookingInfo nextBooking = new ItemResponseDto.BookingInfo();
        nextBooking.setId(3L);
        nextBooking.setBookerId(4L);

        CommentResponseDto comment = new CommentResponseDto();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthorName("John");
        comment.setCreated(LocalDateTime.of(2023, 1, 1, 10, 0));

        ItemResponseDto itemResponseDto = new ItemResponseDto(
                1L, "Drill", "Powerful drill", true, 5L,
                lastBooking, nextBooking, List.of(comment)
        );

        JsonContent<ItemResponseDto> result = json.write(itemResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Drill");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Powerful drill");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(5);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(3);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(4);
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
    }

}
