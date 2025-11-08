package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.CommentResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingInfo lastBooking;
    private BookingInfo nextBooking;
    private List<CommentResponseDto> comments;

    @Data
    public static class BookingInfo {
        private Long id;
        private Long bookerId;
    }

}
