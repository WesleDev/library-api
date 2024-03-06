package com.wesledev.libraryapi.api.resource;

import com.wesledev.libraryapi.api.dto.BookDTO;
import com.wesledev.libraryapi.api.dto.LoanDTO;
import com.wesledev.libraryapi.model.entity.Book;
import com.wesledev.libraryapi.model.entity.Loan;
import com.wesledev.libraryapi.service.BookService;
import com.wesledev.libraryapi.service.LoanService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService service;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BookDTO create(@RequestBody @Valid BookDTO dto) {
        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }

    @GetMapping("{id}")
    BookDTO get(@PathVariable Long id) {
        return service.getById(id).map(book -> modelMapper.map(book, BookDTO.class)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("{id}")
    BookDTO update(@PathVariable Long id, BookDTO dto) {
        return service.getById(id).map(book -> {
            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getAuthor());
            book = service.update(book);
            return modelMapper.map(book, BookDTO.class);

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    Page<BookDTO> find(BookDTO dto, Pageable pageRequest) {
        Book filter = modelMapper.map(dto, Book.class);
        Page<Book> result = service.find(filter, pageRequest);
        List<BookDTO> list = result.getContent().stream().map(entity -> modelMapper.map(entity, BookDTO.class)).collect(Collectors.toList());

        return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(book);
    }

    @GetMapping("{id}/loans")
    Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {
        Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Page<Loan> result = loanService.getLoansByBook(book, pageable);
        List<LoanDTO> list = result.getContent()
                .stream()
                .map(loan -> {
                    Book loanBook = loan.getBook();
                    BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
                    LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
                    loanDTO.setBook(bookDTO);
                    return loanDTO;
                }).collect(Collectors.toList());
        return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
    }
}
