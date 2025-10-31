package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingResponseDto create(BookingRequestDto bookingRequestDto, Long bookerId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NoSuchElementException("Item not found"));

        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Item is not available");
        }
        if (item.getOwner().getId().equals(bookerId)) {
            throw new IllegalArgumentException("Owner cannot book own item");
        }

        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        return toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto update(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Only owner can update booking status");
        }
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new RuntimeException("Booking status already decided");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return toBookingResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return toBookingResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getByBookerId(Long bookerId, String state) {
        userRepository.findById(bookerId).orElseThrow(() -> new RuntimeException("User not found"));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state.toUpperCase()) {
            case "ALL":
                return bookingRepository.findByBookerId(bookerId, sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findByBookerId(bookerId, sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findByBookerIdAndEndBefore(bookerId, LocalDateTime.now(), sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findByBookerIdAndStartAfter(bookerId, LocalDateTime.now(), sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "WAITING":
            case "REJECTED":
                BookingStatus status = BookingStatus.valueOf(state.toUpperCase());
                return bookingRepository.findByBookerIdAndStatus(bookerId, status, sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingResponseDto> getByOwnerId(Long ownerId, String state) {
        userRepository.findById(ownerId).orElseThrow(() -> new NoSuchElementException("User not found"));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state.toUpperCase()) {
            case "ALL":
                return bookingRepository.findByItemOwnerId(ownerId, sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findCurrentBookingsByBooker(ownerId, LocalDateTime.now(), sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findByItemOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findByItemOwnerIdAndStartAfter(ownerId, LocalDateTime.now(), sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            case "WAITING":
            case "REJECTED":
                BookingStatus status = BookingStatus.valueOf(state.toUpperCase());
                return bookingRepository.findByItemOwnerIdAndStatus(ownerId, status, sort).stream()
                        .map(this::toBookingResponseDto)
                        .collect(Collectors.toList());
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    private BookingResponseDto toBookingResponseDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());

        BookingResponseDto.Booker booker = new BookingResponseDto.Booker();
        booker.setId(booking.getBooker().getId());
        booker.setName(booking.getBooker().getName());
        dto.setBooker(booker);

        BookingResponseDto.Item item = new BookingResponseDto.Item();
        item.setId(booking.getItem().getId());
        item.setName(booking.getItem().getName());
        dto.setItem(item);

        return dto;
    }

}
