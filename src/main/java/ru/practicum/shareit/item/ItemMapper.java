package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemResponseDto toItemResponseDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId(),
                null, // lastBooking будет заполнено в сервисе
                null, // nextBooking будет заполнено в сервисе
                null  // comments будет заполнено в сервисе
        );
    }

    public static Item toItem(ItemRequestDto itemRequestDto) {
        return new Item(
                null,
                itemRequestDto.getName(),
                itemRequestDto.getDescription(),
                itemRequestDto.getAvailable(),
                null,
                itemRequestDto.getRequestId()
        );
    }

}
