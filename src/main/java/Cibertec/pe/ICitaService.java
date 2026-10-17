
package Cibertec.pe;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface ICitaService {

    @WebMethod
    List<Cita> listarCitas();

    // NUEVO: CONSULTAR HORARIOS OCUPADOS POR FECHA
    @WebMethod
    List<String> obtenerHorariosOcupados(
            @WebParam(name = "fecha") String fecha
    );

    @WebMethod
    String crearCita(
            @WebParam(name = "mascotaId") Integer mascotaId,
            @WebParam(name = "fecha") String fecha,
            @WebParam(name = "hora") String hora,
            @WebParam(name = "motivo") String motivo
    );

    @WebMethod
    Cita buscarCita(
            @WebParam(name = "codigo") Integer codigo
    );

    @WebMethod
    String actualizarCita(
            @WebParam(name = "codigo") Integer codigo,
            @WebParam(name = "mascotaId") Integer mascotaId,
            @WebParam(name = "fecha") String fecha,
            @WebParam(name = "hora") String hora,
            @WebParam(name = "motivo") String motivo
    );

    @WebMethod
    String confirmarCita(
            @WebParam(name = "codigo") Integer codigo
    );

    @WebMethod
    String marcarCitaAtendida(
            @WebParam(name = "codigo") Integer codigo
    );

    @WebMethod
    String cancelarCita(
            @WebParam(name = "codigo") Integer codigo
    );

    @WebMethod
    String eliminarCita(
            @WebParam(name = "codigo") Integer codigo
    );
}
