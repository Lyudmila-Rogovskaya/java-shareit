package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a drill for home repairs");

        JsonContent<ItemRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Need a drill for home repairs");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"description\":\"Need a drill for home repairs\"}";

        ItemRequestDto result = json.parseObject(content);

        assertThat(result.getDescription()).isEqualTo("Need a drill for home repairs");
    }

}
