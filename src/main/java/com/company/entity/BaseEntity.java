package com.company.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
/*
 * 🖍️...
 * · @PrePersist: Used to mark a method in an entity class that should be executed just before the entity is persisted (saved) for the first time in the database.
 * · @PreUpdate: Used to mark a method in an entity class that should be executed just before an entity is updated in the database.
 * · @Column(nullable = false,updatable = false): It will prevent overriding (updating) the values in the database when we perform the update.
 *   Ex: To prevent an override of "insertDateTime" and "insertUserId" that becomes null, when we update the data.
 * · @Column(nullable = false): It should not be null.
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime insertDateTime;
    @Column(nullable = false, updatable = false)
    private Long insertUserId;
    @Column(nullable = false)
    private LocalDateTime lastUpdateDateTime;
    @Column(nullable = false)
    private Long lastUpdateUserId;
    /*
     * 🖍️...
     * · The isDeleted field is for flagging data. When data will be deleted from the UI but still be available in the database.
     * · private Boolean isDeleted = false; will be used with @Where(clause = "is_deleted=false") in child entity classes.
     */
    private Boolean isDeleted = false;

    @PrePersist
    public void onPrePersist() {
        this.insertDateTime = LocalDateTime.now();
        this.lastUpdateDateTime = LocalDateTime.now();
        this.insertUserId = 1L;
        this.lastUpdateUserId = 1L;
        /*
         * 🖍️...
         * · insertDateTime, insertUserId, lastUpdateDateTime, and lastUpdateUserId will be automatically created by Spring.
         * · When we apply security, following fields will come from the database: insertUserId, lastUpdateUserId
         * · Whenever we save new data on the database first time, @PrePersist annotated method will be executed.
         */
    }

    @PreUpdate // Whenever we update data on the database, @PreUpdate annotated method will be executed.
    public void onPreUpdate() {
        this.lastUpdateDateTime = LocalDateTime.now();
        this.lastUpdateUserId = 1L;
    }




}
