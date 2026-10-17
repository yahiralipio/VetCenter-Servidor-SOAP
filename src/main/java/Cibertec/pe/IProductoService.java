
package Cibertec.pe;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface IProductoService {

    @WebMethod
    List<Producto> listarProductos();

    @WebMethod
    List<Producto> listarProductosActivos();

    @WebMethod
    Producto buscarProducto(
            @WebParam(name = "codigo") Integer codigo
    );

    @WebMethod
    List<Producto> buscarProductosPorNombre(
            @WebParam(name = "nombre") String nombre
    );

    @WebMethod
    List<Producto> listarProductosPorCategoria(
            @WebParam(name = "categoriaId") Integer categoriaId
    );

    @WebMethod
    String crearProducto(
            @WebParam(name = "producto") Producto producto
    );

    @WebMethod
    String actualizarProducto(
            @WebParam(name = "codigo") Integer codigo,
            @WebParam(name = "producto") Producto producto
    );

    @WebMethod
    String desactivarProducto(
            @WebParam(name = "codigo") Integer codigo
    );

    @WebMethod
    List<Categoria> listarCategorias();

    @WebMethod
    String crearCategoria(
            @WebParam(name = "categoria") Categoria categoria
    );

    @WebMethod
    String actualizarCategoria(
            @WebParam(name = "codigo") Integer codigo,
            @WebParam(name = "categoria") Categoria categoria
    );

    @WebMethod
    String eliminarCategoria(
            @WebParam(name = "codigo") Integer codigo
    );
}
