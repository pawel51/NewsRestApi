package dto;

import com.example.newsrestapi.model.AnnouncementState;
import com.example.newsrestapi.model.AppUser;
import com.example.newsrestapi.model.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Getter
@Setter
public class AnnouncementDTO {
    private Long id;
    private String name;
    private String content;
    private Date creationDate;
    private Date expirationDate;
    private AnnouncementState announcementState;
    private Long appUserId;
    private Long categoryId;


}
