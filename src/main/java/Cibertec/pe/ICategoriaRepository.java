package Cibertec.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ICategoriaRepository
        extends JpaRepository<Categoria, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<Categoria> findAllByOrderByNombreAsc();
}
