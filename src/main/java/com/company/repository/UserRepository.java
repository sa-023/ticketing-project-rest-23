package com.company.repository;
import com.company.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/*
 * 🖍️...
 * · Persisting and deleting objects in JPA requires a transaction. That’s why we should use a @Transactional annotation when using
 *   these derived delete queries, to make sure a transaction is running.
 * · For derive queries, we use the @Transactional annotation. We can use this annotation at the class level as well. Ex: Check UserServiceImpl class.
 * · For JPQL and Native queries, we use the @Modifying annotation.
 *
 * 🖍️...
 * · Whatever repository is created from an entity that is annotated with @Where(clause = "is_deleted=false"), all the
 *   queries in that repository will automatically be concatenated with "is_deleted=false" as well.
 * · The User entity class is annotated with @Where(clause = "is_deleted=false"), and UserRepository accepts User entity.
 *   Ex: How the query will run in the background: SELECT * FROM TableName WHERE is_deleted = false
 *                                                 SELECT * FROM users WHERE is_deleted = false;
 *                                                 SELECT user_name FROM users WHERE is_deleted = false;
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> { // <entityName,primaryKey>

    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);
    User findByUserName(String username); //
    User findByUserNameAndIsDeleted(String username, Boolean deleted);
    @Transactional
    void deleteByUserName(String username);
    List<User> findAllByRoleDescriptionIgnoreCase(String description); //
    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String description, Boolean deleted);




}
