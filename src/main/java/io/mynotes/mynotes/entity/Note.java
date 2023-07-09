package io.mynotes.mynotes.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_notes", schema = "dev")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(OffsetDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", user_id='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", created_at=" + createdAt +
                ", modified_at=" + modifiedAt +
                '}';
    }
}
