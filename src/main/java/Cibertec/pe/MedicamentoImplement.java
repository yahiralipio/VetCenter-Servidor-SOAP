
package Cibertec.pe;

import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@WebService(endpointInterface = "Cibertec.pe.IMedicamentoService")
public class MedicamentoImplement implements IMedicamentoService {

    @Autowired
    private IMedicamentoRepository medicamentoRepo;

    @Autowired
    private ICategoriaMedicamentoRepository categoriaRepo;

    // ==========================================
    // OPERACIONES DE MEDICAMENTOS
    // ==========================================

    @Override
    public List<Medicamento> listarMedicamentos() {
        return medicamentoRepo.findAll();
    }

    // ==========================================
    // CREAR MEDICAMENTO
    // ==========================================

    @Override
    public String crearMedicamento(Medicamento medicamento) {

        if (!datosValidos(medicamento)) {
            return "Datos del medicamento invalidos";
        }

        CategoriaMedicamento categoria =
                obtenerCategoria(medicamento);

        if (medicamento.getCategoria() != null
                && categoria == null) {
            return "La categoria seleccionada no existe";
        }

        medicamento.setId(null);
        medicamento.setCategoria(categoria);

        if (medicamento.getActivo() == null) {
            medicamento.setActivo(true);
        }

        medicamentoRepo.save(medicamento);

        return "Medicamento registrado correctamente";
    }

    // ==========================================
    // BUSCAR MEDICAMENTO
    // ==========================================

    @Override
    public Medicamento buscarMedicamento(Integer codigo) {

        if (codigo == null) {
            return null;
        }

        return medicamentoRepo.findById(codigo).orElse(null);
    }

    // ==========================================
    // ACTUALIZAR MEDICAMENTO
    // ==========================================

    @Override
    public String actualizarMedicamento(
            Integer codigo,
            Medicamento medicamento) {

        if (codigo == null || !datosValidos(medicamento)) {
            return "Datos del medicamento invalidos";
        }

        Medicamento actual =
                medicamentoRepo.findById(codigo).orElse(null);

        if (actual == null) {
            return "Medicamento no encontrado";
        }

        CategoriaMedicamento categoria =
                obtenerCategoria(medicamento);

        if (medicamento.getCategoria() != null
                && categoria == null) {
            return "La categoria seleccionada no existe";
        }

        actual.setNombre(medicamento.getNombre());
        actual.setDescripcion(medicamento.getDescripcion());
        actual.setPresentacion(medicamento.getPresentacion());
        actual.setPrecio(medicamento.getPrecio());
        actual.setStock(medicamento.getStock());
        actual.setImagenUrl(medicamento.getImagenUrl());

        if (medicamento.getActivo() != null) {
            actual.setActivo(medicamento.getActivo());
        }

        actual.setCategoria(categoria);

        medicamentoRepo.save(actual);

        return "Medicamento actualizado correctamente";
    }

    // ==========================================
    // ELIMINAR MEDICAMENTO
    // ==========================================

    @Override
    public String eliminarMedicamento(Integer codigo) {

        if (codigo == null
                || !medicamentoRepo.existsById(codigo)) {
            return "Medicamento no encontrado";
        }

        medicamentoRepo.deleteById(codigo);

        return "Medicamento eliminado correctamente";
    }


    // ==========================================
    // LISTAR CATEGORÍAS
    // ==========================================

    @Override
    public List<CategoriaMedicamento> listarCategoriasMedicamento() {
        return categoriaRepo.findAll();
    }

    // ==========================================
    // CREAR CATEGORÍA
    // ==========================================

    @Override
    public String crearCategoriaMedicamento(
            CategoriaMedicamento categoria) {

        if (categoria == null
                || categoria.getNombre() == null
                || categoria.getNombre().isBlank()) {
            return "Nombre de categoria invalido";
        }

        String nombre = categoria.getNombre().trim();

        if (categoriaRepo.existsByNombreIgnoreCase(nombre)) {
            return "La categoria ya existe";
        }

        CategoriaMedicamento nueva =
                new CategoriaMedicamento();

        nueva.setNombre(nombre);
        nueva.setDescripcion(categoria.getDescripcion());

        categoriaRepo.save(nueva);

        return "Categoria registrada correctamente";
    }

    // ==========================================
    // ACTUALIZAR CATEGORÍA
    // ==========================================

    @Override
    public String actualizarCategoriaMedicamento(
            Integer codigo,
            CategoriaMedicamento categoria) {

        if (codigo == null
                || categoria == null
                || categoria.getNombre() == null
                || categoria.getNombre().isBlank()) {

            return "Datos de categoria invalidos";
        }

        CategoriaMedicamento actual =
                categoriaRepo.findById(codigo).orElse(null);

        if (actual == null) {
            return "Categoria no encontrada";
        }

        String nombre = categoria.getNombre().trim();

        // Evitar nombres duplicados, excluyendo
        // la categoría que estamos editando.
        boolean duplicado = categoriaRepo.findAll()
                .stream()
                .anyMatch(c ->
                        !codigo.equals(c.getId())
                                && c.getNombre() != null
                                && c.getNombre().equalsIgnoreCase(nombre)
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
    // ELIMINAR CATEGORÍA
    // ==========================================

    @Override
    public String eliminarCategoriaMedicamento(Integer codigo) {

        if (codigo == null) {
            return "Codigo de categoria invalido";
        }

        CategoriaMedicamento categoria =
                categoriaRepo.findById(codigo).orElse(null);

        if (categoria == null) {
            return "Categoria no encontrada";
        }

        // Comprobar que la categoría no tenga
        // medicamentos asociados.
        boolean enUso = medicamentoRepo.findAll()
                .stream()
                .anyMatch(m ->
                        m.getCategoria() != null
                                && codigo.equals(m.getCategoria().getId())
                );

        if (enUso) {
            return "No se puede eliminar la categoria porque tiene medicamentos asociados";
        }

        categoriaRepo.delete(categoria);

        return "Categoria eliminada correctamente";
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    private CategoriaMedicamento obtenerCategoria(
            Medicamento medicamento) {

        if (medicamento.getCategoria() == null
                || medicamento.getCategoria().getId() == null) {

            return null;
        }

        Integer categoriaId =
                medicamento.getCategoria().getId();

        return categoriaRepo.findById(categoriaId).orElse(null);
    }

    // ==========================================
    // VALIDAR DATOS DEL MEDICAMENTO
    // ==========================================

    private boolean datosValidos(Medicamento medicamento) {

        return medicamento != null
                && medicamento.getNombre() != null
                && !medicamento.getNombre().isBlank()
                && medicamento.getPresentacion() != null
                && !medicamento.getPresentacion().isBlank()
                && medicamento.getPrecio() != null
                && medicamento.getPrecio().signum() >= 0
                && medicamento.getStock() != null
                && medicamento.getStock() >= 0;
    }
}
