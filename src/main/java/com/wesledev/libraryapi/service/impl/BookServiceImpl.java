package com.wesledev.libraryapi.service.impl;

import com.wesledev.libraryapi.api.exception.BusinessException;
import com.wesledev.libraryapi.model.entity.Book;
import com.wesledev.libraryapi.model.repository.BookRepository;
import com.wesledev.libraryapi.service.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;

    @Override
    public Book save(Book book) {
        if(repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("Isbn já cadastrado.");
        }
        return repository.save(book);
    }
}
