package com.example.newsrestapi.repository;

import com.example.newsrestapi.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findAllByCategoryID(long categoryID);
    List<Announcement> findAllByAppUserId(long userID);
    List<Announcement> findAllPublic();
    List<Announcement> findAllNotPublic();
    List<Announcement> findAllArchived();
}
