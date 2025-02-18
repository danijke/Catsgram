package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.*;
import ru.yandex.practicum.catsgram.model.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;

    private final Map<Long, Post> posts = new HashMap<>();

    public Collection<Post> findAll(int from, int size, String sort) {
        return posts.values().stream()
                .sorted(sort.equalsIgnoreCase("asc") ?
                        Comparator.comparing(Post::getPostDate) :
                        Comparator.comparing(Post::getPostDate).reversed())
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        userService.findUserById(post.getAuthorId()).ifPresentOrElse(user -> {
                    post.setId(getNextId());
                    post.setPostDate(Instant.now());
                    posts.put(post.getId(), post);
                },
                () -> {
                    throw new ConditionsNotMetException("Автор с id = " + post.getAuthorId() + " не найден");
                });
        return post;
    }

    public Post update(Post newPost) {
        // проверяем необходимые условия
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Post getPostById(Long id) {
        return Optional.ofNullable(posts.get(id))
                .orElseThrow(() -> new NotFoundException("Пост с id: " + id + " не найден"));
    }
}