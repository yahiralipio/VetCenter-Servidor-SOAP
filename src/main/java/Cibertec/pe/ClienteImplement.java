
package Cibertec.pe;

import jakarta.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@WebService(endpointInterface = "Cibertec.pe.IClienteService")
public class ClienteImplement implements IClienteService {

    @Autowired
    private IClienteRepository clienteRepo;

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepo.findAll();
    }

    @Override
    public String crearCliente(Cliente cliente) {
        clienteRepo.save(cliente);
        return "Cliente registrado correctamente";
    }

    // Registrar cliente y devolver su ID generado
    @Override
    public Integer crearClienteConId(Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "Los datos del cliente son obligatorios"
            );
        }

        // Evitar actualizar accidentalmente otro cliente
        cliente.setId(null);

        Cliente nuevoCliente = clienteRepo.saveAndFlush(cliente);

        return nuevoCliente.getId();
    }

    @Override
    public Cliente buscarCliente(Integer codigo) {
        return clienteRepo.findById(codigo).orElse(null);
    }

    @Override
    public String actualizarCliente(
            Integer codigo,
            Cliente cliente) {

        Cliente cli = clienteRepo.findById(codigo).orElse(null);

        if (cli != null) {

            cli.setNombres(cliente.getNombres());
            cli.setApellidos(cliente.getApellidos());
            cli.setDni(cliente.getDni());
            cli.setTelefono(cliente.getTelefono());
            cli.setCorreo(cliente.getCorreo());

            clienteRepo.save(cli);

            return "Cliente actualizado correctamente";
        }

        return "Cliente no encontrado";
    }

    @Override
    public String eliminarCliente(Integer codigo) {

        if (clienteRepo.existsById(codigo)) {
            clienteRepo.deleteById(codigo);
            return "Cliente eliminado correctamente";
        }

        return "Cliente no encontrado";
    }
}


