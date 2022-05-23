package dto;

import com.example.newsrestapi.model.AnnouncementState;
import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Category;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

public class AnnouncementDTO {
    private Long id;
    private String name;
    private String content;
    private Date creationDate;
    private Date expirationDate;
    private AnnouncementState announcementState;
    private Long appUserID;
    private Long categoryID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public AnnouncementState getAnnouncementState() {
        return announcementState;
    }

    public void setAnnouncementState(AnnouncementState announcementState) {
        this.announcementState = announcementState;
    }

    public Long getAppUserID() {
        return appUserID;
    }

    public void setAppUserID(Long appUserID) {
        this.appUserID = appUserID;
    }

    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }
}
