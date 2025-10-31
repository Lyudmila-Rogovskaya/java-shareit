package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto create(BookingRequestDto bookingRequestDto, Long bookerId);

    BookingResponseDto update(Long bookingId, Long ownerId, Boolean approved);

    BookingResponseDto getById(Long bookingId, Long userId);

    List<BookingResponseDto> getByBookerId(Long bookerId, String state);

    List<BookingResponseDto> getByOwnerId(Long ownerId, String state);

}
