package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.example.models.Pedido;

public class ExportService {
    public static void exportarExcel(List<Pedido> pedidos, String path) throws IOException {
        File file = new File(path);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,mesa,cliente,estado,total,fecha\n");
            for (Pedido p : pedidos) {
                writer.write(p.getId() + "," + p.getMesaId() + "," + p.getCliente() + "," + p.getEstado() + "," + p.getTotal() + "," + p.getFechaCreacion() + "\n");
            }
        }
    }

    public static void exportarPdf(List<Pedido> pedidos, String path) throws IOException {
        Path output = Path.of(path);
        Files.writeString(output, "Exportación PDF simulada\n\n" + pedidos.size() + " pedidos exportados");
    }
}
