package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ItemClient.class)
class ItemClientTest {

    @Autowired
    private ItemClient itemClient;

    @Autowired
    private MockRestServiceServer mockServer;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setName("Test Item");
        itemRequestDto.setDescription("Test Description");
        itemRequestDto.setAvailable(true);
    }

    @Test
    void create_whenValidRequest_thenReturnResponse() {
        mockServer.expect(requestTo("http://localhost:9090/items"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess("{\"id\":1,\"name\":\"Test Item\"}", MediaType.APPLICATION_JSON));

        var response = itemClient.create(itemRequestDto, 1L);

        assertNotNull(response);
        mockServer.verify();
    }

    @Test
    void getById_whenValidRequest_thenReturnResponse() {
        mockServer.expect(requestTo("http://localhost:9090/items/1"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess("{\"id\":1,\"name\":\"Test Item\"}", MediaType.APPLICATION_JSON));

        var response = itemClient.getById(1L, 1L);

        assertNotNull(response);
        mockServer.verify();
    }

    @Test
    void getByOwnerId_whenValidRequest_thenReturnResponse() {
        mockServer.expect(requestTo("http://localhost:9090/items"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess("[{\"id\":1,\"name\":\"Test Item\"}]", MediaType.APPLICATION_JSON));

        var response = itemClient.getByOwnerId(1L);

        assertNotNull(response);
        mockServer.verify();
    }

    @Test
    void search_whenValidRequest_thenReturnResponse() {
        mockServer.expect(requestTo("http://localhost:9090/items/search?text=test&from=0&size=10"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[{\"id\":1,\"name\":\"Test Item\"}]", MediaType.APPLICATION_JSON));

        var response = itemClient.search("test", 0, 10);

        assertNotNull(response);
        mockServer.verify();
    }

    @Test
    void update_whenValidRequest_thenReturnResponse() {
        mockServer.expect(requestTo("http://localhost:9090/items/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess("{\"id\":1,\"name\":\"Updated Item\"}", MediaType.APPLICATION_JSON));

        var response = itemClient.update(1L, itemRequestDto, 1L);

        assertNotNull(response);
        mockServer.verify();
    }

    @Test
    void addComment_whenValidRequest_thenReturnResponse() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Great item!");

        mockServer.expect(requestTo("http://localhost:9090/items/1/comment"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess("{\"id\":1,\"text\":\"Great item!\"}", MediaType.APPLICATION_JSON));

        var response = itemClient.addComment(1L, commentRequestDto, 1L);

        assertNotNull(response);
        mockServer.verify();
    }

}
