package com.fierceadventurer.mediastorageservice.repository;

import com.fierceadventurer.mediastorageservice.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MediaFileRepository extends JpaRepository<MediaFile , UUID> {
}
