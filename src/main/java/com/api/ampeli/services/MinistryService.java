package com.api.ampeli.services;

import com.api.ampeli.model.Ministry;
import com.api.ampeli.repository.MinistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MinistryService {

    @Autowired
    private MinistryRepository ministryRepository;

    public List<Ministry> findAll() {
        return ministryRepository.findAll();
    }

    public Optional<Ministry> findById(Long id) {
        return ministryRepository.findById(id);
    }

    public List<Ministry> findByNameContaining(String name) {
        return ministryRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Ministry> findByDescriptionContaining(String keyword) {
        return ministryRepository.findByDescriptionContaining(keyword);
    }

    public List<Ministry> findByMemberId(Long memberId) {
        return ministryRepository.findByMemberId(memberId);
    }

    public Ministry save(Ministry ministry) {
        return ministryRepository.save(ministry);
    }

    public Ministry update(Long id, Ministry ministryDetails) {
        Ministry ministry = ministryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ministry not found with id: " + id));

        ministry.setName(ministryDetails.getName());
        ministry.setDescription(ministryDetails.getDescription());
        ministry.setLeaderPhone(ministryDetails.getLeaderPhone());

        return ministryRepository.save(ministry);
    }

    public void deleteById(Long id) {
        if (!ministryRepository.existsById(id)) {
            throw new RuntimeException("Ministry not found with id: " + id);
        }
        ministryRepository.deleteById(id);
    }
}
