package com.example.demo2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo2.entity.Announcement;


public interface AnnouncementDao extends JpaRepository<Announcement, Integer> {
    
    Optional<Announcement> findByAnnouncementId(Integer announcementId);
    List<Announcement> findTop3ByOrderByPublishedAtDesc();
}
