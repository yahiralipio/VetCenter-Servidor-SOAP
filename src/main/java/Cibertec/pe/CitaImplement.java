
package Cibertec.pe;

import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@WebService(endpointInterface = "Cibertec.pe.ICitaService")
public class CitaImplement implements ICitaService {

    @Autowired
    private ICitaRepository citaRepo;

    @Autowired
    private IMascotaRepository mascotaRepo;

    private static final List<String> ESTADOS_ACTIVOS =
            List.of("PENDIENTE", "CONFIRMADA");

    private static final Object BLOQUEO_CITAS = new Object();

    @Override
    public List<Cita> listarCitas() {
        return citaRepo.findAll();
    }

    @Override
    public List<String> obtenerHorariosOcupados(String fecha) {

        // Si no se proporciona una fecha, devolver una lista vacía
        if (fecha == null || fecha.isBlank()) {
            return List.of();
        }

        try {
            LocalDate fechaConsulta = LocalDate.parse(fecha);

            // Consultar las citas registradas para esa fecha
            // Solo PENDIENTE y CONFIRMADA bloquean horarios
            return citaRepo.findByFechaAndEstadoIn(
                            fechaConsulta,
                            ESTADOS_ACTIVOS
                    )
                    .stream()
                    .filter(cita ->
                            fechaConsulta.equals(cita.getFecha())
                    )
                    .filter(cita ->
                            ESTADOS_ACTIVOS.contains(cita.getEstado())
                    )
                    .map(cita -> cita.getHora().toString())
                    .distinct()
                    .sorted()
                    .toList();

        } catch (DateTimeParseException e) {
            return List.of();
        }
    }

    @Override
    public String crearCita(Integer mascotaId, String fecha,
                            String hora, String motivo) {

        if (mascotaId == null || motivo == null ||
                motivo.isBlank()) {
            return "Datos de la cita invalidos";
        }

        Mascota mascota =
                mascotaRepo.findById(mascotaId).orElse(null);

        if (mascota == null) {
            return "Mascota no encontrada";
        }

        try {
            LocalDate fechaCita = LocalDate.parse(fecha);
            LocalTime horaCita = LocalTime.parse(hora);

            synchronized (BLOQUEO_CITAS) {

                LocalDateTime fechaHoraCita = LocalDateTime.of(
                        fechaCita,
                        horaCita
                );

                if (!fechaHoraCita.isAfter(LocalDateTime.now())) {
                    return "La fecha y hora de la cita deben ser futuras";
                }

                if (citaRepo.existsByFechaAndHoraAndEstadoIn(
                        fechaCita, horaCita, ESTADOS_ACTIVOS)) {
                    return "El horario ya esta reservado";
                }

                Cita cita = new Cita();
                cita.setMascota(mascota);
                cita.setFecha(fechaCita);
                cita.setHora(horaCita);
                cita.setMotivo(motivo);
                cita.setEstado("PENDIENTE");

                citaRepo.saveAndFlush(cita);

                return "Cita registrada correctamente";
            }

        } catch (DateTimeParseException | NullPointerException e) {
            return "Formato de fecha u hora invalido";
        }
    }

    @Override
    public Cita buscarCita(Integer codigo) {

        if (codigo == null) {
            return null;
        }

        return citaRepo.findById(codigo).orElse(null);
    }

    @Override
    public String actualizarCita(Integer codigo,
                                 Integer mascotaId,
                                 String fecha,
                                 String hora,
                                 String motivo) {

        if (codigo == null || mascotaId == null ||
                motivo == null || motivo.isBlank()) {
            return "Datos de la cita invalidos";
        }

        Mascota mascota =
                mascotaRepo.findById(mascotaId).orElse(null);

        if (mascota == null) {
            return "Mascota no encontrada";
        }

        try {
            LocalDate fechaCita = LocalDate.parse(fecha);
            LocalTime horaCita = LocalTime.parse(hora);

            synchronized (BLOQUEO_CITAS) {

                Cita cita = citaRepo.findById(codigo).orElse(null);

                if (cita == null) {
                    return "Cita no encontrada";
                }

                if ("CANCELADA".equals(cita.getEstado()) ||
                        "ATENDIDA".equals(cita.getEstado())) {
                    return "No se puede modificar una cita finalizada";
                }

                LocalDateTime fechaHoraCita = LocalDateTime.of(
                        fechaCita,
                        horaCita
                );

                if (!fechaHoraCita.isAfter(LocalDateTime.now())) {
                    return "La fecha u hora ya paso";
                }

                if (citaRepo.existsByFechaAndHoraAndEstadoInAndIdNot(
                        fechaCita, horaCita, ESTADOS_ACTIVOS, codigo)) {
                    return "El horario ya esta reservado";
                }

                cita.setMascota(mascota);
                cita.setFecha(fechaCita);
                cita.setHora(horaCita);
                cita.setMotivo(motivo);

                citaRepo.saveAndFlush(cita);

                return "Cita actualizada correctamente";
            }

        } catch (DateTimeParseException | NullPointerException e) {
            return "Formato de fecha u hora invalido";
        }
    }


    @Override
    public String confirmarCita(Integer codigo) {

        if (codigo == null) {
            return "Cita no encontrada";
        }

        synchronized (BLOQUEO_CITAS) {

            Cita cita = citaRepo.findById(codigo).orElse(null);

            if (cita == null) {
                return "Cita no encontrada";
            }

            if ("CONFIRMADA".equals(cita.getEstado())) {
                return "La cita ya esta confirmada";
            }

            if (!"PENDIENTE".equals(cita.getEstado())) {
                return "Solo se pueden confirmar citas pendientes";
            }

            cita.setEstado("CONFIRMADA");
            citaRepo.saveAndFlush(cita);

            return "Cita confirmada correctamente";
        }
    }

    @Override
    public String marcarCitaAtendida(Integer codigo) {

        if (codigo == null) {
            return "Cita no encontrada";
        }

        synchronized (BLOQUEO_CITAS) {

            Cita cita = citaRepo.findById(codigo).orElse(null);

            if (cita == null) {
                return "Cita no encontrada";
            }

            if ("ATENDIDA".equals(cita.getEstado())) {
                return "La cita ya esta atendida";
            }

            if (!"CONFIRMADA".equals(cita.getEstado())) {
                return "Solo se pueden marcar como atendidas las citas confirmadas";
            }

            cita.setEstado("ATENDIDA");
            citaRepo.saveAndFlush(cita);

            return "Cita marcada como atendida correctamente";
        }
    }


    @Override
    public String cancelarCita(Integer codigo) {

        if (codigo == null) {
            return "Cita no encontrada";
        }

        synchronized (BLOQUEO_CITAS) {

            Cita cita = citaRepo.findById(codigo).orElse(null);

            if (cita == null) {
                return "Cita no encontrada";
            }

            if ("ATENDIDA".equals(cita.getEstado())) {
                return "No se puede cancelar una cita atendida";
            }

            if ("CANCELADA".equals(cita.getEstado())) {
                return "La cita ya esta cancelada";
            }

            cita.setEstado("CANCELADA");
            citaRepo.saveAndFlush(cita);

            return "Cita cancelada correctamente";
        }
    }

    @Override
    public String eliminarCita(Integer codigo) {

        if (codigo == null) {
            return "Cita no encontrada";
        }

        synchronized (BLOQUEO_CITAS) {

            if (!citaRepo.existsById(codigo)) {
                return "Cita no encontrada";
            }

            citaRepo.deleteById(codigo);
            citaRepo.flush();

            return "Cita eliminada correctamente";
        }
    }
}
