package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
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
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemResponseDto create(Long userId, ItemRequestDto itemRequestDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Item item = ItemMapper.toItem(itemRequestDto);
        item.setOwner(owner);
        return ItemMapper.toItemResponseDto(itemRepository.save(item));
    }

    @Override
    public ItemResponseDto update(Long userId, Long itemId, ItemRequestDto itemRequestDto) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found"));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new NoSuchElementException("Item not found or user is not owner");
        }

        if (itemRequestDto.getName() != null) existingItem.setName(itemRequestDto.getName());
        if (itemRequestDto.getDescription() != null) existingItem.setDescription(itemRequestDto.getDescription());
        if (itemRequestDto.getAvailable() != null) existingItem.setAvailable(itemRequestDto.getAvailable());

        return ItemMapper.toItemResponseDto(itemRepository.save(existingItem));
    }

    @Override
    public ItemResponseDto getById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        ItemResponseDto dto = ItemMapper.toItemResponseDto(item);

        // добавляем информацию о бронированиях только для владельца
        if (item.getOwner().getId().equals(userId)) {
            addBookingInfoToDto(dto, itemId);
        }

        // добавляем комментарии всегда
        addCommentsToDto(dto, itemId);

        return dto;
    }

    @Override
    public List<ItemResponseDto> getByOwnerId(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(item -> {
                    ItemResponseDto dto = ItemMapper.toItemResponseDto(item);
                    addBookingInfoToDto(dto, item.getId());
                    addCommentsToDto(dto, item.getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponseDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(Long itemId, CommentRequestDto commentRequestDto, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // проверка, что пользователь брал вещь в аренду
        List<Booking> userBookings = bookingRepository.findByBookerIdAndEndBefore(userId, LocalDateTime.now(),
                Sort.by(Sort.Direction.DESC, "end"));

        boolean hasBooked = userBookings.stream()
                .anyMatch(booking -> booking.getItem().getId().equals(itemId));

        if (!hasBooked) {
            throw new RuntimeException("User has not booked this item");
        }

        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return toCommentResponseDto(savedComment);
    }

    private void addBookingInfoToDto(ItemResponseDto dto, Long itemId) {
        LocalDateTime now = LocalDateTime.now();

        // получаем последнее бронирование (самое позднее из завершенных)
        List<Booking> lastBookings = bookingRepository.findLastBookingForItem(itemId, now);
        if (!lastBookings.isEmpty()) {
            Booking lastBooking = lastBookings.get(0);
            ItemResponseDto.BookingInfo lastBookingInfo = new ItemResponseDto.BookingInfo();
            lastBookingInfo.setId(lastBooking.getId());
            lastBookingInfo.setBookerId(lastBooking.getBooker().getId());
            dto.setLastBooking(lastBookingInfo);
        } else {
            dto.setLastBooking(null);
        }

        // получаем следующее бронирование (самое раннее из будущих)
        List<Booking> nextBookings = bookingRepository.findNextBookingForItem(itemId, now);
        if (!nextBookings.isEmpty()) {
            Booking nextBooking = nextBookings.get(0);
            ItemResponseDto.BookingInfo nextBookingInfo = new ItemResponseDto.BookingInfo();
            nextBookingInfo.setId(nextBooking.getId());
            nextBookingInfo.setBookerId(nextBooking.getBooker().getId());
            dto.setNextBooking(nextBookingInfo);
        } else {
            dto.setNextBooking(null);
        }
    }

    private void addCommentsToDto(ItemResponseDto dto, Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(this::toCommentResponseDto)
                .collect(Collectors.toList());
        dto.setComments(commentDtos);
    }

    private CommentResponseDto toCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());
        return dto;
    }

}
