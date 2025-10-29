package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 1L;

    public Item save(Item item) {
        item.setId(idCounter++);
        items.put(item.getId(), item);
        return item;
    }

    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    public List<Item> findByOwnerId(Long ownerId) {
        List<Item> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (ownerId.equals(item.getOwnerId())) {
                result.add(item);
            }
        }
        return result;
    }

    public List<Item> search(String text) {
        if (text.isBlank()) return Collections.emptyList();

        List<Item> result = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getAvailable() &&
                    (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(text.toLowerCase()))) {
                result.add(item);
            }
        }
        return result;
    }

    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

}
