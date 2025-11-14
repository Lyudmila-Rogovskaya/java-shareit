package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestResponseDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Drill", "Powerful drill", true, 1L);

        ItemRequestResponseDto requestResponseDto = new ItemRequestResponseDto();
        requestResponseDto.setId(1L);
        requestResponseDto.setDescription("Need a drill for home repairs");
        requestResponseDto.setCreated(LocalDateTime.of(2023, 1, 1, 10, 0));
        requestResponseDto.setItems(List.of(itemDto));

        JsonContent<ItemRequestResponseDto> result = json.write(requestResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Need a drill for home repairs");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2023-01-01T10:00:00");
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Drill");
    }

}
