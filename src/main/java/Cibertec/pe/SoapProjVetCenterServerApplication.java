
package Cibertec.pe;

import jakarta.xml.ws.Endpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SoapProjVetCenterServerApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =
				SpringApplication.run(
						SoapProjVetCenterServerApplication.class, args
				);

		ClienteImplement clienteService =
				context.getBean(ClienteImplement.class);

		MascotaImplement mascotaService =
				context.getBean(MascotaImplement.class);

		MedicamentoImplement medicamentoService =
				context.getBean(MedicamentoImplement.class);

		CitaImplement citaService =
				context.getBean(CitaImplement.class);

		// Instancia sin proxy de Spring para publicar con JAX-WS
		ProductoImplement productoService =
				new ProductoImplement(
						context.getBean(IProductoRepository.class),
						context.getBean(ICategoriaRepository.class)
				);

		PedidoTransaccionService pedidoTransacciones =
				context.getBean(PedidoTransaccionService.class);

		Endpoint.publish(
				"http://localhost:8085/ws/cliente",
				clienteService
		);

		Endpoint.publish(
				"http://localhost:8085/ws/mascota",
				mascotaService
		);

		Endpoint.publish(
				"http://localhost:8085/ws/medicamento",
				medicamentoService
		);

		Endpoint.publish(
				"http://localhost:8085/ws/cita",
				citaService
		);

		Endpoint.publish(
				"http://localhost:8085/ws/producto",
				productoService
		);

		Endpoint.publish(
				"http://localhost:8085/ws/pedido",
				new PedidoImplement(pedidoTransacciones)
		);

		System.out.println(
				"Servicios SOAP VetCenter iniciados correctamente"
		);
	}
}
