package com.example.api.principal;
import com.example.api.model.ApiConexion;
import com.example.api.model.Autor;
import com.example.api.model.DatosLibro;
import com.example.api.model.Libro;
import com.example.api.repository.iAutorRepository;
import com.example.api.repository.iLibroRepository;
import com.example.api.service.ConsumoApi;
import com.example.api.service.ConvierteDatos;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos convertir = new ConvierteDatos();
    private static String API_BASE = "https://gutendex.com/books/?search=";
    private List<Libro> datosLibro = new ArrayList<>();
    private iLibroRepository libroRepository;
    private iAutorRepository autorRepository;

    public principal(iLibroRepository libroRepository, iAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    
                    ****     SELECCIONE UNA OPCIÓN   ****|
                    |*************************************
                    1 - Buscar Libro por Titulo en la API
                    2 - Listar Libros Guardados
                    3 - Listar Autores Guardados
                    4 - Listar Autores vivos en este Año
                    5 - Listar Libros por Idioma
                    0 - Salir
                    |************************************|
                    """;
            try {
                System.out.println(menu);
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("|  ingrese una Opcion válida.  |");
                System.out.println("|*****************************|\n");
                sc.nextLine();
                continue;
            }
            switch (opcion){
                case 1:
                    buscarLibroAPI();
                    break;
                case 2:
                    librosGuardados();
                    break;
                case 3:
                    autoresGuardados();
                    break;
                case 4:
                    autoresVivoAnio();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 0:
                    opcion = 0;
                    System.out.println("|  Feliz Dia   |");
                    System.out.println("|**************|\n");
                    break;
                default:
                    System.out.println("|  Opción Incorrecta. |");
                    System.out.println("|*********************|\n");
                    muestraElMenu();
                    break;
            }
        }
    }

    private Libro getDatosLibro(){
        System.out.println("Ingrese el nombre del libro: ");
        var nombreLibro = sc.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(API_BASE + nombreLibro.replace(" ", "%20"));
        ApiConexion datos = convertir.convertirDatosJsonAJava(json, ApiConexion.class);
            if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
                DatosLibro primerLibro = datos.getResultadoLibros().get(0);
                return new Libro(primerLibro);
            } else {
                System.out.println("No se encontraron resultados.");
                return null;
            }
    }

    private void buscarLibroAPI() {
        Libro libro = getDatosLibro();
        if (libro == null){
            System.out.println("Libro NO encontrado");
            return;
        }
        try{
            boolean libroExists = libroRepository.existsByTitulo(libro.getTitulo());
            if (libroExists){
                System.out.println("Este libro ya fue buscado!");
            }else {
                libroRepository.save(libro);
                System.out.println(libro.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("Ocurrio un Error!");
        }
    }

    @Transactional(readOnly = true)
    private void librosGuardados(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros encontrados en la base de datos:");
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }

    private  void autoresGuardados(){
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos. \n");
        } else {
            System.out.println("Libros encontrados en la base de datos: \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {
                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre()+'\n');
                }
            }
        }
    }

    private void autoresVivoAnio() {
        System.out.println("Ingrese el año a consultar \n");
        var anioBuscado = sc.nextInt();
        sc.nextLine();
        List<Autor> autoresVivos = autorRepository.findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(anioBuscado, anioBuscado);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores que estuvieran vivos en el año " + anioBuscado + ".");
        } else {
            System.out.println("Los autores registrados que vivian en " + anioBuscado + " eran:");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autoresVivos) {
                if (autor.getCumpleanios() != null && autor.getFechaFallecimiento() != null) {
                    if (autor.getCumpleanios() <= anioBuscado && autor.getFechaFallecimiento() >= anioBuscado) {
                        if (autoresUnicos.add(autor.getNombre())) {
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }

    private void  librosPorIdioma(){
        System.out.println("Los Idiomas que tenemos son: \n");
        System.out.println(" Español - es // Ingles - en");
        System.out.println("|*****************************\n");
        var idioma = sc.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en ese Idioma.");
        } else {
            System.out.println("Los libros encontrados son:");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }
    }
}
