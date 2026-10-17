
package Cibertec.pe;

import jakarta.jws.WebService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@WebService(endpointInterface = "Cibertec.pe.IProductoService")
@Transactional
public class ProductoImplement implements IProductoService {

    private final IProductoRepository productoRepo;
    private final ICategoriaRepository categoriaRepo;

    public ProductoImplement(
            IProductoRepository productoRepo,
            ICategoriaRepository categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    @Override
    public List<Producto> listarProductosActivos() {
        return productoRepo.findByActivoTrueOrderByNombreAsc();
    }

    @Override
    public Producto buscarProducto(Integer codigo) {
        if (codigo == null) return null;
        return productoRepo.findById(codigo).orElse(null);
    }

    @Override
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepo
                .findByNombreContainingIgnoreCaseAndActivoTrue(
                        nombre == null ? "" : nombre.trim()
                );
    }

    @Override
    public List<Producto> listarProductosPorCategoria(
            Integer categoriaId) {

        if (categoriaId == null) return List.of();

        return productoRepo.findByCategoriaIdAndActivoTrue(
                categoriaId
        );
    }

    @Override
    public String crearProducto(Producto producto) {

        if (!datosValidos(producto)) {
            return "Datos del producto invalidos";
        }

        Categoria categoria = categoriaRepo.findById(
                producto.getCategoria().getId()
        ).orElse(null);

        if (categoria == null) {
            return "Categoria no encontrada";
        }

        producto.setId(null);
        producto.setCategoria(categoria);
        producto.setActivo(true);

        productoRepo.save(producto);

        return "Producto registrado correctamente";
    }

    @Override
    public String actualizarProducto(
            Integer codigo, Producto producto) {

        if (codigo == null || !datosValidos(producto)) {
            return "Datos del producto invalidos";
        }

        Producto actual = productoRepo.findById(codigo)
                .orElse(null);

        if (actual == null) {
            return "Producto no encontrado";
        }

        Categoria categoria = categoriaRepo.findById(
                producto.getCategoria().getId()
        ).orElse(null);

        if (categoria == null) {
            return "Categoria no encontrada";
        }

        actual.setNombre(producto.getNombre().trim());
        actual.setDescripcion(producto.getDescripcion());
        actual.setCategoria(categoria);
        actual.setPrecio(producto.getPrecio());
        actual.setStock(producto.getStock());
        actual.setImagenUrl(producto.getImagenUrl());
        actual.setActivo(producto.getActivo());

        productoRepo.save(actual);

        return "Producto actualizado correctamente";
    }

    @Override
    public String desactivarProducto(Integer codigo) {

        if (codigo == null) {
            return "Codigo invalido";
        }

        Producto producto = productoRepo.findById(codigo)
                .orElse(null);

        if (producto == null) {
            return "Producto no encontrado";
        }

        producto.setActivo(false);
        productoRepo.save(producto);

        return "Producto desactivado correctamente";
    }

    // ==========================================
    // CATEGORÍAS DE PRODUCTOS
    // ==========================================

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepo.findAllByOrderByNombreAsc();
    }

    @Override
    public String crearCategoria(Categoria categoria) {

        if (categoria == null ||
                categoria.getNombre() == null ||
                categoria.getNombre().isBlank()) {
            return "Nombre de categoria obligatorio";
        }

        String nombre = categoria.getNombre().trim();

        if (categoriaRepo.existsByNombreIgnoreCase(nombre)) {
            return "La categoria ya existe";
        }

        categoria.setId(null);
        categoria.setNombre(nombre);

        categoriaRepo.save(categoria);

        return "Categoria registrada correctamente";
    }

    // ==========================================
    // ACTUALIZAR CATEGORÍA DE PRODUCTOS
    // ==========================================

    @Override
    public String actualizarCategoria(
            Integer codigo, Categoria categoria) {

        if (codigo == null ||
                categoria == null ||
                categoria.getNombre() == null ||
                categoria.getNombre().isBlank()) {
            return "Datos de categoria invalidos";
        }

        Categoria actual = categoriaRepo.findById(codigo)
                .orElse(null);

        if (actual == null) {
            return "Categoria no encontrada";
        }

        String nombre = categoria.getNombre().trim();

        boolean duplicado = categoriaRepo.findAll()
                .stream()
                .anyMatch(c ->
                        !c.getId().equals(codigo) &&
                                c.getNombre() != null &&
                                c.getNombre().equalsIgnoreCase(nombre)
                );

        if (duplicado) {
            return "La categoria ya existe";
        }

        actual.setNombre(nombre);
        actual.setDescripcion(categoria.getDescripcion());

        categoriaRepo.save(actual);

        return "Categoria actualizada correctamente";
    }

    // ==========================================
    // ELIMINAR CATEGORÍA DE PRODUCTOS
    // ==========================================

    @Override
    public String eliminarCategoria(Integer codigo) {

        if (codigo == null) {
            return "Codigo de categoria invalido";
        }

        Categoria categoria = categoriaRepo.findById(codigo)
                .orElse(null);

        if (categoria == null) {
            return "Categoria no encontrada";
        }

        boolean enUso = productoRepo.findAll()
                .stream()
                .anyMatch(p ->
                        p.getCategoria() != null &&
                                codigo.equals(p.getCategoria().getId())
                );

        if (enUso) {
            return "No se puede eliminar la categoria porque tiene productos asociados";
        }

        categoriaRepo.delete(categoria);

        return "Categoria eliminada correctamente";
    }

    // ==========================================
    // VALIDACIONES DE PRODUCTOS
    // ==========================================

    private boolean datosValidos(Producto producto) {

        return producto != null
                && producto.getNombre() != null
                && !producto.getNombre().isBlank()
                && producto.getCategoria() != null
                && producto.getCategoria().getId() != null
                && producto.getPrecio() != null
                && producto.getPrecio().signum() >= 0
                && producto.getStock() != null
                && producto.getStock() >= 0;
    }
}
