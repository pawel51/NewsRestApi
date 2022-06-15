package com.example.newsrestapi.api;

import com.example.newsrestapi.model.Announcement;
import com.example.newsrestapi.model.AnnouncementState;
import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Category;
import com.example.newsrestapi.service.AnnouncementService;
import com.example.newsrestapi.service.CategoryService;
import com.example.newsrestapi.service.EmailService;
import com.example.newsrestapi.service.UserService;
import dto.AnnouncementDTO;
//import dto.StatusDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("api/")
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final EmailService emailService;
    private final ModelMapper modelMapper;
    @Autowired
    public AnnouncementController(AnnouncementService announcementService, CategoryService categoryService, UserService userService,
                                  EmailService emailService, ModelMapper modelMapper) {
        this.announcementService = announcementService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }
    @PostMapping(path="announcements")
    public ResponseEntity<Announcement> createAnnouncement(Principal principal, @RequestBody AnnouncementDTO announcementDTO)
    {
        AppUser user = userService.getUser(principal.getName());
        Announcement announcement = ConvertFromDTO(List.of(announcementDTO)).get(0);
        if (user != null){
            announcement.setAppUser(user);
        }

        announcement.setCreationDate(new Date());
        return ResponseEntity.status(HttpStatus.OK).body(announcementService.create(announcement));

    }
    @GetMapping(path="announcements")
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncements()
    {
        return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(announcementService.findAll()));
    }
    @GetMapping(path = "announcements/{id}")
    public ResponseEntity<AnnouncementDTO> getAnnouncement(@PathVariable Long id)
    {
        Announcement announcement = announcementService.getAnnouncement(id);
        if(announcement == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement doesnt exist");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(List.of(announcement)).get(0));
        }
    }
    @GetMapping(path = {"categories/{categoryID}/announcements", "announcements/bycategoryid/{categoryID}"})
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementsByCategoryID(@PathVariable Long categoryID)
    {
        Category category = categoryService.findById(categoryID);
        if(category == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "category doesnt exist");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(announcementService.findAllByCategoryID(categoryID)));
        }
    }
    @GetMapping(path = {"users/{userID}/announcements", "announcements/byuserid/{userID}"})
    public ResponseEntity<List<AnnouncementDTO>> getAnnouncementsByUserID(@PathVariable Long userID)
    {
        AppUser appUser = userService.getUserById(userID);
        if(appUser == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesnt exist");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(announcementService.findAllByApplicationUserID(userID)));
        }
    }
    @GetMapping(path = "announcements/public")
    public ResponseEntity<List<AnnouncementDTO>> getPublicAnnouncements()
    {
        return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(announcementService.findAllPublic()));
    }
    @GetMapping(path = "announcements/notpublic")
    public ResponseEntity<List<AnnouncementDTO>> getNotPublicAnnouncements()
    {
        return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(announcementService.findAllNotPublic()));
    }
    @GetMapping(path = "announcements/archived")
    public ResponseEntity<List<AnnouncementDTO>> getArchivedAnnouncements()
    {
        return ResponseEntity.status(HttpStatus.OK).body(ConvertToDTO(announcementService.findAllArchived()));
    }
    @GetMapping("announcements/states")
    public List<String> getStatuses(){
        List<String> enumNames = Stream.of(AnnouncementState.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return enumNames;

    }

    @DeleteMapping(path = "announcements/{id}")
    public void deleteAnnouncement(@PathVariable("id") Long id)
    {
        Announcement announcement = announcementService.getAnnouncement(id);
        if(announcement == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Announcement doesnt exist");
        }
        else
        {
            announcementService.delete(announcement);
        }
    }
    @PutMapping("announcements/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable("id") Long id, @RequestBody AnnouncementDTO announcementDTO)
    {
        Announcement announcementFromDB = announcementService.getAnnouncement(id);
        if(announcementFromDB == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category doesnt exist");
        }
        else
        {
            AnnouncementState previousState = announcementFromDB.getAnnouncementState();
            announcementDTO.setId(id);
            ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body(announcementService.update(
                    ConvertFromDTO(List.of(announcementDTO)).get(0)));
            if(previousState == AnnouncementState.NotPublic && announcementDTO.getAnnouncementState() == AnnouncementState.Public)
            {
                AppUser appUser = userService.getUserById(announcementDTO.getAppUserId());
                try{
                    //ENABLE IN FINAL VERSION OF APP
                    //emailService.sendEmailAboutAnnouncementPublication(appUser);
                }
                catch (Exception e)
                {
                    log.error(e.getMessage());
                }
            }
            return responseEntity;
        }
    }
    private List<AnnouncementDTO> ConvertToDTO(List<Announcement> announcementList)
    {
        List<AnnouncementDTO> convertedAnnouncements = new ArrayList<AnnouncementDTO>();
        for (Announcement announcement : announcementList) {
            AnnouncementDTO announcementDTO = modelMapper.map(announcement, AnnouncementDTO.class);
            convertedAnnouncements.add(announcementDTO);
        }
        return  convertedAnnouncements;
    }
    private List<Announcement> ConvertFromDTO(List<AnnouncementDTO> announcementList)
    {
        List<Announcement> convertedFromDTOAnnouncements = new ArrayList<Announcement>();
        for (AnnouncementDTO announcementDTO : announcementList) {
            Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
            convertedFromDTOAnnouncements.add(announcement);
        }
        return  convertedFromDTOAnnouncements;
    }
}
