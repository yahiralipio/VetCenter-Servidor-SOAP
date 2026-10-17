
package Cibertec.pe;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface IMedicamentoService {

    // ==========================================
    // OPERACIONES DE MEDICAMENTOS
    // ==========================================

    @WebMethod
    List<Medicamento> listarMedicamentos();

    @WebMethod
    String crearMedicamento(
            @WebParam(name = "medicamento")
            Medicamento medicamento
    );

    @WebMethod
    Medicamento buscarMedicamento(
            @WebParam(name = "codigo")
            Integer codigo
    );

    @WebMethod
    String actualizarMedicamento(
            @WebParam(name = "codigo")
            Integer codigo,
            @WebParam(name = "medicamento")
            Medicamento medicamento
    );

    @WebMethod
    String eliminarMedicamento(
            @WebParam(name = "codigo")
            Integer codigo
    );

    // ==========================================
    // OPERACIONES DE CATEGORÍAS
    // ==========================================

    @WebMethod
    List<CategoriaMedicamento> listarCategoriasMedicamento();

    @WebMethod
    String crearCategoriaMedicamento(
            @WebParam(name = "categoria")
            CategoriaMedicamento categoria
    );

    // ==========================================
    // ACTUALIZAR CATEGORÍA DE MEDICAMENTOS
    // ==========================================

    @WebMethod
    String actualizarCategoriaMedicamento(
            @WebParam(name = "codigo")
            Integer codigo,
            @WebParam(name = "categoria")
            CategoriaMedicamento categoria
    );

    // ==========================================
    // ELIMINAR CATEGORÍA DE MEDICAMENTOS
    // ==========================================

    @WebMethod
    String eliminarCategoriaMedicamento(
            @WebParam(name = "codigo")
            Integer codigo
    );
}
