PRACTICA JAVA - VERSION BASICA JUNIOR

Se resolvieron los cinco ejercicios de Practica 1 con las 25 ampliaciones.
Cada carpeta contiene un programa independiente. No abras todos los Main
como si fueran un unico proyecto: tienen el mismo nombre a proposito.

COMO EJECUTAR
1. Instala un JDK 11 o posterior.
2. Entra en la carpeta del ejercicio desde la terminal.
3. Ejecuta: java Main.java

Si prefieres compilar:
  javac Main.java
  java Main

En cada Main.java, la clase Main esta primero y las clases que utiliza
estan debajo. Solo Main es public, como exige Java para este archivo.
Puedes leerlo todo sin cambiar de archivo. Si el profesor pide un archivo
por clase, se pueden separar manteniendo el codigo de cada clase.

TEMAS UTILIZADOS
- Variables y operaciones.
- if, else, switch.
- for, for-each, while, do-while.
- Clases, objetos, constructores, atributos privados, getters y metodos.
- ArrayList.
- Herencia y clase abstracta, porque las piden los puntos 5 y 13.
- FileWriter y Scanner para archivos, por los puntos 9 y 20.
- try-catch, porque lo pide el punto 24 y se necesita para los archivos.

No se usan mapas, lambdas, streams, serializacion ni clases anidadas.
La clase Entrada, al final de cada archivo, repite tres metodos sencillos
para leer texto, numeros e enteros sin que el programa se cierre al
escribir un dato incorrecto. El try-catch del punto 24 esta en su metodo
numero, que es llamado desde el main del ejercicio de curvas.

DONDE ESTA CADA PUNTO
01_Cajero:
1 transferir en CuentaBancaria.
2 registrar y mostrarMovimientos: ultimos cinco movimientos.
3 ArrayList de cuentas y solicitud del titular antes del PIN.
4 clase Banco con buscarCuenta y validarPin.
5 Cuenta abstracta, CuentaBancaria, CuentaAhorro y CuentaCorriente.

02_Registro:
6 Asignatura y ArrayList de asignaturas en Estudiante.
7 Curso calcula promedio, aprobados, mejor y peor.
8 aprobo exige promedio >= 6 y faltas < 10.
9 guardarReporte genera reporte.txt.
10 buscarEstudiante y modificarNota.

03_Tienda:
11 categoria y mostrarCategoria.
12 subtotal, descuento y total en Carrito.
13 ProductoAlimenticio y ProductoElectronico.
14 Cliente y login simulado mediante email.
15 Inventario consulta y disminuye el stock para el carrito.
DetalleCompra es una clase sencilla para guardar producto y cantidad.

04_Agenda:
16 ArrayList de contactos.
17 telefono de 9 digitos, email con @ y punto, sin duplicados.
18 buscarContacto devuelve una lista con coincidencias parciales.
19 grupo y mostrarContactosPorGrupo.
20 guardarEnArchivo y cargarDesdeArchivo, usando agenda.txt.

05_Curva:
21 aplicarCurvaPorcentual.
22 curva de puntos igual a 10 menos la mayor nota del grupo.
23 dos parciales del 30% y examen del 40%.
24 Entrada.numero captura InputMismatchException y vuelve a preguntar.
25 media, desviacion estandar y cantidad de recuperados.

DECISIONES DEL ENUNCIADO
- Se usan notas de 0 a 10 y se aprueba desde 6.
- Las asignaturas variables reemplazan el arreglo inicial de tres notas.
- Se elige una de las dos curvas; no se aplican juntas.
- Como dice Practica 1, solo hay curva si el promedio original es menor a 6.
- La desviacion es poblacional: se divide entre la cantidad de alumnos.
- El promedio del registro es el promedio de los promedios individuales.
- Si hay empate para mejor o peor, se muestra al primero.
- Ahorro: interes del 1% en depositos directos, no en transferencias.
- Corriente: permite sobregiro de 200 para retirar o transferir.
- Tres PIN incorrectos terminan el cajero como cuenta bloqueada.
  El bloqueo y los saldos son de esta ejecucion, sin base de datos.
- Tienda: descuento del 10% si el subtotal supera 1000, no si es igual.
- El stock se descuenta al agregar al carrito, como pide el punto 15.
- Login de tienda: ana@correo.com o luis@correo.com, sin contrasena.
- Agenda: nueve digitos como el ejemplo del profesor. Para diez digitos,
  cambia el 9 de la validacion y del mensaje por 10.
- Agenda guarda cuatro lineas por contacto: nombre, telefono, email, grupo.
- Los archivos agenda.txt y reporte.txt se crean donde ejecutas Java.
- No se incluyeron funciones adicionales ajenas a los ejercicios.

PRUEBAS
Se ejecutaron los cinco programas con Java 17 y datos de ejemplo.
Se revisaron transferencias, intereses, sobregiro, bloqueo por PIN,
notas y faltas, modificacion y guardado de reporte, descuento,
agenda persistente, duplicados, ambas curvas, curso sin curva
y entrada de texto en un campo numerico.
