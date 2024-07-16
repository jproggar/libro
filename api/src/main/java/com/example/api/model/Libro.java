package com.example.api.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long libroId;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Long cantidadDescargas;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() { }
    public Libro(Libro libro) { }
    public Libro(DatosLibro datosLibro) {
        this.libroId = datosLibro.libroId();
        this.titulo = datosLibro.titulo();
        if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
            this.autor = new Autor(datosLibro.autor().get(0)); // Toma el primer autor de la lista
        } else {
            this.autor = null; // o maneja el caso de que no haya autor
        }
        this.idioma = idiomaModificado(datosLibro.idioma());
        this.cantidadDescargas = datosLibro.cantidadDescargas();
    }
    private String idiomaModificado(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Desconocido";
        }
        return idiomas.get(0);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getLibroId() {
        return libroId;
    }
    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutores() {
        return autor;
    }
    public void setAutores(Autor autores) {
        this.autor = autores;
    }

    public String getIdioma() {
        return idioma;
    }
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getCantidadDescargas() {
        return cantidadDescargas;
    }
    public void setCantidadDescargas(Long cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

    @Override
    public String toString() {
        return
                "  \nid=" + id +
                "  \nLibro id=" + libroId +
                ", \ntitulo='" + titulo + '\'' +
                ", \nauthors=" + (autor != null ? autor.getNombre() : "N/A")+
                ", \nidioma=" + idioma +
                ", \ncantidadDescargas=" + cantidadDescargas;
    }
}
