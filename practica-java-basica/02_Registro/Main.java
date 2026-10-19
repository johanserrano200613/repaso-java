import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Curso curso = new Curso();
        int cantidad = Entrada.entero("Cantidad de estudiantes: ", 1, 1000);
        for (int i = 0; i < cantidad; i++) {
            String nombre = Entrada.texto("Nombre: ");
            if (curso.buscarEstudiante(nombre) != null) {
                System.out.println("Nombre repetido.");
                i--;
            } else {
                int faltas = Entrada.entero("Faltas: ", 0, 10000);
                Estudiante estudiante = new Estudiante(nombre, faltas);
                int materias = Entrada.entero("Cantidad de asignaturas: ", 1, 100);
                for (int j = 0; j < materias; j++) {
                    String materia = Entrada.texto("Asignatura: ");
                    if (estudiante.buscarAsignatura(materia) != null) {
                        System.out.println("Asignatura repetida.");
                        j--;
                    } else {
                        double nota = Entrada.numero("Nota (0-10): ", 0, 10);
                        estudiante.agregarAsignatura(new Asignatura(materia, nota));
                    }
                }
                curso.agregarEstudiante(estudiante);
            }
        }
        int opcion;
        do {
            opcion = Entrada.entero("1 Reporte  2 Buscar  3 Modificar nota  4 Guardar  0 Salir: ", 0, 4);
            switch (opcion) {
                case 1:
                    System.out.println(curso.crearReporte());
                    break;
                case 2:
                    Estudiante encontrado = curso.buscarEstudiante(Entrada.texto("Nombre: "));
                    if (encontrado == null) System.out.println("No encontrado.");
                    else encontrado.mostrarInfo();
                    break;
                case 3:
                    String nombre = Entrada.texto("Estudiante: ");
                    Estudiante estudiante = curso.buscarEstudiante(nombre);
                    if (estudiante == null) {
                        System.out.println("Estudiante no encontrado.");
                        break;
                    }
                    estudiante.mostrarInfo();
                    String materia = Entrada.texto("Asignatura: ");
                    double nota = Entrada.numero("Nueva nota: ", 0, 10);
                    if (curso.modificarNota(nombre, materia, nota)) System.out.println("Nota modificada.");
                    else System.out.println("Estudiante o asignatura no encontrado.");
                    break;
                case 4:
                    curso.guardarReporte("reporte.txt");
                    break;
            }
        } while (opcion != 0);
    }
}

// Punto 6: cada estudiante tiene una lista de asignaturas.
class Asignatura {
    private String nombre;
    private double notaFinal;
    public Asignatura(String nombre, double notaFinal) { this.nombre = nombre; this.notaFinal = notaFinal; }
    public String getNombre() { return nombre; }
    public double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(double nota) { notaFinal = nota; }
}

class Estudiante {
    private String nombre;
    private int faltas;
    private ArrayList<Asignatura> asignaturas = new ArrayList<Asignatura>();
    public Estudiante(String nombre, int faltas) { this.nombre = nombre; this.faltas = faltas; }
    public String getNombre() { return nombre; }
    public int getFaltas() { return faltas; }
    public void mostrarInfo() {
        System.out.println(nombre + " Promedio: " + calcularPromedio() + " Faltas: " + faltas);
        for (Asignatura asignatura : asignaturas) {
            System.out.println(asignatura.getNombre() + ": " + asignatura.getNotaFinal());
        }
    }
    public void agregarAsignatura(Asignatura asignatura) { asignaturas.add(asignatura); }
    public Asignatura buscarAsignatura(String nombre) {
        for (Asignatura asignatura : asignaturas) {
            if (asignatura.getNombre().equalsIgnoreCase(nombre)) return asignatura;
        }
        return null;
    }
    public double calcularPromedio() {
        double suma = 0;
        for (Asignatura asignatura : asignaturas) suma += asignatura.getNotaFinal();
        if (asignaturas.size() == 0) return 0;
        return suma / asignaturas.size();
    }
    // Punto 8: ambas condiciones deben cumplirse.
    public boolean aprobo() { return calcularPromedio() >= 6 && faltas < 10; }
}

// Puntos 7, 9 y 10.
class Curso {
    private ArrayList<Estudiante> estudiantes = new ArrayList<Estudiante>();
    public void agregarEstudiante(Estudiante estudiante) { estudiantes.add(estudiante); }
    public Estudiante buscarEstudiante(String nombre) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNombre().equalsIgnoreCase(nombre)) return estudiante;
        }
        return null;
    }
    public boolean modificarNota(String nombre, String materia, double nota) {
        Estudiante estudiante = buscarEstudiante(nombre);
        if (estudiante == null || nota < 0 || nota > 10) return false;
        Asignatura asignatura = estudiante.buscarAsignatura(materia);
        if (asignatura == null) return false;
        asignatura.setNotaFinal(nota);
        return true;
    }
    public double promedioGeneral() {
        double suma = 0;
        for (Estudiante estudiante : estudiantes) suma += estudiante.calcularPromedio();
        if (estudiantes.size() == 0) return 0;
        return suma / estudiantes.size();
    }
    public int contarAprobados() {
        int aprobados = 0;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.aprobo()) aprobados++;
        }
        return aprobados;
    }
    public Estudiante mejorEstudiante() {
        if (estudiantes.size() == 0) return null;
        Estudiante mejor = estudiantes.get(0);
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.calcularPromedio() > mejor.calcularPromedio()) mejor = estudiante;
        }
        return mejor;
    }
    public Estudiante peorEstudiante() {
        if (estudiantes.size() == 0) return null;
        Estudiante peor = estudiantes.get(0);
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.calcularPromedio() < peor.calcularPromedio()) peor = estudiante;
        }
        return peor;
    }
    public String crearReporte() {
        String reporte = "REGISTRO ACADEMICO\n";
        for (Estudiante estudiante : estudiantes) {
            reporte += estudiante.getNombre() + " Promedio: " + estudiante.calcularPromedio();
            reporte += " Faltas: " + estudiante.getFaltas();
            if (estudiante.aprobo()) reporte += " Aprobado\n";
            else reporte += " Reprobado\n";
        }
        reporte += "Promedio general: " + promedioGeneral() + "\n";
        reporte += "Aprobados: " + contarAprobados() + "\n";
        reporte += "Reprobados: " + (estudiantes.size() - contarAprobados()) + "\n";
        if (estudiantes.size() > 0) {
            reporte += "Mejor: " + mejorEstudiante().getNombre() + "\n";
            reporte += "Peor: " + peorEstudiante().getNombre() + "\n";
        }
        return reporte;
    }
    public void guardarReporte(String nombreArchivo) {
        try {
            FileWriter archivo = new FileWriter(nombreArchivo);
            archivo.write(crearReporte());
            archivo.close();
            System.out.println("Reporte guardado.");
        } catch (IOException error) {
            System.out.println("No se pudo guardar el reporte.");
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
