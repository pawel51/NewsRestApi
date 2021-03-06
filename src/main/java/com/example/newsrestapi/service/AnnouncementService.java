package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.Category;

import java.util.List;

public interface AnnouncementService {
    Announcement create(Announcement announcement);
    void delete(Announcement announcement);
    Announcement update(Announcement announcement);
    Announcement getAnnouncement(long id);
    List<Announcement> findAllByApplicationUserID(long applicationUserID);
    List<Announcement> findAllByCategoryID(long categoryID);
    List<Announcement> findAllPublic();
    List<Announcement> findAllNotPublic();
    List<Announcement> findAllArchived();
    void deleteAll();
    List<Announcement> findAll();
}
