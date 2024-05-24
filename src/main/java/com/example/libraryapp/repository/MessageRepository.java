package com.example.libraryapp.repository;

import com.example.libraryapp.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

//    Page<Message> findByUserEmail(@RequestParam("userEmail") String userEmail, Pageable pageable);
//    Page<Message> findByClosed(@RequestParam("closed") boolean closed, Pageable pageable);
    Page<Message> findByUserEmail(String userEmail, Pageable pageable);
    Page<Message> findByClosed(boolean closed, Pageable pageable);
}
