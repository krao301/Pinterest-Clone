package com.pinterest.content.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.BucketExistsArgs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class StorageService {

    private final MinioClient minioClient;
    private final String bucket;

    public StorageService() {
        var endpoint = System.getenv().getOrDefault("MINIO_ENDPOINT", "http://localhost:9000");
        var access = System.getenv().getOrDefault("MINIO_ACCESS_KEY", "minioadmin");
        var secret = System.getenv().getOrDefault("MINIO_SECRET_KEY", "minioadmin");
        this.bucket = System.getenv().getOrDefault("MINIO_BUCKET", "pins");
        this.minioClient = MinioClient.builder().endpoint(endpoint).credentials(access, secret).build();

        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            // ignore and fall back to local storage later
        }
    }

    public String store(MultipartFile file) throws Exception {
        var filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        try (InputStream is = file.getInputStream()) {
            try {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(filename)
                        .stream(is, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
                var endpoint = System.getenv().getOrDefault("MINIO_PUBLIC_URL", System.getenv().getOrDefault("MINIO_ENDPOINT", "http://localhost:9000"));
                return endpoint + "/" + bucket + "/" + filename;
            } catch (Exception e) {
                // fallback to local filesystem storage
                var uploadsDir = new java.io.File("uploads");
                if (!uploadsDir.exists()) uploadsDir.mkdirs();
                var outFile = new java.io.File(uploadsDir, filename);
                try (var os = new java.io.FileOutputStream(outFile)) {
                    is.transferTo(os);
                }
                var publicBase = System.getenv().getOrDefault("PUBLIC_BASE_URL", "http://localhost:9002");
                return publicBase + "/" + filename;
            }
        }
    }
}
