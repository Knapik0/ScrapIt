package com.example.scrapeit.repository;

import com.example.scrapeit.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenseRepo extends JpaRepository<License, Long> {

    List<License> findAllByFileId(Long fileId);
}
