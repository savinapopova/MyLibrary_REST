package com.example.libraryapp.service;

import com.example.libraryapp.model.dto.AdminQuestionDTO;
import com.example.libraryapp.model.dto.MessageDTO;
import com.example.libraryapp.model.entity.Message;
import com.example.libraryapp.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private MessageRepository messageRepository;
    private ModelMapper modelMapper;

    public MessageService(MessageRepository messageRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }

    public void postMessage(Message messageRequest, String userEmail) {
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public Page<MessageDTO> getMessages(String userEmail, Pageable pageable) {
        return this.messageRepository.findByUserEmail(userEmail, pageable)
                .map(message -> this.modelMapper.map(message, MessageDTO.class));
    }

    public Page<MessageDTO> getAdminMessages(boolean closed, Pageable pageable) {
        return this.messageRepository.findByClosed(closed, pageable)
                .map(message -> this.modelMapper.map(message, MessageDTO.class));
    }

    public void putMessage(AdminQuestionDTO adminQuestionDTO, String userEmail) throws Exception {
        Optional<Message> optionalMessage = messageRepository.findById(adminQuestionDTO.getId());
        if (optionalMessage.isEmpty()) {
            throw new Exception("Message not found");
        }

        Message message = optionalMessage.get();

        message.setAdminEmail(userEmail);
        message.setResponse(adminQuestionDTO.getResponse());
        message.setClosed(true);
        messageRepository.save(message);
    }
}
