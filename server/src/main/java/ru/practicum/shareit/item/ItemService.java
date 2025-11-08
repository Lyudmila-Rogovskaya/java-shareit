package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    ItemResponseDto create(Long userId, ItemRequestDto itemRequestDto);

    ItemResponseDto update(Long userId, Long itemId, ItemRequestDto itemRequestDto);

    ItemResponseDto getById(Long itemId, Long userId);

    List<ItemResponseDto> getByOwnerId(Long ownerId);

    List<ItemResponseDto> search(String text);

    CommentResponseDto addComment(Long itemId, CommentRequestDto commentRequestDto, Long userId);

}
