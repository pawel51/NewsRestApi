package com.example.newsrestapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Announcement")
@NamedQueries({
        @NamedQuery(name="Announcement.findAllByCategoryID", query="SELECT a FROM Announcement a WHERE a.category.id = :cID"),
        @NamedQuery(name="Announcement.findAllByUserID", query="SELECT a FROM Announcement a WHERE a.appUser.id = :aID")
})
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;
    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;
    @Column(name = "announcement_state", nullable = false)
    private AnnouncementState announcementState;
    @ManyToOne
    private AppUser appUser;
    @ManyToOne
    private Category category;
}
