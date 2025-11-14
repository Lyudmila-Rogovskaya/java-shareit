package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentRequestDtoJsonTest {

    @Autowired
    private JacksonTester<CommentRequestDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentRequestDto dto = new CommentRequestDto();
        dto.setText("Great item!");

        var result = json.write(dto);

        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("Great item!");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"text\":\"Great item!\"}";

        CommentRequestDto result = json.parseObject(content);

        assertThat(result.getText()).isEqualTo("Great item!");
    }

}
