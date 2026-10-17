
package Cibertec.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoriaMedicamentoRepository
        extends JpaRepository<CategoriaMedicamento, Integer> {

    Optional<CategoriaMedicamento> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
