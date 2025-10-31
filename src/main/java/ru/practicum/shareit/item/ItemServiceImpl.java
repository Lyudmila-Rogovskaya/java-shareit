package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponseDto create(Long userId, ItemRequestDto itemRequestDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Item item = ItemMapper.toItem(itemRequestDto, userId);
        return ItemMapper.toItemResponseDto(itemRepository.save(item));
    }

    @Override
    public ItemResponseDto update(Long userId, Long itemId, ItemRequestDto itemRequestDto) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));

        if (!existingItem.getOwnerId().equals(userId)) {
            throw new NoSuchElementException("User is not owner");
        }

        if (itemRequestDto.getName() != null) existingItem.setName(itemRequestDto.getName());
        if (itemRequestDto.getDescription() != null) existingItem.setDescription(itemRequestDto.getDescription());
        if (itemRequestDto.getAvailable() != null) existingItem.setAvailable(itemRequestDto.getAvailable());

        return ItemMapper.toItemResponseDto(itemRepository.update(existingItem));
    }

    @Override
    public ItemResponseDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
        return ItemMapper.toItemResponseDto(item);
    }

    @Override
    public List<ItemResponseDto> getByOwnerId(Long ownerId) {
        List<ItemResponseDto> result = new ArrayList<>();
        for (Item item : itemRepository.findByOwnerId(ownerId)) {
            result.add(ItemMapper.toItemResponseDto(item));
        }
        return result;
    }

    @Override
    public List<ItemResponseDto> search(String text) {

        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        List<ItemResponseDto> result = new ArrayList<>();
        for (Item item : itemRepository.search(text)) {
            result.add(ItemMapper.toItemResponseDto(item));
        }
        return result;
    }

}
