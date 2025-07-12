package com.fluffyknightz.ebook_library.bucket;

import com.fluffyknightz.ebook_library.modules.book.entity.Book;
import com.fluffyknightz.ebook_library.modules.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/buckets")
@RequiredArgsConstructor
public class BucketController {

    private final S3Client s3Client;
    private final BookRepository bookRepository;

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.s3.region}")
    private String region;

    @GetMapping("/list")
    public List<String> buckets() {
        var list = s3Client.listBuckets();
        return list.buckets()
                   .stream()
                   .map(Bucket::name)
                   .toList();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("fileData") MultipartFile file) {
        try {
            String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                                .bucket(bucket)
                                                                .key(key)
                                                                .contentType(file.getContentType())
                                                                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String objectUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

            Book book = Book.builder()
                            .coverImageName(file.getOriginalFilename())
                            .coverImageType(file.getContentType())
                            .s3Key(key)
                            .objectURL(objectUrl)
                            .build();
            bookRepository.insert(book);
            return ResponseEntity.ok("File uploaded successfully with");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                                 .body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/download")
    public ResponseEntity<?> uploadFile(@RequestParam("id") String id) {
        try {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isEmpty()) {
                return ResponseEntity.notFound()
                                     .build();
            }


            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                                                .bucket("fluffyknightz")
                                                                .key(book.get()
                                                                         .getS3Key())
                                                                .build();

//        get response
            ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(getObjectRequest);

//        get filedata
            InputStreamResource resource = new InputStreamResource(stream);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename= '%s'", book.get()
                                                                                                             .getCoverImageName()));

            MediaType mediaType;
            try {
                mediaType = MediaType.parseMediaType(book.get()
                                                         .getCoverImageType());
            } catch (IllegalArgumentException e) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                                 .headers(httpHeaders)
                                 .contentType(mediaType)
                                 .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                                 .build();
        }

    }
}
