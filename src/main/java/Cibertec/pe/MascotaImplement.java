
package Cibertec.pe;

import jakarta.jws.WebService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Component
@WebService(endpointInterface = "Cibertec.pe.IMascotaService")
public class MascotaImplement implements IMascotaService {

    @Autowired
    private IMascotaRepository mascotaRepo;

    @Autowired
    private IClienteRepository clienteRepo;

    @Override
    public List<Mascota> listarMascotas() {
        return mascotaRepo.findAll();
    }

    @Override
    public String crearMascota(Mascota mascota, Integer clienteId) {

        Cliente cliente = clienteRepo.findById(clienteId).orElse(null);

        if (cliente == null) {
            return "Cliente no encontrado";
        }

        mascota.setId(null);
        mascota.setCliente(cliente);
        mascotaRepo.save(mascota);

        return "Mascota registrada correctamente";
    }

    @Override
    public Mascota buscarMascota(Integer codigo) {
        return mascotaRepo.findById(codigo).orElse(null);
    }

    @Override
    public String actualizarMascota(
            Integer codigo, Mascota mascota, Integer clienteId) {

        Mascota actual = mascotaRepo.findById(codigo).orElse(null);

        if (actual == null) {
            return "Mascota no encontrada";
        }

        Cliente cliente = clienteRepo.findById(clienteId).orElse(null);

        if (cliente == null) {
            return "Cliente no encontrado";
        }

        actual.setNombre(mascota.getNombre());
        actual.setEspecie(mascota.getEspecie());
        actual.setRaza(mascota.getRaza());
        actual.setSexo(mascota.getSexo());
        actual.setFechaNacimiento(mascota.getFechaNacimiento());
        actual.setCliente(cliente);

        mascotaRepo.save(actual);

        return "Mascota actualizada correctamente";
    }

    @Override
    public String eliminarMascota(Integer codigo) {

        if (mascotaRepo.existsById(codigo)) {
            mascotaRepo.deleteById(codigo);
            return "Mascota eliminada correctamente";
        }

        return "Mascota no encontrada";
    }
}
