package com.example.newsrestapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
        @NamedQuery(name="Announcement.findAllByCategoryID", query="SELECT a FROM Announcement a WHERE a.category.id = :categoryID"),
        @NamedQuery(name="Announcement.findAllByUserID", query="SELECT a FROM Announcement a WHERE a.appUser.id = :appUserID")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Announcement implements Comparable<Announcement>{
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
    @JoinColumn(name = "appuser_id")
    private AppUser appUser;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Override
    public int compareTo(Announcement o) {
        return getCreationDate().compareTo(o.getCreationDate());
    }
}
