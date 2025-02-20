package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.model.*;
import ru.yandex.practicum.catsgram.service.ImageService;

import java.util.List;

@RestController("/posts/{postId}/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping
    public List<Image> getPostImages(@PathVariable long postId) {
        return imageService.getPostImages(postId);
    }

    @GetMapping(value = "/{imageId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable long imageId) {
        ImageData imageData = imageService.getImageData(imageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(imageData.name())
                        .build()
        );

        return new ResponseEntity<>(imageData.data(), headers, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Image> addPostImage(@PathVariable long postId,
                                    @RequestParam List<MultipartFile> files) {
        return imageService.saveImages(postId, files);
    }
}
