package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.service.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/posts/{postId}/images")
    public List<Image> getPostImages(@PathVariable long postId) {
        return imageService.getPostImages(postId);
    }

    @PostMapping("/posts/{postId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Image> addPostImage(@PathVariable long postId,
                                    @RequestParam List<MultipartFile> files) {
        return imageService.saveImages(postId, files);
    }
}
