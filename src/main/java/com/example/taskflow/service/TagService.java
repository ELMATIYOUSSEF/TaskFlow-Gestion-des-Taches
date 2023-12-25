package com.example.taskflow.service;

import com.example.taskflow.Dto.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO createTag(TagDTO tagDTO);

    void deleteTag(Long tagId);

    List<TagDTO> getAllTags();
}
