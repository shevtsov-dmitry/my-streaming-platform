package com.filminfopage.repo;

import com.filminfopage.model.VideoMaterial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VideoMaterialRepo extends JpaRepository<VideoMaterial, Long> {
    @Query("SELECT t FROM VideoMaterial t ORDER BY t.id DESC")
    List<VideoMaterial> getLastSaved();
}
