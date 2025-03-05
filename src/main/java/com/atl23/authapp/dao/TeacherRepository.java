package com.atl23.authapp.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherEntity,Long> {

 Optional <TeacherEntity> findByUsername(String username);
}
