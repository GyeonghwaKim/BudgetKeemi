package com.example.budgeKeemi.service;

import com.example.budgeKeemi.domain.entity.ProfileImg;
import com.example.budgeKeemi.exception.excep.MultipartFileException;
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

        validationMultipartFile(multipartFile);

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

    private void validationMultipartFile(MultipartFile multipartFile) {


        //빈파일 체크
        if(multipartFile==null){
            throw new MultipartFileException("파일을 첨부해주세요");
        }

        //파일 형식 체크
        String mimeType=multipartFile.getContentType();
        if(mimeType==null || !mimeType.startsWith("image/")) throw new MultipartFileException("지원되지않는 파일 형식입니다");


        //사이즈 체크
        long maxFileSize=3*1024*1024;
        if(multipartFile.getSize()>maxFileSize){
            throw new MultipartFileException("파일 크기는 최대 3MB까지 가능합니다");
        }
    }

    public ProfileImg defaultProfile() {
        ProfileImg profileImg = ProfileImg.builder()
                .storedFileName(profileImgResourcePath + "/default/defaultProfile.png")
                .build();

        return repository.save(profileImg);
    }
}
