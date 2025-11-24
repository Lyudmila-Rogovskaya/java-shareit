package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentResponseDtoJsonTest {

    @Autowired
    private JacksonTester<CommentResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(1L);
        dto.setText("Great item!");
        dto.setAuthorName("John");
        dto.setCreated(LocalDateTime.of(2023, 1, 1, 10, 0));

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Great item!");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("John");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-01-01T10:00:00");
    }

}
