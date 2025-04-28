package com.models.UserSavedGames.UserSavedCollection;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name="collection_items")
public class CollectionItem {
  @Id @GeneratedValue
  private Long id;

  @Column(name="game_id", nullable=false)
  private Long gameId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="collection_id", nullable=false)
  @JsonBackReference
  private MyCollection collection;
    // ─── Getters & setters ────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MyCollection getCollection() { return collection; }
    public void setCollection(MyCollection collection) { this.collection = collection; }

    public Long getGameId() { return gameId; }
    public void setGameId(Long gameId) { this.gameId = gameId; }
}
