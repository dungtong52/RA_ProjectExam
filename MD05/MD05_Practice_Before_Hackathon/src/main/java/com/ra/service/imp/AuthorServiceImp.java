package com.ra.service.imp;

import com.ra.model.Author;
import com.ra.model.PaginationResult;
import com.ra.repository.AuthorRepository;
import com.ra.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImp implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImp(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean saveAuthor(Author author) {
        return authorRepository.saveAuthor(author);
    }

    @Override
    public boolean updateAuthor(Author author) {
        return authorRepository.updateAuthor(author);
    }

    @Override
    public boolean deleteAuthor(int id) {
        return authorRepository.deleteAuthor(id);
    }

    @Override
    public List<Author> findAuthorByField(String field) {
        return authorRepository.findAuthorByField(field);
    }

    @Override
    public Author findAuthorById(int id) {
        return authorRepository.findAuthorById(id);
    }

    @Override
    public boolean isAuthorNameUnique(int id, String name) {
        return authorRepository.isAuthorNameUnique(id, name);
    }

    @Override
    public PaginationResult<Author> getPaginationData(int size, int currentPage) {
        return authorRepository.getAllAuthor(size, currentPage);
    }
}
