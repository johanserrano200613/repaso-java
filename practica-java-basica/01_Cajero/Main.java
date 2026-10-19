import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Banco banco = new Banco();
        banco.agregar(new CuentaAhorro("Ana", 1000, "1234"));
        banco.agregar(new CuentaCorriente("Luis", 500, "4321", 200));
        System.out.println("Cuentas: Ana PIN 1234 | Luis PIN 4321");
        String titular = Entrada.texto("Titular: ");
        CuentaBancaria cuenta = banco.buscarCuenta(titular);
        if (cuenta == null) {
            System.out.println("No existe la cuenta.");
            return;
        }
        boolean correcto = false;
        int intentos = 0;
        while (intentos < 3 && !correcto) {
            correcto = banco.validarPin(titular, Entrada.texto("PIN: "));
            intentos++;
            if (!correcto) System.out.println("PIN incorrecto.");
        }
        if (!correcto) {
            System.out.println("Cuenta bloqueada. Fin del programa.");
            return;
        }
        int opcion;
        do {
            opcion = Entrada.entero("1 Saldo  2 Depositar  3 Retirar  4 Transferir  5 Movimientos  0 Salir: ", 0, 5);
            switch (opcion) {
                case 1:
                    System.out.println("Saldo: " + cuenta.consultarSaldo());
                    break;
                case 2:
                    cuenta.depositar(Entrada.numero("Monto: ", 0.01, 1000000000));
                    break;
                case 3:
                    if (!cuenta.retirar(Entrada.numero("Monto: ", 0.01, 1000000000))) {
                        System.out.println("Saldo insuficiente.");
                    }
                    break;
                case 4:
                    CuentaBancaria destino = banco.buscarCuenta(Entrada.texto("Titular destino: "));
                    double monto = Entrada.numero("Monto: ", 0.01, 1000000000);
                    if (cuenta.transferir(destino, monto)) System.out.println("Transferencia realizada.");
                    else System.out.println("No se pudo transferir.");
                    break;
                case 5:
                    cuenta.mostrarMovimientos();
                    break;
            }
        } while (opcion != 0);
    }
}

// Punto 5: clase abstracta y herencia.
abstract class Cuenta {
    private String titular;
    private double saldo;
    public Cuenta(String titular, double saldo) {
        this.titular = titular;
        this.saldo = saldo;
    }
    public String getTitular() { return titular; }
    public double consultarSaldo() { return saldo; }
    protected void cambiarSaldo(double valor) { saldo = valor; }
    public abstract void depositar(double monto);
    public abstract boolean retirar(double monto);
}

class CuentaBancaria extends Cuenta {
    private String pin;
    private ArrayList<String> movimientos;
    public CuentaBancaria(String titular, double saldo, String pin) {
        super(titular, saldo);
        this.pin = pin;
        movimientos = new ArrayList<String>();
    }
    public boolean validarPin(String pin) { return this.pin.equals(pin); }
    // Punto 2: como maximo cinco movimientos.
    public void registrar(String movimiento) {
        if (movimientos.size() == 5) movimientos.remove(0);
        movimientos.add(movimiento);
    }
    public void mostrarMovimientos() {
        if (movimientos.size() == 0) System.out.println("Sin movimientos.");
        for (String movimiento : movimientos) System.out.println(movimiento);
    }
    public boolean haySaldo(double monto) {
        return monto > 0 && monto <= consultarSaldo();
    }
    public void depositar(double monto) {
        if (monto > 0) {
            cambiarSaldo(consultarSaldo() + monto);
            registrar("Deposito: +" + monto);
        }
    }
    public boolean retirar(double monto) {
        if (!haySaldo(monto)) return false;
        cambiarSaldo(consultarSaldo() - monto);
        registrar("Retiro: -" + monto);
        return true;
    }
    // Punto 1: la transferencia no produce intereses.
    public boolean transferir(CuentaBancaria destino, double monto) {
        if (destino == null || destino == this || !haySaldo(monto)) return false;
        cambiarSaldo(consultarSaldo() - monto);
        destino.cambiarSaldo(destino.consultarSaldo() + monto);
        registrar("Transferencia a " + destino.getTitular() + ": -" + monto);
        destino.registrar("Transferencia de " + getTitular() + ": +" + monto);
        return true;
    }
}

class CuentaAhorro extends CuentaBancaria {
    public CuentaAhorro(String titular, double saldo, String pin) { super(titular, saldo, pin); }
    public void depositar(double monto) {
        if (monto > 0) {
            super.depositar(monto);
            double interes = monto * 0.01;
            cambiarSaldo(consultarSaldo() + interes);
            registrar("Interes: +" + interes);
        }
    }
}

class CuentaCorriente extends CuentaBancaria {
    private double sobregiro;
    public CuentaCorriente(String titular, double saldo, String pin, double sobregiro) {
        super(titular, saldo, pin);
        this.sobregiro = sobregiro;
    }
    public boolean haySaldo(double monto) {
        return monto > 0 && monto <= consultarSaldo() + sobregiro;
    }
}

// Puntos 3 y 4: multiples cuentas administradas por Banco.
class Banco {
    private ArrayList<CuentaBancaria> cuentas = new ArrayList<CuentaBancaria>();
    public void agregar(CuentaBancaria cuenta) { cuentas.add(cuenta); }
    public CuentaBancaria buscarCuenta(String titular) {
        for (CuentaBancaria cuenta : cuentas) {
            if (cuenta.getTitular().equalsIgnoreCase(titular)) return cuenta;
        }
        return null;
    }
    public boolean validarPin(String titular, String pin) {
        CuentaBancaria cuenta = buscarCuenta(titular);
        if (cuenta == null) return false;
        return cuenta.validarPin(pin);
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
