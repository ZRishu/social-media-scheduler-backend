package com.fierceadventurer.postservice.repository;

import com.fierceadventurer.postservice.entity.PostVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostVariantRepository extends JpaRepository<PostVariant, UUID> {
}
