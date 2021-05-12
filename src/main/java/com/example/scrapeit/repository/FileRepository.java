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

    @Query("SELECT u FROM License u WHERE u.file.id = ?1")
    List<License> findAllLicensesById(Long fileId);

    @Query("SELECT u FROM FileData u WHERE u.fileId = ?1")
    FileData findFileDataById(Long fileId);

    File findByName(String fileName);

    @Query("SELECT u.name FROM File u WHERE u.id = ?1")
    String findNameById(Long fileId);
}
