import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(new Cliente("Ana", "ana@correo.com"));
        clientes.add(new Cliente("Luis", "luis@correo.com"));
        System.out.println("Clientes: ana@correo.com | luis@correo.com");
        String email = Entrada.texto("Email: ");
        Cliente cliente = null;
        for (Cliente actual : clientes) {
            if (actual.getEmail().equalsIgnoreCase(email)) cliente = actual;
        }
        if (cliente == null) {
            System.out.println("Cliente no registrado.");
            return;
        }
        Inventario inventario = new Inventario();
        inventario.agregar(new ProductoAlimenticio("Arroz", 50, 20, "Alimentos", "2027-12-31"));
        inventario.agregar(new ProductoElectronico("Audifonos", 600, 5, "Electronica", 12));
        inventario.agregar(new ProductoElectronico("Teclado", 450, 8, "Electronica", 24));
        Carrito carrito = new Carrito(cliente, inventario);
        int opcion;
        do {
            opcion = Entrada.entero("1 Comprar  2 Ver factura  0 Finalizar: ", 0, 2);
            switch (opcion) {
                case 1:
                    String categoria = Entrada.texto("Categoria: Alimentos / Electronica / Todas: ");
                    inventario.mostrarCategoria(categoria);
                    String nombre = Entrada.texto("Nombre del producto (0 para volver): ");
                    if (!nombre.equals("0")) {
                        Producto producto = inventario.buscar(nombre);
                        if (producto == null) System.out.println("No existe.");
                        else if (!categoria.equalsIgnoreCase("Todas") && !producto.getCategoria().equalsIgnoreCase(categoria)) {
                            System.out.println("No pertenece a esa categoria.");
                        } else {
                            int cantidad = Entrada.entero("Cantidad: ", 1, 100000);
                            if (carrito.agregarProducto(nombre, cantidad)) System.out.println("Agregado.");
                            else System.out.println("Stock insuficiente.");
                        }
                    }
                    break;
                case 2:
                    carrito.mostrarFactura();
                    break;
            }
        } while (opcion != 0);
        carrito.mostrarFactura();
    }
}

// Puntos 11 y 13.
class Producto {
    private String nombre;
    private double precio;
    private int stock;
    private String categoria;
    public Producto(String nombre, double precio, int stock, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public boolean hayStock(int cantidad) { return cantidad > 0 && cantidad <= stock; }
    public void disminuirStock(int cantidad) {
        if (hayStock(cantidad)) stock -= cantidad;
    }
    public void mostrarInfo() {
        System.out.println(nombre + " Precio: " + precio + " Stock: " + stock + " Categoria: " + categoria);
    }
}
class ProductoAlimenticio extends Producto {
    private String fechaCaducidad;
    public ProductoAlimenticio(String nombre, double precio, int stock, String categoria, String fecha) {
        super(nombre, precio, stock, categoria);
        fechaCaducidad = fecha;
    }
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Caducidad: " + fechaCaducidad);
    }
}
class ProductoElectronico extends Producto {
    private int mesesGarantia;
    public ProductoElectronico(String nombre, double precio, int stock, String categoria, int meses) {
        super(nombre, precio, stock, categoria);
        mesesGarantia = meses;
    }
    public void mostrarInfo() {
        super.mostrarInfo();
        System.out.println("Garantia: " + mesesGarantia + " meses");
    }
}
// Punto 14.
class Cliente {
    private String nombre;
    private String email;
    public Cliente(String nombre, String email) { this.nombre = nombre; this.email = email; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}
// Punto 15: el carrito solicita el stock al inventario.
class Inventario {
    private ArrayList<Producto> productos = new ArrayList<Producto>();
    public void agregar(Producto producto) { productos.add(producto); }
    public Producto buscar(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) return producto;
        }
        return null;
    }
    public boolean descontarStock(String nombre, int cantidad) {
        Producto producto = buscar(nombre);
        if (producto == null || !producto.hayStock(cantidad)) return false;
        producto.disminuirStock(cantidad);
        return true;
    }
    public void mostrarCategoria(String categoria) {
        for (Producto producto : productos) {
            if (categoria.equalsIgnoreCase("Todas") || producto.getCategoria().equalsIgnoreCase(categoria)) {
                producto.mostrarInfo();
            }
        }
    }
}
// Una linea de factura guarda producto y cantidad, sin mapas.
class DetalleCompra {
    private Producto producto;
    private int cantidad;
    public DetalleCompra(Producto producto, int cantidad) { this.producto = producto; this.cantidad = cantidad; }
    public double calcularImporte() { return producto.getPrecio() * cantidad; }
    public void mostrar() {
        System.out.println(producto.getNombre() + " x " + cantidad + " = " + calcularImporte());
    }
}
class Carrito {
    private Cliente cliente;
    private Inventario inventario;
    private ArrayList<DetalleCompra> detalles = new ArrayList<DetalleCompra>();
    public Carrito(Cliente cliente, Inventario inventario) { this.cliente = cliente; this.inventario = inventario; }
    public boolean agregarProducto(String nombre, int cantidad) {
        if (!inventario.descontarStock(nombre, cantidad)) return false;
        detalles.add(new DetalleCompra(inventario.buscar(nombre), cantidad));
        return true;
    }
    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleCompra detalle : detalles) subtotal += detalle.calcularImporte();
        return subtotal;
    }
    // Punto 12.
    public double calcularDescuento() {
        if (calcularSubtotal() > 1000) return calcularSubtotal() * 0.10;
        return 0;
    }
    public double calcularTotal() { return calcularSubtotal() - calcularDescuento(); }
    public void mostrarFactura() {
        System.out.println("Cliente: " + cliente.getNombre() + " Email: " + cliente.getEmail());
        for (DetalleCompra detalle : detalles) detalle.mostrar();
        System.out.println("Subtotal: " + calcularSubtotal());
        System.out.println("Descuento: " + calcularDescuento());
        System.out.println("Total: " + calcularTotal());
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
