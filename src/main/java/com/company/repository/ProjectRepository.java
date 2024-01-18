package com.company.repository;
import com.company.entity.Project;
import com.company.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> { // <entityName,primaryKey>

    Project findByProjectCode(String code);
    List<Project> findAllByAssignedManager(User manager);




}
