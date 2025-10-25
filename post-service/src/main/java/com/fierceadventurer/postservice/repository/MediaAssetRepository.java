package com.fierceadventurer.postservice.repository;

import com.fierceadventurer.postservice.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, UUID> {
}
