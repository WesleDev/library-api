package com.wesledev.libraryapi.model.repository;

import com.wesledev.libraryapi.model.entity.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir  um livro na base com o isbn informado.")
    void returnTrueWhenIsbnExists() {
        //cenario

        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        //execucao
        boolean exists = repository.existsByIsbn(isbn);

        //verificacao
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("Deve retornar false quando nao existir  um livro na base com o isbn informado.")
    void returnFalseWhenIsbnDoesntExists() {
        //cenario

        String isbn = "123";

        //execucao
        boolean exists = repository.existsByIsbn(isbn);

        //verificacao
        assertThat(exists).isFalse();

    }

    @Test
    @DisplayName("Deve obter um livro por id.")
    void findByIdTest() {

        //cenario
        Book book = createNewBook("123");
        entityManager.persist(book);

        //execucao
        Optional<Book> foundBook = repository.findById(book.getId());

        //verificacao
        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um livro.")
    void saveBookTest() {

        Book book = createNewBook("123");

        Book savedBook = repository.save(book);

        assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    void deleteBookTest() {
        //cenario
        Book book = createNewBook("123");
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());

        repository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());
        assertThat(deletedBook).isNull();
    }

    public static Book createNewBook(String isbn) {
        return Book.builder().title("Teste").author("Teste").isbn(isbn).build();
    }
}
