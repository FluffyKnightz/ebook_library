package com.fluffyknightz.ebook_library.modules.author.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorRepository;
import com.fluffyknightz.ebook_library.modules.author.service.AuthorService;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author save(User user, AuthorDTO authorDTO) {

        Author author = new Author(authorDTO.name(), authorDTO.description(), LocalDate.now(), user, LocalDate.now(),
                                   user, false);
        try {
            return authorRepository.save(author);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException("Author with username: " + authorDTO.name() + " already exists");
        }
    }

    @Override
    public Page<Author> findAll(Pageable pageable) {

        return authorRepository.findAll(pageable);
    }

    @Override
    public Author findById(String id) {
        return authorRepository.findById(id)
                               .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        Author author = findById(id);
        author.setDeleted(true);
        authorRepository.save(author);
    }

    @Override
    public Author update(User user, AuthorDTO authorDTO) {
        Author author = findById(authorDTO.id());
        author.setName(authorDTO.name() != null ? authorDTO.name() : author.getName());
        author.setDescription(authorDTO.description());
        author.setUpdatedDate(LocalDate.now());
        author.setUpdatedUser(user);
        author.setNationality(authorDTO.nationality() != null ? authorDTO.nationality() : author.getNationality());
        return authorRepository.save(author);
    }
}
