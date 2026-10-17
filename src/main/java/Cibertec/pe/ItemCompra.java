
package Cibertec.pe;

public class ItemCompra {

    private Integer productoId;
    private Integer medicamentoId;
    private Integer cantidad;

    public ItemCompra() {
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Integer medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public boolean esProducto() {
        return productoId != null && medicamentoId == null;
    }

    public boolean esMedicamento() {
        return medicamentoId != null && productoId == null;
    }

    public boolean esValido() {
        return cantidad != null
                && cantidad > 0
                && (esProducto() || esMedicamento());
    }
}
