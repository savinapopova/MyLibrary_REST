package com.example.libraryapp.web;

import com.example.libraryapp.model.dto.HistoryDTO;
import com.example.libraryapp.service.HistoryService;
import com.example.libraryapp.utils.ExtractJWT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class HistoryController {

    private HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("api/histories/search/findBooksByUserEmail")
    public ResponseEntity<Page<HistoryDTO>> getHistory(@RequestParam("userEmail") String userEmail,
                                                       Pageable pageable) {
//
        Page<HistoryDTO> historyPage = historyService.findBooksByUserEmail(userEmail, pageable);
        return ResponseEntity.ok(historyPage);
    }
}
