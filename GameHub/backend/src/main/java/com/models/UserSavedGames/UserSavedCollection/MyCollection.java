package com.models.UserSavedGames.UserSavedCollection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.models.UserModel.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="collections")
public class MyCollection {
  @Id @GeneratedValue
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id", nullable=false)
  @JsonIgnore 
  private User user;

  @OneToMany(
    mappedBy="collection",
    cascade=CascadeType.ALL,
    orphanRemoval=true,
    fetch=FetchType.LAZY
  )
  @JsonManagedReference
  private List<CollectionItem> items = new ArrayList<>();

    // ─── Getters & setters ────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<CollectionItem> getItems() { return items; }
    public void setItems(List<CollectionItem> items) { this.items = items; }
}
