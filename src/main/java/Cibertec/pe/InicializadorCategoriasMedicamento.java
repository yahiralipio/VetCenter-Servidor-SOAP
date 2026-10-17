
package Cibertec.pe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InicializadorCategoriasMedicamento
        implements CommandLineRunner {

    private final ICategoriaMedicamentoRepository repository;

    public InicializadorCategoriasMedicamento(
            ICategoriaMedicamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        registrarSiNoExiste(
                "Antiparasitarios",
                "Medicamentos contra parásitos"
        );

        registrarSiNoExiste(
                "Antibióticos",
                "Medicamentos antibacterianos"
        );

        registrarSiNoExiste(
                "Antiinflamatorios",
                "Tratamientos antiinflamatorios"
        );

        registrarSiNoExiste(
                "Suplementos",
                "Vitaminas y suplementos veterinarios"
        );

        registrarSiNoExiste(
                "Cuidado dermatológico",
                "Tratamientos para piel y pelaje"
        );

        registrarSiNoExiste(
                "Otros",
                "Otros medicamentos veterinarios"
        );
    }

    private void registrarSiNoExiste(
            String nombre,
            String descripcion) {

        if (repository.existsByNombreIgnoreCase(nombre)) {
            return;
        }

        CategoriaMedicamento categoria =
                new CategoriaMedicamento();

        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);

        repository.save(categoria);
    }
}
