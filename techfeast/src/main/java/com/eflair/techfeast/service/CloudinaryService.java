package com.eflair.techfeast.service;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

//    public String uploadImage(MultipartFile file) {
//        try {
//            Map uploadResult = cloudinary.uploader().upload(
//                    file.getBytes(),
//                    Map.of("folder", "techfeast_payments")
//            );
//
//            return uploadResult.get("secure_url").toString();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Image upload failed", e);
//        }
//    }
    public String uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "techfeast")
            );

            // ✅ THIS is the correct URL
            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed");
        }
    }

}
