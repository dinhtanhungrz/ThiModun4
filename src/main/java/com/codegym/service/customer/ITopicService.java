package com.codegym.service.customer;

import com.codegym.model.Topic;
import com.codegym.service.IGenerateService;

import java.util.List;

public interface ITopicService extends IGenerateService<Topic> {
    List<Topic> findByNameContaining (String keyword);
}
