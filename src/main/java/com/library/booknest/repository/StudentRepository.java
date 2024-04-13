package com.library.booknest.repository;

import org.springframework.stereotype.Repository;

import com.library.booknest.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUsername(String username);
}
