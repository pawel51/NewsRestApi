package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementServiceImpl implements AnnouncementService{
    private final AnnouncementRepository announcementRepository;
    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public void create(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    @Override
    public void delete(Announcement announcement) {
        announcementRepository.delete(announcement);
    }

    @Override
    public void update(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    @Override
    public Announcement getAnnouncement(long id) {
        Optional<Announcement> announcement = announcementRepository.findById(id);
        if(announcement.isPresent())
        {
            return announcement.get();
        }
        else{
            return null;
        }
    }

    @Override
    public List<Announcement> findAllByApplicationUserID(long applicationUserID) {
        List<Announcement> announcementList = announcementRepository.findAllByUserID(applicationUserID);
        return announcementList;
    }

    @Override
    public List<Announcement> findAllByCategoryID(long categoryID) {
        List<Announcement> announcementList = announcementRepository.findAllByCategoryID(categoryID);
        return announcementList;
    }

    @Override
    public List<Announcement> findAll() {
        return announcementRepository.findAll();
    }
}
