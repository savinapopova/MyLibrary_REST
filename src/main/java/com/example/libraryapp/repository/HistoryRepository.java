package com.example.libraryapp.repository;

import com.example.libraryapp.model.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

//    Page<History> findBooksByUserEmail(@RequestParam String userEmail, Pageable pageable);
    Page<History> findAllByUserEmail(String userEmail, Pageable pageable);

}
