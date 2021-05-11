package com.example.scrapeit.repository;

import com.example.scrapeit.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDataRepo extends JpaRepository<FileData, Long> {
}
