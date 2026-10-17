package Cibertec.pe;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String valor) {
        return valor == null || valor.isBlank()
                ? null
                : LocalDate.parse(valor);
    }

    @Override
    public String marshal(LocalDate valor) {
        return valor == null ? null : valor.toString();
    }
}
