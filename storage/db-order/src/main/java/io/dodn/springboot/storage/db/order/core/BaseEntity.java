package io.dodn.springboot.storage.db.order.core;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @Column
    private Long created;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
    private Long updated;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getCreated() {
        return created;
    }

    public Long getUpdated() {
        return updated;
    }

}
