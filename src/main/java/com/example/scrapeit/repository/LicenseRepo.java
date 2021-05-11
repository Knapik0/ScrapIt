package com.example.scrapeit.repository;

import com.example.scrapeit.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepo extends JpaRepository<License, Long> {
}
