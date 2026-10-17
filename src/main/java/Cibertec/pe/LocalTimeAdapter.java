
package Cibertec.pe;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    @Override
    public LocalTime unmarshal(String valor) {
        return valor == null || valor.isBlank()
                ? null
                : LocalTime.parse(valor);
    }

    @Override
    public String marshal(LocalTime valor) {
        return valor == null ? null : valor.toString();
    }
}
