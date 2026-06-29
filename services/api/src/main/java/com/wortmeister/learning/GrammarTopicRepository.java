package com.wortmeister.learning;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarTopicRepository extends JpaRepository<GrammarTopic, Long> {
    List<GrammarTopic> findByCefrLevelOrderByTitleAsc(String cefrLevel);
}
