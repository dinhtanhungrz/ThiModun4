package com.codegym.service.tag;

import com.codegym.model.Tag;
import com.codegym.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired; // Nhớ import cái này
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService implements ITagService {

    // 1. Cần tiêm phụ thuộc vào đây (Dùng @Autowired hoặc Constructor)
    @Autowired
    private ITagRepository tagRepository;

    @Override
    public Iterable<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Optional<Tag> findById(Long id) {
        // 2. Gọi hàm findById của Repo
        return tagRepository.findById(id);
    }

    @Override
    public void save(Tag tag) {
        // 3. Gọi hàm save của Repo
        tagRepository.save(tag);
    }

    @Override
    public void remove(Long id) {
        // 4. Gọi hàm deleteById của Repo
        tagRepository.deleteById(id);
    }
}