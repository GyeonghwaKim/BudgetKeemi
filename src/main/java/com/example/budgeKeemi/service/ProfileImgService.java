package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.entity.ProfileImg;
import com.example.budgeKeemi.repository.ProfileImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ProfileImgService {

    @Value("${IMG_SAVE_PATH}")
    private String imgSavePath;

    @Value("${PROFILEIMG_RESOURCE_PATH}")
    private String profileImgResourcePath;


    private final ProfileImgRepository repository;


    public ProfileImg saveProfileImg(MultipartFile multipartFile) {
        String originalFileName=multipartFile.getOriginalFilename();
        String storedFileName = profileImgResourcePath+ "/"+ System.currentTimeMillis() + "-" + originalFileName;
        String savePath=imgSavePath+storedFileName;

        try {
            multipartFile.transferTo(new File(savePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ProfileImg profileImg=ProfileImg.builder()
                .originalFileName(originalFileName)
                .storedFileName(storedFileName)
                .build();

        ProfileImg saveProfileImg = repository.save(profileImg);

        return saveProfileImg;

    }

    public ProfileImg defaultProfile() {
        ProfileImg profileImg = ProfileImg.builder()
                .storedFileName(profileImgResourcePath + "/default/defaultProfile.png")
                .build();

        return repository.save(profileImg);
    }
}
