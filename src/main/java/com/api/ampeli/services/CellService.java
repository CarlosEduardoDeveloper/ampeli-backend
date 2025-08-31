package com.api.ampeli.services;

import com.api.ampeli.model.Cell;
import com.api.ampeli.repository.CellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CellService {

    @Autowired
    private CellRepository cellRepository;

    public List<Cell> findAll() {
        return cellRepository.findAll();
    }

    public Optional<Cell> findById(Long id) {
        return cellRepository.findById(id);
    }

    public List<Cell> findByNameContaining(String name) {
        return cellRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Cell> findByDescriptionContaining(String keyword) {
        return cellRepository.findByDescriptionContaining(keyword);
    }

    public List<Cell> findByMemberId(Long memberId) {
        return cellRepository.findByMemberId(memberId);
    }

    public Cell save(Cell cell) {
        return cellRepository.save(cell);
    }

    public Cell update(Long id, Cell cellDetails) {
        Cell cell = cellRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cell not found with id: " + id));

        cell.setName(cellDetails.getName());
        cell.setDescription(cellDetails.getDescription());
        cell.setLeaderPhone(cellDetails.getLeaderPhone());

        return cellRepository.save(cell);
    }

    public void deleteById(Long id) {
        if (!cellRepository.existsById(id)) {
            throw new RuntimeException("Cell not found with id: " + id);
        }
        cellRepository.deleteById(id);
    }
}
