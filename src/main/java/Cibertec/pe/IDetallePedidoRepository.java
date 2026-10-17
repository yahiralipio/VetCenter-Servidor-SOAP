
package Cibertec.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IDetallePedidoRepository
        extends JpaRepository<DetallePedido, Integer> {

    List<DetallePedido> findByPedido_IdOrderByIdAsc(
            Integer pedidoId
    );
}
