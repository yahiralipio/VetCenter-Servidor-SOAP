package Cibertec.pe;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface IClienteService {

    @WebMethod
    public List<Cliente> listarClientes();

    @WebMethod
    public String crearCliente(
            @WebParam(name = "cliente") Cliente cliente);

    @WebMethod
    public Integer crearClienteConId(
            @WebParam(name = "cliente") Cliente cliente);

    @WebMethod
    public Cliente buscarCliente(
            @WebParam(name = "codigo") Integer codigo);

    @WebMethod
    public String actualizarCliente(
            @WebParam(name = "codigo") Integer codigo,
            @WebParam(name = "cliente") Cliente cliente);

    @WebMethod
    public String eliminarCliente(
            @WebParam(name = "codigo") Integer codigo);
}
