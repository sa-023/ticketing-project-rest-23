package com.company.entity;
import com.company.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import javax.persistence.*;
/*
 * 🦋 @Where(clause = "is_deleted=false")
 * · Whatever repository is created from an entity that is annotated with @Where(clause = "is_deleted=false"), all the
 *   queries in that repository will automatically be concatenated with "is_deleted=false" as well.
 * · The User entity class is annotated with @Where(clause = "is_deleted=false"), and UserRepository accepts User entity.
 *   Ex: How the query will run in the background: SELECT * FROM TableName WHERE is_deleted = false
 *                                                 SELECT * FROM users WHERE is_deleted = false;
 *                                                 SELECT user_name FROM users WHERE is_deleted = false;
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@Where(clause = "is_deleted=false")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String userName;

    private String passWord;
    private boolean enabled;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;


}
