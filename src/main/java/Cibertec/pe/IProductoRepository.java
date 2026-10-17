package Cibertec.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IProductoRepository
        extends JpaRepository<Producto, Integer> {

    List<Producto> findByActivoTrueOrderByNombreAsc();

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(
            String nombre
    );

    List<Producto> findByCategoriaIdAndActivoTrue(
            Integer categoriaId
    );
}
