package com.codegym.controller;

import com.codegym.model.Tag;
import com.codegym.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tags")
@CrossOrigin("*")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<Iterable<Tag>> getAll() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    // 2. Tạo mới (CREATE)
    @PostMapping
    public ResponseEntity<Tag> create(@RequestBody Tag tag) {
        tagService.save(tag);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    // 3. Tìm theo ID (GET ONE)
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable Long id) {
        Optional<Tag> tagOptional = tagService.findById(id);
        // Nếu tìm thấy trả về OK, không thấy trả về NOT_FOUND
        return tagOptional.map(tag -> new ResponseEntity<>(tag, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. Cập nhật (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@PathVariable Long id, @RequestBody Tag tag) {
        Optional<Tag> tagOptional = tagService.findById(id);
        if (!tagOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Gán ID từ URL vào object để đảm bảo update đúng dòng
        tag.setId(id);
        tagService.save(tag);
        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    // 5. Xóa (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Tag> delete(@PathVariable Long id) {
        Optional<Tag> tagOptional = tagService.findById(id);
        if (!tagOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        tagService.remove(id);
        return new ResponseEntity<>(tagOptional.get(), HttpStatus.OK);
    }
}





