package com.fierceadventurer.postservice.repository;
import com.fierceadventurer.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> , PagingAndSortingRepository<Post, UUID> {
}
