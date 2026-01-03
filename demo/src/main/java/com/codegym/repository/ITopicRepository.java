package com.codegym.repository;

import com.codegym.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ITopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByTitleContaining(String title); 
}
