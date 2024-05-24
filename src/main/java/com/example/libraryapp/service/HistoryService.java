package com.example.libraryapp.service;

import com.example.libraryapp.model.dto.HistoryDTO;
import com.example.libraryapp.model.entity.History;
import com.example.libraryapp.repository.HistoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private HistoryRepository historyRepository;
    private ModelMapper modelMapper;

    public HistoryService(HistoryRepository historyRepository, ModelMapper modelMapper) {
        this.historyRepository = historyRepository;
        this.modelMapper = modelMapper;
    }

    public Page<HistoryDTO> findBooksByUserEmail(String userEmail, Pageable pageable) {
        Page<History> historyPage = historyRepository.findAllByUserEmail(userEmail, pageable);
        return historyPage.map(history -> this.modelMapper.map(history, HistoryDTO.class));
    }
}
