package com.fluffyknightz.ebook_library.modules.author.service_implement;

import com.fluffyknightz.ebook_library.exception.ResourceNotFoundException;
import com.fluffyknightz.ebook_library.modules.author.dto.AuthorDTO;
import com.fluffyknightz.ebook_library.modules.author.entity.Author;
import com.fluffyknightz.ebook_library.modules.author.repository.AuthorRepository;
import com.fluffyknightz.ebook_library.modules.author.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author save(AuthorDTO authorDTO) {
        Author author = new Author(authorDTO.name(), authorDTO.description(), LocalDate.now(), null, LocalDate.now(), null, false);
        return authorRepository.save(author);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author findById(String id) {
        return authorRepository.findById(id)
                               .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    @Override
    public void delete(String id) {
        Author author = authorRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        author.setDeleted(true);
        authorRepository.save(author);
    }

    @Override
    public Author update(AuthorDTO authorDTO) {
        Author author = authorRepository.findById(authorDTO.id())
                                        .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorDTO.id()));
        author.setName(authorDTO.name());
        author.setDescription(authorDTO.description());
        author.setUpdatedDate(LocalDate.now());
        author.setUpdatedUser(null);
        return authorRepository.save(author);
    }
}
