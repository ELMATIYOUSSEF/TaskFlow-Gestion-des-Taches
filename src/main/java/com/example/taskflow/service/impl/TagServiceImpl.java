package com.example.taskflow.service.impl;


import com.example.taskflow.Dto.TagDTO;
import com.example.taskflow.Entity.Tag;
import com.example.taskflow.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements com.example.taskflow.service.TagService {
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = modelMapper.map(tagDTO, Tag.class);
        Tag savedTag = tagRepository.save(tag);
        return modelMapper.map(savedTag ,TagDTO.class);
    }
    @Override
    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }
    @Override
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tag -> modelMapper.map(tag, TagDTO.class))
                .collect(Collectors.toList());
    }


}
