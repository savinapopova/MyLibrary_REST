package com.example.libraryapp.web;

import com.example.libraryapp.model.dto.AdminQuestionDTO;
import com.example.libraryapp.model.dto.MessageDTO;
import com.example.libraryapp.model.entity.Message;
import com.example.libraryapp.responseModels.ShelfCurrentLoansResponse;
import com.example.libraryapp.service.MessageService;
import com.example.libraryapp.utils.ExtractJWT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/secure/add/message")
    public void postMessage(@RequestHeader(value = "Authorization") String token,
                            @RequestBody Message messageRequest ) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        messageService.postMessage(messageRequest, userEmail);

    }

    @GetMapping("/search/findByUserEmail")
    public ResponseEntity<Page<MessageDTO>> getMessages(@RequestHeader(value = "Authorization")
                                                        String token, Pageable pageable)
            throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        Page<MessageDTO> messages = this.messageService.getMessages(userEmail, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/search/findByClosed")
    public ResponseEntity<Page<MessageDTO>> getAdminMessages(@RequestParam("closed") boolean closed,
                                                             Pageable pageable) {

        Page<MessageDTO> messages = this.messageService.getAdminMessages(closed, pageable);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/secure/admin/message")
    public void putMessage(@RequestHeader(value = "Authorization") String token,
                           @RequestBody AdminQuestionDTO adminQuestionDTO) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        messageService.putMessage(adminQuestionDTO, userEmail);
    }
}
