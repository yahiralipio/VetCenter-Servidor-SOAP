
package Cibertec.pe;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Service
public class PedidoTransaccionService {

    private final EntityManager entityManager;
    private final IPedidoRepository pedidoRepository;
    private final IDetallePedidoRepository detalleRepository;

    public PedidoTransaccionService(
            EntityManager entityManager,
            IPedidoRepository pedidoRepository,
            IDetallePedidoRepository detalleRepository) {

        this.entityManager = entityManager;
        this.pedidoRepository = pedidoRepository;
        this.detalleRepository = detalleRepository;
    }

    @Transactional
    public String registrarPedidoConPago(
            Integer usuarioId,
            List<ItemCompra> items,
            String metodoPago) {

        if (metodoPago == null ||
                !Set.of("YAPE", "PLIN", "TARJETA").contains(
                        metodoPago.trim().toUpperCase(Locale.ROOT))) {

            throw new IllegalArgumentException(
                    "Método de pago inválido. Selecciona YAPE, PLIN o TARJETA."
            );
        }

        return registrarPedidoInterno(
                usuarioId,
                items,
                metodoPago.trim().toUpperCase(Locale.ROOT)
        );
    }

    @Transactional
    public String registrarPedido(
            Integer usuarioId,
            List<ItemCompra> items) {

        return registrarPedidoInterno(usuarioId, items, null);
    }

