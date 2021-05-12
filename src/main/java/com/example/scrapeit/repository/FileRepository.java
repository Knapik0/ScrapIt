package com.example.scrapeit.repository;

import com.example.scrapeit.model.File;
import com.example.scrapeit.model.FileData;
import com.example.scrapeit.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT u FROM License u where u.file.id = ?1")
    List<License> findAllLicensesById(Long fileId);

    @Query("SELECT u from FileData u where u.fileId = ?1")
    FileData findFileDataById(Long fileId);
}
