package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.Category;

import java.util.List;

public interface AnnouncementService {
    void create(Announcement announcement);
    void delete(Announcement announcement);
    void update(Announcement announcement);
    Announcement getAnnouncement(long id);
    List<Announcement> findAllByApplicationUserID(long applicationUserID);
    List<Announcement> findAllByCategoryID(long categoryID);
    List<Announcement> findAll();
}
