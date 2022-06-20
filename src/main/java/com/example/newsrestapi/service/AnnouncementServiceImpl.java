package com.example.newsrestapi.service;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.AnnouncementState;
import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.repository.AnnouncementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService{
    private final AnnouncementRepository announcementRepository;
    private final  EmailService emailService;
    private final UserService userService;
    @Autowired
    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository, EmailService emailService, UserService userService) {
        this.announcementRepository = announcementRepository;
        this.emailService = emailService;
        this.userService = userService;
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
        List<Announcement> announcementList = announcementRepository.findAllByAppUserId(applicationUserID);
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
    public List<Announcement> findAllPublic() {
        List<Announcement> announcementList = announcementRepository.findAllPublic();
        Collections.sort(announcementList);
        return announcementList;
    }

    @Override
    public List<Announcement> findAllNotPublic() {
        List<Announcement> announcementList = announcementRepository.findAllNotPublic();
        Collections.sort(announcementList);
        return announcementList;
    }

    @Override
    public List<Announcement> findAllArchived() {
        List<Announcement> announcementList = announcementRepository.findAllArchived();
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
    //delete expired announcements after 1 minute
    @Scheduled(fixedDelay = 60000L)
    public void CheckExpirationDate()
    {
        int count = 0;
        List<Announcement> announcementList = announcementRepository.findAll();
        Date date = new Date();
        for (Announcement announcement : announcementList) {
            if(announcement.getExpirationDate().before(date) && announcement.getAnnouncementState() != AnnouncementState.Archived)
            {
                announcement.setAnnouncementState(AnnouncementState.Archived);
                announcementRepository.save(announcement);
                count++;
                emailService.sendEmailAboutAnnouncementEnd(announcement.getAppUser());
            }
        }
        log.info("Scheduled Process - CheckExpirationDate() - has been completed. Archived announcements - " + count);
    }
}
