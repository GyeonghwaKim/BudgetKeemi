package com.example.budgeKeemi.repository;

import com.example.budgeKeemi.domain.entity.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImgRepository extends JpaRepository<ProfileImg,Long > {


}
