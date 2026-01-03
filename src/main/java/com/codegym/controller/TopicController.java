package com.codegym.controller;

import com.codegym.model.Topic;
import com.codegym.service.customer.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/topics")
@CrossOrigin("*")
public class TopicController {

    @Autowired
    private ITopicService topicService;
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Topic topic) {
        topicService.save(topic);
        ResponseEntity<String> res = new ResponseEntity<>("Add successfully", HttpStatus.CREATED);
        return res;
    }

        @GetMapping("")
        public ResponseEntity<Iterable<Topic>> getAll(@RequestParam(name = "q",defaultValue = "")String keyword) {
            Iterable<Topic> list =  topicService.findByNameContaining(keyword);
            ResponseEntity<Iterable<Topic>> res =
                    new ResponseEntity<>(list, HttpStatus.OK);
            return res;
        }

    @GetMapping("{id}")
    public ResponseEntity<Topic> getOne(
            @PathVariable(name = "id") Long id) {

        Optional<Topic> topic = topicService.findById(id);
        ResponseEntity<Topic> res =
                new ResponseEntity<>(topic.get(), HttpStatus.OK);
        return res;
    }

    // PUT - update full
    @PutMapping("{id}")
    public ResponseEntity<String> update(
            @PathVariable(name = "id") Long id,
            @RequestBody Topic topic) {

        topic.setId(id);
        topicService.save(topic);
        ResponseEntity<String> res =
                new ResponseEntity<>("Update successfully", HttpStatus.OK);
        return res;
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(
            @PathVariable(name = "id") Long id) {

        topicService.remove(id);
        ResponseEntity<String> res =
                new ResponseEntity<>("Delete successfully", HttpStatus.OK);
        return res;
    }

}