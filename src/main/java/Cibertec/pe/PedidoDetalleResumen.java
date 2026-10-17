
package Cibertec.pe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PedidoDetalleResumen {

    private Integer id;
    private Integer usuarioId;
    private String fecha;
    private BigDecimal total;
    private String estado;
    private String estadoPago;
    private String metodoPago;

    private List<DetallePedidoResumen> detalles = new ArrayList<>();

    public PedidoDetalleResumen() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // TOTAL DEL PEDIDO
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    // MÉTODO DE PAGO: YAPE, PLIN O TARJETA
    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<DetallePedidoResumen> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoResumen> detalles) {
        this.detalles = detalles != null
                ? detalles
                : new ArrayList<>();
    }
}
