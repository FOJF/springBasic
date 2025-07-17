package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();
    public static Long id = 1L;

    public void save(Author author) {
        this.authorList.add(author);
        id++;
    }

    public List<Author> findAll() {
        return this.authorList;
    }

    public Optional<Author> findById(Long id) {
        return this.authorList.stream().filter(author -> author.getId().equals(id)).findFirst();
    }

    public Optional<Author> findByEmail(String email) {
        return this.authorList.stream().filter(author -> author.getEmail().equals(email)).findFirst();
    }

    public void delete(Long id) {
        for (int i = 0; i < this.authorList.size(); i++) {
            if (this.authorList.get(i).getId().equals(id)) {
                this.authorList.remove(i);
                break;
            }
        }
    }
}
