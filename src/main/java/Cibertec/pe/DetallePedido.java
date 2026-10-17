
package Cibertec.pe;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_detalle_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Pedido al que pertenece el detalle
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // Producto (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "producto_id",
            nullable = true
    )
    private Producto producto;

    // Medicamento (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "medicamento_id",
            nullable = true,
            foreignKey = @ForeignKey(
                    name = "fk_detalle_medicamento"
            )
    )
    private Medicamento medicamento;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(
            name = "precio_unitario",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal precioUnitario;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal subtotal;

    public DetallePedido() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // Validación: producto O medicamento
    @PrePersist
    @PreUpdate
    private void validarTipoArticulo() {

        boolean tieneProducto = producto != null;
        boolean tieneMedicamento = medicamento != null;

        if (tieneProducto == tieneMedicamento) {
            throw new IllegalStateException(
                    "El detalle debe contener un producto " +
                            "o un medicamento, pero no ambos."
            );
        }
    }
}
