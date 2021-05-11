package com.example.scrapeit.repository;

import com.example.scrapeit.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepo extends JpaRepository<File, Long> {
}
