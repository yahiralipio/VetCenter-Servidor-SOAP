
package Cibertec.pe;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface IPedidoService {

    @WebMethod
    String crearPedido(
            @WebParam(name = "usuarioId") Integer usuarioId,
            @WebParam(name = "items") List<ItemCompra> items
    );

    @WebMethod
    List<PedidoResumen> listarPedidosUsuario(
            @WebParam(name = "usuarioId") Integer usuarioId
    );

    @WebMethod
    PedidoDetalleResumen obtenerDetallePedido(
            @WebParam(name = "pedidoId") Integer pedidoId,
            @WebParam(name = "usuarioId") Integer usuarioId
    );

    @WebMethod
    List<PedidoResumen> listarTodosPedidos();

    @WebMethod
    PedidoDetalleResumen obtenerDetallePedidoAdmin(
            @WebParam(name = "pedidoId") Integer pedidoId
    );

    @WebMethod
    String actualizarEstadoPedido(
            @WebParam(name = "pedidoId") Integer pedidoId,
            @WebParam(name = "nuevoEstado") String nuevoEstado
    );
    @WebMethod
    String crearPedidoConPago(
            @WebParam(name = "usuarioId") Integer usuarioId,
            @WebParam(name = "items") List<ItemCompra> items,
            @WebParam(name = "metodoPago") String metodoPago
    );
}
