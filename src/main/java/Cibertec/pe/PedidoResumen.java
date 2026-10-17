
package Cibertec.pe;

import java.math.BigDecimal;

public class PedidoResumen {

    private Integer id;
    private Integer usuarioId;
    private String fecha;
    private BigDecimal total;
    private String estado;
    private String estadoPago;

    public PedidoResumen() {
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}
