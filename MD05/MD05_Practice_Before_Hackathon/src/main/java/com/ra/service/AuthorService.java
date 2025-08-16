package com.ra.service;

import com.ra.model.Author;

import java.util.List;

public interface AuthorService extends PaginationService<Author>{
    boolean saveAuthor(Author author);

    boolean updateAuthor(Author author);

    boolean deleteAuthor(int id);

    List<Author> findAuthorByField(String field);

    Author findAuthorById(int id);

    boolean isAuthorNameUnique(int id, String name);
}
