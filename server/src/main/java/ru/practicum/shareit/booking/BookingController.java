package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto create(@Valid @RequestBody BookingRequestDto bookingRequestDto,
                                     @RequestHeader(USER_ID_HEADER) Long bookerId) {
        return bookingService.create(bookingRequestDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader(USER_ID_HEADER) Long ownerId) {
        return bookingService.update(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@PathVariable Long bookingId,
                                      @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getByBookerId(@RequestHeader(USER_ID_HEADER) Long bookerId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getByOwnerId(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getByOwnerId(ownerId, state);
    }

}
