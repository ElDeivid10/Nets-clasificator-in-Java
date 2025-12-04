import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class ClasificadorIP {
    private Map<String, GrupoRed> gruposRed;

    public ClasificadorIP() {

        gruposRed = new HashMap<>();
    }

    public static void main(String[] args) {

        ClasificadorIP clasificador = new ClasificadorIP();
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\nIngrese una dirección IP y su máscara de subred (o 'salir' para terminar):");
            System.out.print("Dirección IP (ej. 192.168.1.1): ");
            String entrada = scanner.nextLine();

            if (entrada.equalsIgnoreCase("salir")) {
                break;
            }

            System.out.print("Máscara de subred (ej. 255.255.255.0): ");
            String mascara = scanner.nextLine();

            try {

                clasificador.agregarIP(entrada, mascara);
                clasificador.mostrarGrupos();
            } catch (IllegalArgumentException e) {

                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    public void agregarIP(String ip, String mascara) {

        String direccionRed = calcularDireccionRed(ip, mascara);

        gruposRed.putIfAbsent(direccionRed, new GrupoRed(direccionRed));
        gruposRed.get(direccionRed).agregarIP(ip);
    }

    public void mostrarGrupos() {
        System.out.println("\n--- Grupos de Red ---");

        if (gruposRed.isEmpty()) {
            System.out.println("No hay grupos de red creados.");
        } else {

            for (GrupoRed grupo : gruposRed.values()) {
                grupo.mostrar();
            }
        }
    }

    private String calcularDireccionRed(String ip, String mascara) {

        String[] octetosIP = ip.split("\\.");
        String[] octetosMascara = mascara.split("\\.");

        StringBuilder direccionRed = new StringBuilder();

        for (int i = 0; i < 4; i++) {

            int octetoIP = Integer.parseInt(octetosIP[i]);
            int octetoMascara = Integer.parseInt(octetosMascara[i]);
            int octetoRed = octetoIP & octetoMascara;
            direccionRed.append(octetoRed);
            if (i < 3) direccionRed.append(".");
        }

        return direccionRed.toString();
    }

    private static class GrupoRed {

        private String direccionRed;
        private List<String> direccionesIP;

        public GrupoRed(String direccionRed) {

            this.direccionRed = direccionRed;
            this.direccionesIP = new ArrayList<>();
        }

        public void agregarIP(String ip) {

            if (!direccionesIP.contains(ip)) {
                direccionesIP.add(ip);
            }
        }

        public void mostrar() {

            System.out.println("\nRed: " + direccionRed);
            System.out.println("Direcciones IP en esta red:");

            for (String ip : direccionesIP) {
                System.out.println("- " + ip);
            }
        }
    }
}