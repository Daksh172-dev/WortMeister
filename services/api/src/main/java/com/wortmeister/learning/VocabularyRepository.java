package com.wortmeister.learning;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<VocabularyItem, Long> {
    List<VocabularyItem> findTop20ByCefrLevelOrderByGermanAsc(String cefrLevel);
}
