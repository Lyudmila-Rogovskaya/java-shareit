package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Creating item {}, userId={}", itemRequestDto, userId);
        return itemClient.create(itemRequestDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId,
                                         @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Updating item {}, itemId={}, userId={}", itemRequestDto, itemId, userId);
        return itemClient.update(itemId, itemRequestDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable Long itemId,
                                          @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Get item, itemId={}, userId={}", itemId, userId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwnerId(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Get items by owner, userId={}", userId);
        return itemClient.getByOwnerId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Search items, text={}, from={}, size={}", text, from, size);
        return itemClient.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                             @RequestBody CommentRequestDto commentRequestDto,
                                             @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info("Adding comment to item {}, userId={}", itemId, userId);
        return itemClient.addComment(itemId, commentRequestDto, userId);
    }

}
