package com.example.demo2.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.AnnouncementCreateRequest;
import com.example.demo2.dto.response.AnnouncementResponse;
import com.example.demo2.entity.Announcement;
import com.example.demo2.entity.User;
import com.example.demo2.exception.NotFoundException;
import com.example.demo2.repository.AnnouncementDao;
import com.example.demo2.repository.UserDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    
    private final AnnouncementDao announcementDao;
    private final UserDao userDao;
    
    @Transactional
    public AnnouncementResponse CreateAnnouncement(AnnouncementCreateRequest request, String name) {
        User author = userDao.findByUserName(name)
                .orElseThrow(() -> new NotFoundException("user not exists"));
        Announcement announcement = new Announcement(
            request.title(),
            request.content(),
            request.category(),
            author,
            request.isPinned(),
            request.expiresAt()
        );
        announcementDao.save(announcement);
        return AnnouncementResponse.from(announcement);
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> findAll() {
        return announcementDao.findAll().stream().map(AnnouncementResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AnnouncementResponse findById(Integer id) {
        return AnnouncementResponse.from(announcementDao.findByAnnouncementId(id)
                .orElseThrow(() -> new NotFoundException("找不到公告")));
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> findRecentThree() {
        return announcementDao.findTop3ByOrderByPublishedAtDesc().stream().map(AnnouncementResponse::from).toList();
    }

    @Transactional
    public void deleteById(Integer id) {
        announcementDao.deleteById(id);
    }

    @Transactional
    public AnnouncementResponse updateById(Integer id, AnnouncementCreateRequest request) {
        Announcement a = announcementDao.findByAnnouncementId(id)
                .orElseThrow(() -> new NotFoundException("找不到公告"));
        a.setTitle(request.title());
        a.setContent(request.content());
        a.setCategory(request.category());
        a.setExpiresAt(request.expiresAt());
        a.setIsPinned(request.isPinned());
        return AnnouncementResponse.from(a);
    }
}
