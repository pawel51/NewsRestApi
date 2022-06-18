package com.example.newsrestapi;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.AnnouncementState;
import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.service.AnnouncementService;
import com.example.newsrestapi.service.CategoryService;
import com.example.newsrestapi.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class AnnouncementServiceTests {

    @Autowired
    CategoryService categoryService;
    @Autowired
    AnnouncementService announcementService;
    @Autowired
    UserService userService;
    @Test
    public void create_ShouldCreateAnnouncement() {
        //arrange
        Announcement announcement = createAnnouncement(createCategoryAndSaveToDB());

        //act
        announcementService.create(announcement);

        //assert
        Assertions.assertNotNull(announcement.getId());
    }

    @Test
    public void getAnnouncement_ShouldFindAnnouncement() {
        //arrange
        Announcement announcement = createAnnouncementAndSaveToDB(createCategoryAndSaveToDB());

        //act
        Announcement announcementFromDB = announcementService.getAnnouncement(announcement.getId());

        //assert
        Assertions.assertEquals(announcement.getId(), announcementFromDB.getId());
    }

    @Test
    public void findAll_ShouldReturnThreeAnnouncements() {
        //arrange
        Category category = createCategoryAndSaveToDB();
        Announcement announcement1 = createAnnouncementAndSaveToDB(category);
        Announcement announcement2 = createAnnouncementAndSaveToDB(category);
        Announcement announcement3 = createAnnouncementAndSaveToDB(category);

        //act
        List<Announcement> announcements = announcementService.findAll();

        //assert
        Assertions.assertEquals(3, announcements.size());
    }
    @Test
    public void findAllPublic_ShouldReturnTwoAnnouncements()
    {
        //arrange
        Category category = createCategoryAndSaveToDB();
        Announcement announcement1 = createAnnouncementAndSaveToDB(category);
        Announcement announcement2 = createAnnouncementAndSaveToDB(category);
        Announcement announcement3 = createAnnouncement(category);
        announcement3.setAnnouncementState(AnnouncementState.NotPublic);
        announcementService.create(announcement3);

        //act
        List<Announcement> announcements = announcementService.findAllPublic();

        //assert
        Assertions.assertEquals(2, announcements.size());
    }
    @Test
    public void findAllNotPublic_ShouldReturnOneAnnouncement()
    {
        //arrange
        Category category = createCategoryAndSaveToDB();
        Announcement announcement1 = createAnnouncementAndSaveToDB(category);
        Announcement announcement2 = createAnnouncementAndSaveToDB(category);
        Announcement announcement3 = createAnnouncement(category);
        announcement3.setAnnouncementState(AnnouncementState.NotPublic);
        announcementService.create(announcement3);

        //act
        List<Announcement> announcements = announcementService.findAllNotPublic();

        //assert
        Assertions.assertEquals(1, announcements.size());
    }
    @Test
    public void findAllArchived_ShouldReturnTwoAnnouncements()
    {
        //arrange
        Category category = createCategoryAndSaveToDB();
        Announcement announcement1 = createAnnouncementAndSaveToDB(category);
        Announcement announcement2 = createAnnouncementAndSaveToDB(category);
        Announcement announcement3 = createAnnouncement(category);
        announcement3.setAnnouncementState(AnnouncementState.Archived);
        announcementService.create(announcement3);

        //act
        List<Announcement> announcements = announcementService.findAllArchived();

        //assert
        Assertions.assertEquals(1, announcements.size());
    }
    @Test
    public void findAllByCategoryID_ShouldReturnTwoAnnouncements()
    {
        //arrange
        Category category1 = createCategoryAndSaveToDB();
        Category category2 = createCategoryAndSaveToDB("Internship Offers");
        Announcement announcement1 = createAnnouncementAndSaveToDB(category1);
        Announcement announcement2 = createAnnouncementAndSaveToDB(category1);
        Announcement announcement3 = createAnnouncementAndSaveToDB(category2);

        //act
        List<Announcement> announcements = announcementService.findAllByCategoryID(category1.getId());

        //assert
        Assertions.assertEquals(2, announcements.size());
    }

    @Test
    public void findAllByApplicationUserID_ShouldReturnTwoAnnouncements()
    {
        //arrange
        Category category = createCategoryAndSaveToDB();
        AppUser applicationUser = new AppUser();
        applicationUser.setPassword("****");
        userService.saveUser(applicationUser);
        Announcement announcement1 = createAnnouncementAndSaveToDB(category);
        Announcement announcement2 = createAnnouncement(category);
        Announcement announcement3 = createAnnouncement(category);
        announcement2.setAppUser(applicationUser);
        announcement3.setAppUser(applicationUser);
        announcementService.create(announcement2);
        announcementService.create(announcement3);

        //act
        List<Announcement> announcements = announcementService.findAllByApplicationUserID(applicationUser.getId());

        //assert
        Assertions.assertEquals(2, announcements.size());
    }

    @Test
    public void delete_ShouldDeleteOneAnnouncement()
    {
        //arrange
        Announcement announcement = createAnnouncementAndSaveToDB(createCategoryAndSaveToDB());
        Long id = announcement.getId();

        //act
        announcementService.delete(announcement);
        Announcement announcementFromDB = announcementService.getAnnouncement(id);

        //assert
        Assertions.assertNull(announcementFromDB);
    }

    @Test
    public void update_ShouldChangeName()
    {
        //arrange
        Announcement announcement = createAnnouncementAndSaveToDB(createCategoryAndSaveToDB());

        //act
        String updatedName = "UPDATED";
        announcement.setName(updatedName);
        announcementService.update(announcement);
        Announcement announcementFromDB = announcementService.getAnnouncement(announcement.getId());

        //assert
        Assertions.assertEquals(announcement.getName(), announcementFromDB.getName());
    }

    @Test
    public void deleteAll_ShouldDeleteAllAnnouncements()
    {
        //arrange
        Category category = createCategoryAndSaveToDB();
        Announcement announcement = createAnnouncementAndSaveToDB(category);
        Announcement announcement2 = createAnnouncementAndSaveToDB(category);
        Announcement announcement3 = createAnnouncementAndSaveToDB(category);

        //act
        announcementService.deleteAll();
        List<Announcement> announcements = announcementService.findAll();

        //assert
        Assertions.assertEquals(0, announcements.size());
    }
    @BeforeEach
    public void Clean() {
        announcementService.deleteAll();
        categoryService.deleteAll();
    }

    private Category createCategoryAndSaveToDB()
    {
        return createCategoryAndSaveToDB("JobOffers");
    }
    private Category createCategoryAndSaveToDB(String name)
    {
        Category category = new Category();
        category.setName(name);
        categoryService.create(category);
        return category;
    }
    private Announcement createAnnouncement(Category category) {

        Announcement announcement = new Announcement();
        announcement.setName("Title 1");
        announcement.setContent("This is content!");
        announcement.setCreationDate(new Date());

        Date tomorrow = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(tomorrow);
        c.add(Calendar.DATE, 1);
        tomorrow = c.getTime();

        announcement.setExpirationDate(tomorrow);
        announcement.setAnnouncementState(AnnouncementState.Public);
        announcement.setCategory(category);
        return announcement;
    }
    private Announcement createAnnouncementAndSaveToDB(Category category)
    {
        Announcement announcement = createAnnouncement(category);
        return announcementService.create(announcement);
    }
}
