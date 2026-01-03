package com.codegym.service.customer;

import com.codegym.model.Topic;
import com.codegym.repository.ITopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService implements ITopicService {

    @Autowired
    private ITopicRepository itopicRepository;

    @Override
    public Iterable<Topic> findAll() {
        return itopicRepository.findAll();
    }

    @Override
    public Optional<Topic> findById(Long id) {
        return itopicRepository.findById(id);
    }

    @Override
    public void save(Topic topic) {
        itopicRepository.save(topic);
    }

    @Override
    public void remove(Long id) {
        itopicRepository.deleteById(id);
    }


    @Override
    public List<Topic> findByNameContaining(String keyword) {
        // Gọi hàm findByTitleContaining của Repository vì Entity dùng 'title'
        return itopicRepository.findByTitleContaining(keyword);
    }
}

