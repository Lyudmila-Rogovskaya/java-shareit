package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestResponseDto create(ItemRequestDto itemRequestDto, Long requestorId) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());

        ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
        return toItemRequestResponseDto(savedRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getByRequestor(Long requestorId) {
        userRepository.findById(requestorId).orElseThrow(() -> new NoSuchElementException("User not found"));
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(requestorId);
        return requests.stream()
                .map(this::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestResponseDto> getAll(Long requestorId, Integer from, Integer size) {
        userRepository.findById(requestorId).orElseThrow(() -> new NoSuchElementException("User not found"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(requestorId, pageable);
        return requests.stream()
                .map(this::toItemRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestResponseDto getById(Long requestId, Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        return toItemRequestResponseDto(itemRequest);
    }

    private ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest) {
        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());

        List<Item> items = itemRepository.findByRequestId(itemRequest.getId());
        List<ItemDto> itemDtos = items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    private ItemDto toItemDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequestId());
        return dto;
    }

}
