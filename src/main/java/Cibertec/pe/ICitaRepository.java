
package Cibertec.pe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ICitaRepository
        extends JpaRepository<Cita, Integer> {

    // VALIDAR SI UN HORARIO YA ESTÁ RESERVADO
    boolean existsByFechaAndHoraAndEstadoIn(
            LocalDate fecha,
            LocalTime hora,
            Collection<String> estados
    );

    // VALIDAR HORARIO AL ACTUALIZAR UNA CITA
    boolean existsByFechaAndHoraAndEstadoInAndIdNot(
            LocalDate fecha,
            LocalTime hora,
            Collection<String> estados,
            Integer id
    );

    // NUEVO: CONSULTAR CITAS ACTIVAS POR FECHA
    List<Cita> findByFechaAndEstadoIn(
            LocalDate fecha,
            Collection<String> estados
    );
}
