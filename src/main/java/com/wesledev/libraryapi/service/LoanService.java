package com.wesledev.libraryapi.service;

import com.wesledev.libraryapi.api.dto.LoanFilterDTO;
import com.wesledev.libraryapi.api.resource.BookController;
import com.wesledev.libraryapi.model.entity.Book;
import com.wesledev.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);

    List<Loan> getAllLateLocans();
}
