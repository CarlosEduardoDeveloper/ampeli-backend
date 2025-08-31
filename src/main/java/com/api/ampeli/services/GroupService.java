package com.api.ampeli.services;

import com.api.ampeli.model.Group;
import com.api.ampeli.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    public List<Group> findByNameContaining(String name) {
        return groupRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Group> findByDescriptionContaining(String keyword) {
        return groupRepository.findByDescriptionContaining(keyword);
    }

    public List<Group> findByMemberId(Long memberId) {
        return groupRepository.findByMemberId(memberId);
    }

    public Group save(Group group) {
        return groupRepository.save(group);
    }

    public Group update(Long id, Group groupDetails) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));

        group.setName(groupDetails.getName());
        group.setDescription(groupDetails.getDescription());
        group.setLeaderPhone(groupDetails.getLeaderPhone());

        return groupRepository.save(group);
    }

    public void deleteById(Long id) {
        if (!groupRepository.existsById(id)) {
            throw new RuntimeException("Group not found with id: " + id);
        }
        groupRepository.deleteById(id);
    }
}
