package com.example.newsrestapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Category")
@NamedQueries({
        @NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    private List<Announcement> Announcements;
}
