package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemResponseDto create(@RequestBody ItemRequestDto itemRequestDto,
                                  @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.create(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto update(@PathVariable Long itemId,
                                  @RequestBody ItemRequestDto itemRequestDto,
                                  @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.update(userId, itemId, itemRequestDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getById(@PathVariable Long itemId,
                                   @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getByOwnerId(@RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@PathVariable Long itemId,
                                         @RequestBody CommentRequestDto commentRequestDto,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.addComment(itemId, commentRequestDto, userId);
    }

}
