
package Cibertec.pe;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface IMascotaService {

    @WebMethod
    List<Mascota> listarMascotas();

    @WebMethod
    String crearMascota(
            @WebParam(name = "mascota") Mascota mascota,
            @WebParam(name = "clienteId") Integer clienteId);

    @WebMethod
    Mascota buscarMascota(
            @WebParam(name = "codigo") Integer codigo);

    @WebMethod
    String actualizarMascota(
            @WebParam(name = "codigo") Integer codigo,
            @WebParam(name = "mascota") Mascota mascota,
            @WebParam(name = "clienteId") Integer clienteId);

    @WebMethod
    String eliminarMascota(
            @WebParam(name = "codigo") Integer codigo);
}
