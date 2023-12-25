package com.example.taskflow.service.impl;


import com.example.taskflow.Dto.TagDTO;
import com.example.taskflow.Entity.Tag;
import com.example.taskflow.mapper.TagMapper;
import com.example.taskflow.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements com.example.taskflow.service.TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = tagMapper.dtoToEntity(tagDTO);
        Tag savedTag = tagRepository.save(tag);
        return tagMapper.entityToDto(savedTag);
    }
    @Override
    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }
    @Override
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream()
                .map(tagMapper::entityToDto)
                .collect(Collectors.toList());
    }

}