    private String registrarPedidoInterno(
            Integer usuarioId,
            List<ItemCompra> items,
            String metodoPago) {

        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("Usuario inválido.");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido no tiene artículos."
            );
        }

        Map<Integer, Integer> productos = new TreeMap<>();
        Map<Integer, Integer> medicamentos = new TreeMap<>();

        for (ItemCompra item : items) {

            if (item == null ||
                    item.getCantidad() == null ||
                    item.getCantidad() <= 0) {

                throw new IllegalArgumentException(
                        "Artículo o cantidad inválida."
                );
            }

            boolean tieneProducto = item.getProductoId() != null;
            boolean tieneMedicamento = item.getMedicamentoId() != null;

            if (tieneProducto == tieneMedicamento) {
                throw new IllegalArgumentException(
                        "Cada artículo debe indicar un producto " +
                                "o un medicamento, pero no ambos."
                );
            }

            if (tieneProducto) {

                if (item.getProductoId() <= 0) {
                    throw new IllegalArgumentException(
                            "Identificador de producto inválido."
                    );
                }

                productos.merge(
                        item.getProductoId(),
                        item.getCantidad(),
                        Math::addExact
                );

            } else {

                if (item.getMedicamentoId() <= 0) {
                    throw new IllegalArgumentException(
                            "Identificador de medicamento inválido."
                    );
                }

                medicamentos.merge(
                        item.getMedicamentoId(),
                        item.getCantidad(),
                        Math::addExact
                );
            }
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(usuarioId);
        pedido.setEstado("RECIBIDO");
        pedido.setEstadoPago(
                metodoPago != null ? "PAGADO" : "PENDIENTE"
        );
        pedido.setMetodoPago(metodoPago);

        BigDecimal total = BigDecimal.ZERO;

        // Procesar productos
        for (Map.Entry<Integer, Integer> entrada :
                productos.entrySet()) {

            Integer productoId = entrada.getKey();
            Integer cantidad = entrada.getValue();

            Producto producto = entityManager.find(
                    Producto.class,
                    productoId,
                    LockModeType.PESSIMISTIC_WRITE
            );

            if (producto == null) {
                throw new IllegalArgumentException(
                        "Producto no encontrado: " + productoId
                );
            }

            if (!Boolean.TRUE.equals(producto.getActivo())) {
                throw new IllegalArgumentException(
                        "El producto está inactivo: " + productoId
                );
            }

            if (producto.getStock() == null ||
                    producto.getStock() < cantidad) {

                throw new IllegalArgumentException(
                        "Stock insuficiente: " + producto.getNombre()
                );
            }

            if (producto.getPrecio() == null ||
                    producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "Precio inválido: " + producto.getNombre()
                );
            }

            BigDecimal precio = producto.getPrecio();
            BigDecimal subtotal = precio.multiply(
                    BigDecimal.valueOf(cantidad)
            );

            producto.setStock(producto.getStock() - cantidad);

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setMedicamento(null);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precio);
            detalle.setSubtotal(subtotal);

            pedido.agregarDetalle(detalle);
            total = total.add(subtotal);
        }

        // Procesar medicamentos
        for (Map.Entry<Integer, Integer> entrada :
                medicamentos.entrySet()) {

            Integer medicamentoId = entrada.getKey();
            Integer cantidad = entrada.getValue();

            Medicamento medicamento = entityManager.find(
                    Medicamento.class,
                    medicamentoId,
                    LockModeType.PESSIMISTIC_WRITE
            );

            if (medicamento == null) {
                throw new IllegalArgumentException(
                        "Medicamento no encontrado: " + medicamentoId
                );
            }

            if (!Boolean.TRUE.equals(medicamento.getActivo())) {
                throw new IllegalArgumentException(
                        "El medicamento está inactivo: " + medicamentoId
                );
            }

            if (medicamento.getStock() == null ||
                    medicamento.getStock() < cantidad) {

                throw new IllegalArgumentException(
                        "Stock insuficiente: " + medicamento.getNombre()
                );
            }

            if (medicamento.getPrecio() == null ||
                    medicamento.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "Precio inválido: " + medicamento.getNombre()
                );
            }

            BigDecimal precio = medicamento.getPrecio();
            BigDecimal subtotal = precio.multiply(
                    BigDecimal.valueOf(cantidad)
            );

            medicamento.setStock(
                    medicamento.getStock() - cantidad
            );

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(null);
            detalle.setMedicamento(medicamento);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precio);
            detalle.setSubtotal(subtotal);

            pedido.agregarDetalle(detalle);
            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.saveAndFlush(pedido);

        return "Pedido registrado correctamente. ID: "
                + guardado.getId()
                + " | Total: S/ " + total;
    }

    @Transactional(readOnly = true)
    public List<PedidoResumen> listarPorUsuario(
            Integer usuarioId) {

        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("Usuario inválido.");
        }

        List<PedidoResumen> resultado = new ArrayList<>();

        for (Pedido pedido :
                pedidoRepository.findByUsuarioIdOrderByFechaDesc(
                        usuarioId)) {

            resultado.add(crearResumen(pedido));
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public PedidoDetalleResumen obtenerDetallePedido(
            Integer pedidoId,
            Integer usuarioId) {

        if (pedidoId == null || pedidoId <= 0 ||
                usuarioId == null || usuarioId <= 0) {

            throw new IllegalArgumentException(
                    "Identificadores inválidos."
            );
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElse(null);

        if (pedido == null ||
                !usuarioId.equals(pedido.getUsuarioId())) {
            return null;
        }

        return crearDetalleResumen(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResumen> listarTodosPedidos() {

        List<PedidoResumen> resultado = new ArrayList<>();

        for (Pedido pedido :
                pedidoRepository.findAllByOrderByFechaDesc()) {

            resultado.add(crearResumen(pedido));
        }

        return resultado;
    }

    @Transactional(readOnly = true)
    public PedidoDetalleResumen obtenerDetallePedidoAdmin(
            Integer pedidoId) {

        if (pedidoId == null || pedidoId <= 0) {
            throw new IllegalArgumentException(
                    "Identificador de pedido inválido."
            );
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElse(null);

        if (pedido == null) {
            return null;
        }

        return crearDetalleResumen(pedido);
    }

    private PedidoResumen crearResumen(Pedido pedido) {

        PedidoResumen resumen = new PedidoResumen();

        resumen.setId(pedido.getId());
        resumen.setUsuarioId(pedido.getUsuarioId());
        resumen.setFecha(
                pedido.getFecha() == null
                        ? ""
                        : pedido.getFecha().toString()
        );
        resumen.setTotal(pedido.getTotal());
        resumen.setEstado(pedido.getEstado());
        resumen.setEstadoPago(pedido.getEstadoPago());

        return resumen;
    }

    private PedidoDetalleResumen crearDetalleResumen(
            Pedido pedido) {

        PedidoDetalleResumen resultado =
                new PedidoDetalleResumen();

        resultado.setId(pedido.getId());
        resultado.setUsuarioId(pedido.getUsuarioId());
        resultado.setFecha(
                pedido.getFecha() == null
                        ? ""
                        : pedido.getFecha().toString()
        );
        resultado.setTotal(pedido.getTotal());
        resultado.setEstado(pedido.getEstado());
        resultado.setEstadoPago(pedido.getEstadoPago());
        resultado.setMetodoPago(pedido.getMetodoPago());

        List<DetallePedidoResumen> detalles = new ArrayList<>();

        for (DetallePedido detalle :
                detalleRepository.findByPedido_IdOrderByIdAsc(
                        pedido.getId())) {

            DetallePedidoResumen item =
                    new DetallePedidoResumen();

            Producto producto = detalle.getProducto();
            Medicamento medicamento = detalle.getMedicamento();

            if (producto != null && medicamento == null) {

                item.setProductoId(producto.getId());
                item.setMedicamentoId(null);
                item.setTipoArticulo("PRODUCTO");
                item.setNombreProducto(producto.getNombre());

            } else if (medicamento != null && producto == null) {

                item.setProductoId(null);
                item.setMedicamentoId(medicamento.getId());
                item.setTipoArticulo("MEDICAMENTO");
                item.setNombreProducto(medicamento.getNombre());

            } else {
                throw new IllegalStateException(
                        "El detalle " + detalle.getId() +
                                " tiene una referencia de artículo inválida."
                );
            }

            item.setCantidad(detalle.getCantidad());
            item.setPrecioUnitario(
                    detalle.getPrecioUnitario()
            );
            item.setSubtotal(detalle.getSubtotal());

            detalles.add(item);
        }

        resultado.setDetalles(detalles);

        return resultado;
    }

    @Transactional
    public String actualizarEstadoPedido(
            Integer pedidoId,
            String nuevoEstado) {

        if (pedidoId == null || pedidoId <= 0) {
            throw new IllegalArgumentException(
                    "Identificador de pedido inválido."
            );
        }

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException(
                    "Debes indicar un estado."
            );
        }

        String estadoSolicitado =
                nuevoEstado.trim().toUpperCase(Locale.ROOT);

        Pedido pedido = entityManager.find(
                Pedido.class,
                pedidoId,
                LockModeType.PESSIMISTIC_WRITE
        );

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "Pedido no encontrado."
            );
        }

        String estadoActual = pedido.getEstado();

        boolean permitido = switch (estadoActual) {
            case "CONFIRMADO", "RECIBIDO" ->
                    estadoSolicitado.equals("PREPARANDO")
                            || estadoSolicitado.equals("CANCELADO");

            case "PREPARANDO" ->
                    estadoSolicitado.equals("LISTO_RECOJO")
                            || estadoSolicitado.equals("CANCELADO");

            case "LISTO_RECOJO" ->
                    estadoSolicitado.equals("ENTREGADO");

            default -> false;
        };

        if (!permitido) {
            throw new IllegalArgumentException(
                    "No se permite cambiar el pedido de "
                            + estadoActual + " a " + estadoSolicitado
            );
        }

        if (estadoSolicitado.equals("ENTREGADO")
                && !"PAGADO".equals(pedido.getEstadoPago())) {

            throw new IllegalArgumentException(
                    "No se puede entregar un pedido sin pago verificado."
            );
        }

        if (estadoSolicitado.equals("CANCELADO")) {
            throw new IllegalStateException(
                    "La cancelación requiere implementar "
                            + "la devolución segura del stock."
            );
        }

        pedido.setEstado(estadoSolicitado);
        pedidoRepository.saveAndFlush(pedido);

        return "Estado del pedido actualizado correctamente: "
                + estadoSolicitado;
    }
}
