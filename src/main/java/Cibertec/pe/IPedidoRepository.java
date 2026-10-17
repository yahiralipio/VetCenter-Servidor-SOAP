
package Cibertec.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IPedidoRepository
        extends JpaRepository<Pedido, Integer> {

    // Historial de pedidos de un cliente
    List<Pedido> findByUsuarioIdOrderByFechaDesc(
            Integer usuarioId
    );

    // Todos los pedidos para el administrador
    List<Pedido> findAllByOrderByFechaDesc();
}
