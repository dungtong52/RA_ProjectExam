package com.ra.repository;

import com.ra.model.Author;
import com.ra.model.PaginationResult;

import java.util.List;

public interface AuthorRepository {
    PaginationResult<Author> getAllAuthor(int size, int currentPage);

    boolean saveAuthor(Author author);

    boolean updateAuthor(Author author);

    boolean deleteAuthor(int id);

    List<Author> findAuthorByField(String field);

    Author findAuthorById(int id);

    boolean isAuthorNameUnique(int id, String name);
}
