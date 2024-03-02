package com.wesledev.libraryapi.api.resource;

import com.wesledev.libraryapi.api.dto.BookDTO;
import com.wesledev.libraryapi.api.dto.LoanDTO;
import com.wesledev.libraryapi.api.dto.LoanFilterDTO;
import com.wesledev.libraryapi.api.dto.ReturnedLoanDTO;
import com.wesledev.libraryapi.model.entity.Book;
import com.wesledev.libraryapi.model.entity.Loan;
import com.wesledev.libraryapi.service.BookService;
import com.wesledev.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;
    private final BookService bookService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Long create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsbn(
                        dto.getIsbn())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));
        Loan entity = Loan.builder()
                .book(book)
                .customer(dto.getCustomer())
                .loanDate(LocalDate.now())
                .build();

        entity = service.save(entity);
        return entity.getId();
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
        Loan loan = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        loan.setReturned(dto.getReturned());

        service.update(loan);
    }

    @GetMapping
    Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageRequest) {
        Book filter = modelMapper.map(dto, Book.class);
        Page<Loan> result = service.find(dto, pageRequest);
        List<LoanDTO> loans = result
                .getContent()
                .stream()
                .map(entity -> {

                    Book book = entity.getBook();
                    BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
                    return loanDTO;

                }).collect(Collectors.toList());

        return new PageImpl<LoanDTO>(loans, pageRequest, result.getTotalElements());
    }
}
