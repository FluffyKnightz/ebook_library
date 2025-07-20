package com.fluffyknightz.ebook_library.modules.author.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorRepository;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorViewRepository;
import com.fluffyknightz.ebook_library.modules.author.service.AuthorService;
import com.fluffyknightz.ebook_library.modules.author.view.AuthorView;
import com.fluffyknightz.ebook_library.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorViewRepository authorViewRepository;

    @Override
    public Author save(User user, AuthorDTO authorDTO) throws IOException {

        Author author = new Author(authorDTO.name(), authorDTO.description(), authorDTO.nationality(),
                                   authorDTO.birthedDate(), LocalDate.now(), user, LocalDate.now(), user, false);
        try {
            return authorRepository.save(author);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateKeyException("Author with username: " + authorDTO.name() + " already exists");
        }
    }

    @Override
    public Page<AuthorView> findAll(String search, Pageable pageable) {
        return authorViewRepository.findAllForTable(search, pageable);
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
        author.setName(authorDTO.name());
        author.setDescription(authorDTO.description());
        author.setNationality(authorDTO.nationality());
        author.setBirthedDate(authorDTO.birthedDate());
        author.setUpdatedDate(LocalDate.now());
        author.setUpdatedUser(user);
        return authorRepository.save(author);
    }
}
