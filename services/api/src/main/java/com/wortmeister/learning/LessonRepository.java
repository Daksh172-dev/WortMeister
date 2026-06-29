package com.wortmeister.learning;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByPublicId(String publicId);
}
