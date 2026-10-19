import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = Entrada.teclado;
        ArrayList<Alumno> alumnos = new ArrayList<Alumno>();
        int cantidad = Entrada.entero("Cantidad de alumnos: ", 1, 1000);
        double suma = 0;
        double mejorNota = 0;
        for (int i = 0; i < cantidad; i++) {
            String nombre = Entrada.texto("Nombre: ");
            // Punto 24: try-catch directamente en main, como pide el profesor.
            double[] notas = new double[3];
            String[] mensajes = {"Parcial 1 (0-10): ", "Parcial 2 (0-10): ", "Examen final (0-10): "};
            for (int j = 0; j < notas.length; j++) {
                boolean valida = false;
                while (!valida) {
                    try {
                        System.out.print(mensajes[j]);
                        notas[j] = teclado.nextDouble();
                        teclado.nextLine();
                        if (notas[j] >= 0 && notas[j] <= 10) valida = true;
                        else System.out.println("La nota debe estar entre 0 y 10.");
                    } catch (InputMismatchException error) {
                        teclado.nextLine();
                        System.out.println("Escriba un numero. Use punto para decimales.");
                    }
                }
            }
            double parcial1 = notas[0];
            double parcial2 = notas[1];
            double examen = notas[2];
            Alumno alumno = new Alumno(nombre, parcial1, parcial2, examen);
            alumnos.add(alumno);
            suma += alumno.calcularNotaOriginal();
            if (alumno.calcularNotaOriginal() > mejorNota) mejorNota = alumno.calcularNotaOriginal();
        }
        double promedio = suma / cantidad;
        System.out.println("Promedio original: " + promedio);
        // La condicion de aplicar curva viene de Practica 1.
        System.out.println("Regla de Practica 1: solo se aplica una curva si el promedio es menor a 6.");
        if (promedio < 6) {
            int tipo = Entrada.entero("1 Curva porcentual  2 Curva segun mejor alumno: ", 1, 2);
            if (tipo == 1) {
                double porcentaje = Entrada.numero("Porcentaje: ", 0, 1000);
                for (Alumno alumno : alumnos) alumno.aplicarCurvaPorcentual(porcentaje);
            } else {
                double puntos = 10 - mejorNota;
                System.out.println("Puntos de curva: " + puntos);
                for (Alumno alumno : alumnos) alumno.aplicarCurvaPuntos(puntos);
            }
        } else System.out.println("No se aplica curva porque el promedio es 6 o mayor.");
        // Punto 25: estadisticas despues de la curva.
        double sumaFinal = 0;
        int recuperados = 0;
        for (Alumno alumno : alumnos) {
            sumaFinal += alumno.getNotaFinal();
            if (alumno.calcularNotaOriginal() < 6 && alumno.estaAprobado()) recuperados++;
            System.out.println(alumno.getNombre() + " Original: " + alumno.calcularNotaOriginal() + " Final: " + alumno.getNotaFinal());
            if (alumno.estaAprobado()) System.out.println("Aprobado");
            else System.out.println("Reprobado");
        }
        double media = sumaFinal / cantidad;
        double sumaCuadrados = 0;
        for (Alumno alumno : alumnos) {
            double diferencia = alumno.getNotaFinal() - media;
            sumaCuadrados += diferencia * diferencia;
        }
        double desviacion = Math.sqrt(sumaCuadrados / cantidad);
        System.out.println("Media final: " + media);
        System.out.println("Desviacion estandar: " + desviacion);
        System.out.println("Recuperados: " + recuperados);
    }
}
class Alumno {
    private String nombre;
    private double notaParcial1;
    private double notaParcial2;
    private double examenFinal;
    private double notaFinal;
    public Alumno(String nombre, double notaParcial1, double notaParcial2, double examenFinal) {
        this.nombre = nombre;
        this.notaParcial1 = notaParcial1;
        this.notaParcial2 = notaParcial2;
        this.examenFinal = examenFinal;
        notaFinal = calcularNotaOriginal();
    }
    public String getNombre() { return nombre; }
    public double getNotaFinal() { return notaFinal; }
    // Punto 23: ponderaciones.
    public double calcularNotaOriginal() {
        return notaParcial1 * 0.30 + notaParcial2 * 0.30 + examenFinal * 0.40;
    }
    // Punto 21: porcentaje sobre la nota original.
    public void aplicarCurvaPorcentual(double porcentaje) {
        double original = calcularNotaOriginal();
        notaFinal = original + original * porcentaje / 100;
        if (notaFinal > 10) notaFinal = 10;
    }
    // Punto 22: puntos necesarios para llevar al mejor alumno hasta 10.
    public void aplicarCurvaPuntos(double puntos) {
        notaFinal = calcularNotaOriginal() + puntos;
        if (notaFinal > 10) notaFinal = 10;
    }
    public boolean estaAprobado() { return notaFinal >= 6; }
}

// Ayuda para leer datos sin repetir la misma validacion en cada menu.
class Entrada {
    static Scanner teclado = new Scanner(System.in).useLocale(Locale.US);
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
