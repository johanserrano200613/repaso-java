import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Agenda agenda = new Agenda();
        agenda.cargarDesdeArchivo();
        int opcion;
        do {
            opcion = Entrada.entero("1 Agregar  2 Buscar  3 Listar  4 Eliminar  5 Grupo  6 Guardar  7 Cargar  0 Salir: ", 0, 7);
            switch (opcion) {
                case 1:
                    String nombre = Entrada.texto("Nombre: ");
                    String telefono = Entrada.texto("Telefono (9 digitos): ");
                    String email = Entrada.texto("Email: ");
                    String grupo = Entrada.texto("Grupo: ");
                    Contacto contacto = new Contacto(nombre, telefono, email, grupo);
                    if (agenda.agregarContacto(contacto)) System.out.println("Contacto agregado.");
                    else System.out.println("Datos invalidos o contacto repetido.");
                    break;
                case 2:
                    ArrayList<Contacto> resultados = agenda.buscarContacto(Entrada.texto("Buscar nombre: "));
                    if (resultados.size() == 0) System.out.println("Sin resultados.");
                    for (Contacto resultado : resultados) resultado.mostrarInfo();
                    break;
                case 3:
                    agenda.mostrarTodos();
                    break;
                case 4:
                    if (agenda.eliminarContacto(Entrada.texto("Nombre exacto: "))) System.out.println("Eliminado.");
                    else System.out.println("No encontrado.");
                    break;
                case 5:
                    agenda.mostrarContactosPorGrupo(Entrada.texto("Grupo: "));
                    break;
                case 6:
                    agenda.guardarEnArchivo();
                    break;
                case 7:
                    agenda.cargarDesdeArchivo();
                    break;
            }
        } while (opcion != 0);
        agenda.guardarEnArchivo();
    }
}
class Contacto {
    private String nombre;
    private String telefono;
    private String email;
    private String grupo;
    public Contacto(String nombre, String telefono, String email, String grupo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.grupo = grupo;
    }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getGrupo() { return grupo; }
    public void mostrarInfo() { System.out.println(nombre + " | " + telefono + " | " + email + " | " + grupo); }
}
class Agenda {
    // Punto 16: lista sin limite fijo.
    private ArrayList<Contacto> contactos = new ArrayList<Contacto>();
    // Si la carga falla, no se permite sobrescribir el archivo original.
    private boolean permitirGuardado = true;
    // Punto 17: comprobacion con un for, sin expresiones regulares.
    public boolean agregarContacto(Contacto contacto) {
        if (contacto.getNombre().trim().isEmpty() || contacto.getGrupo().trim().isEmpty()) return false;
        String telefono = contacto.getTelefono();
        if (telefono.length() != 9) return false;
        for (int i = 0; i < telefono.length(); i++) {
            if (telefono.charAt(i) < '0' || telefono.charAt(i) > '9') return false;
        }
        String email = contacto.getEmail();
        int arroba = email.indexOf("@");
        int punto = email.lastIndexOf(".");
        if (arroba <= 0 || arroba != email.lastIndexOf("@")) return false;
        if (punto <= arroba + 1 || punto == email.length() - 1) return false;
        for (int i = 0; i < email.length(); i++) {
            if (Character.isWhitespace(email.charAt(i))) return false;
        }
        for (Contacto actual : contactos) {
            if (actual.getNombre().equalsIgnoreCase(contacto.getNombre()) || actual.getTelefono().equals(telefono)) return false;
        }
        contactos.add(contacto);
        return true;
    }
    // Punto 18: buscar parte de un nombre.
    public ArrayList<Contacto> buscarContacto(String nombre) {
        ArrayList<Contacto> resultados = new ArrayList<Contacto>();
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().toLowerCase().contains(nombre.toLowerCase())) resultados.add(contacto);
        }
        return resultados;
    }
    public void mostrarTodos() {
        if (contactos.size() == 0) System.out.println("Agenda vacia.");
        for (Contacto contacto : contactos) contacto.mostrarInfo();
    }
    public boolean eliminarContacto(String nombre) {
        for (int i = 0; i < contactos.size(); i++) {
            if (contactos.get(i).getNombre().equalsIgnoreCase(nombre)) {
                contactos.remove(i);
                return true;
            }
        }
        return false;
    }
    // Punto 19.
    public void mostrarContactosPorGrupo(String grupo) {
        for (Contacto contacto : contactos) {
            if (contacto.getGrupo().equalsIgnoreCase(grupo)) contacto.mostrarInfo();
        }
    }
    // Punto 20: cuatro lineas por contacto; no se usa serializacion.
    public void guardarEnArchivo() {
        if (!permitirGuardado) {
            System.out.println("No se guardo: la carga fallo. El archivo original se conserva.");
            System.out.println("Corrija agenda.txt y use Cargar antes de guardar cambios.");
            return;
        }
        try {
            FileWriter archivo = new FileWriter("agenda.txt");
            for (Contacto contacto : contactos) {
                archivo.write(contacto.getNombre() + "\n");
                archivo.write(contacto.getTelefono() + "\n");
                archivo.write(contacto.getEmail() + "\n");
                archivo.write(contacto.getGrupo() + "\n");
            }
            archivo.close();
            System.out.println("Agenda guardada.");
        } catch (IOException error) {
            System.out.println("No se pudo guardar.");
        }
    }
    public void cargarDesdeArchivo() {
        File archivo = new File("agenda.txt");
        if (!archivo.exists()) {
            permitirGuardado = true;
            return;
        }
        try {
            Scanner lector = new Scanner(archivo);
            Agenda temporal = new Agenda();
            boolean correcto = true;
            while (lector.hasNextLine() && correcto) {
                String nombre = lector.nextLine();
                if (!lector.hasNextLine()) { correcto = false; break; }
                String telefono = lector.nextLine();
                if (!lector.hasNextLine()) { correcto = false; break; }
                String email = lector.nextLine();
                if (!lector.hasNextLine()) { correcto = false; break; }
                String grupo = lector.nextLine();
                if (nombre.trim().isEmpty() || grupo.trim().isEmpty()) correcto = false;
                else correcto = temporal.agregarContacto(new Contacto(nombre, telefono, email, grupo));
            }
            lector.close();
            if (correcto) {
                contactos = temporal.contactos;
                permitirGuardado = true;
                System.out.println("Agenda cargada.");
            } else {
                permitirGuardado = false;
                System.out.println("Archivo incompleto o invalido. No se cargo.");
                System.out.println("Guardado bloqueado para conservar el archivo original.");
            }
        } catch (IOException error) {
            permitirGuardado = false;
            System.out.println("No se pudo cargar. Guardado bloqueado para conservar el archivo original.");
        }
    }
}

// Ayuda para leer datos sin repetir la misma validacion en cada menu.
class Entrada {
    private static Scanner teclado = new Scanner(System.in).useLocale(Locale.US);
    public static String texto(String mensaje) {
        String valor;
        do {
            System.out.print(mensaje);
            valor = teclado.nextLine().trim();
            if (valor.isEmpty()) System.out.println("No deje el dato vacio.");
        } while (valor.isEmpty());
        return valor;
    }
    // Punto 24: si se escribe texto, se captura el error y se vuelve a pedir.
    public static double numero(String mensaje, double minimo, double maximo) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = teclado.nextDouble();
                teclado.nextLine();
                if (valor >= minimo && valor <= maximo) return valor;
                System.out.println("Debe estar entre " + minimo + " y " + maximo);
            } catch (InputMismatchException error) {
                teclado.nextLine();
                System.out.println("Escriba un numero. Use punto para decimales.");
            }
        }
    }
    public static int entero(String mensaje, int minimo, int maximo) {
        while (true) {
            double valor = numero(mensaje, minimo, maximo);
            int entero = (int) valor;
            if (valor == entero) return entero;
            System.out.println("Debe ser un numero entero.");
        }
    }
}
