package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public Announcement create(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @Override
    public void delete(Announcement announcement) {
        announcementRepository.delete(announcement);
    }

    @Override
    public Announcement update(Announcement announcement) {
        return announcementRepository.save(announcement);
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
        Collections.sort(announcementList);
        return announcementList;
    }

    @Override
    public List<Announcement> findAllByCategoryID(long categoryID) {
        List<Announcement> announcementList = announcementRepository.findAllByCategoryID(categoryID);
        Collections.sort(announcementList);
        return announcementList;
    }

    @Override
    public void deleteAll() {
        announcementRepository.deleteAll();
    }

    @Override
    public List<Announcement> findAll() {
        List<Announcement> announcementList = announcementRepository.findAll();
        Collections.sort(announcementList);
        return announcementList;
    }
}
