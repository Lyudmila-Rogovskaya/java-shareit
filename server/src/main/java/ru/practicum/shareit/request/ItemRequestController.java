package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestResponseDto create(@RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.create(itemRequestDto, requestorId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getByRequestor(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.getByRequestor(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getAll(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(@PathVariable Long requestId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getById(requestId, userId);
    }

}
