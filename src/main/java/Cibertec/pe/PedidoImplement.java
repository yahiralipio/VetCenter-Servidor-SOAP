
package Cibertec.pe;

import jakarta.jws.WebService;
import java.util.List;

@WebService(
        endpointInterface = "Cibertec.pe.IPedidoService",
        serviceName = "PedidoImplementService"
)
public class PedidoImplement implements IPedidoService {

    private final PedidoTransaccionService transacciones;

    public PedidoImplement(
            PedidoTransaccionService transacciones) {
        this.transacciones = transacciones;
    }

    @Override
    public String crearPedido(
            Integer usuarioId,
            List<ItemCompra> items) {

        try {
            return transacciones.registrarPedido(
                    usuarioId, items
            );
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Override
    public List<PedidoResumen> listarPedidosUsuario(
            Integer usuarioId) {

        return transacciones.listarPorUsuario(usuarioId);
    }

    @Override
    public PedidoDetalleResumen obtenerDetallePedido(
            Integer pedidoId,
            Integer usuarioId) {

        return transacciones.obtenerDetallePedido(
                pedidoId, usuarioId
        );
    }

    @Override
    public List<PedidoResumen> listarTodosPedidos() {
        return transacciones.listarTodosPedidos();
    }

    @Override
    public PedidoDetalleResumen obtenerDetallePedidoAdmin(
            Integer pedidoId) {

        return transacciones.obtenerDetallePedidoAdmin(
                pedidoId
        );
    }
    @Override
    public String actualizarEstadoPedido(
            Integer pedidoId,
            String nuevoEstado) {

        try {
            return transacciones.actualizarEstadoPedido(
                    pedidoId,
                    nuevoEstado
            );
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
    @Override
    public String crearPedidoConPago(
            Integer usuarioId,
            List<ItemCompra> items,
            String metodoPago) {

        try {
            return transacciones.registrarPedidoConPago(
                    usuarioId,
                    items,
                    metodoPago
            );
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
