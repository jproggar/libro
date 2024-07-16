package com.example.api.repository;
import com.example.api.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface iLibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTitulo(String titulo);
    List<Libro> findByIdioma(String idioma);
}
