package com.example.taskflow.service;

import com.example.taskflow.Dto.TagDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface TagService {
    TagDTO createTag(TagDTO tagDTO);

    void deleteTag(Long tagId);

    List<TagDTO> getAllTags();
}
