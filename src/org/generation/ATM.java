package org.generation;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ATM {
  private static BigDecimal saldo = new BigDecimal("10000.00");
  private static int intentosInvalidos = 0;
  private static ArrayList<String> movimientos = new ArrayList<>();

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      if (intentosInvalidos >= 3) {
        System.out.println("Demasiados intentos inválidos. Saliendo del sistema.");
        System.exit(0);
      }
      mostrarMenu();

      // Verificar si la entrada es un entero
      if (scanner.hasNextInt()) {
        int opcion = scanner.nextInt();

        if ((opcion >= 1 && opcion <= 5) || opcion == 9) {
          // La opción es válida, reiniciar el contador de intentos inválidos
          intentosInvalidos = 0;

          switch (opcion) {
            case 1:
              retirarDinero(scanner);
              break;
            case 2:
              hacerDepositos(scanner);
              break;
            case 3:
              consultarSaldo();
              break;
            case 4:
              quejas();
              break;
            case 5:
              verUltimosMovimientos();
              break;
            case 9:
              salir();
              break;
          }
        } else {
          // Opción inválida
          System.out.println("Opción inválida. Por favor, ingrese un número válido.");
          ++intentosInvalidos;
          System.out.println("Ingresaste un número. Intentos Inválidos = " + intentosInvalidos + " de 3");
        }
      } else {
        // Entrada no es un entero
        System.out.println("Opción inválida. Por favor, ingrese un número válido.");
        scanner.next(); // Limpiar el buffer del scanner
        ++intentosInvalidos;
        System.out.println("Ingresaste una letra. Intentos Inválidos = " + intentosInvalidos + " de 3");
      }
    }
  }

  private static void mostrarMenu() {
    System.out.println("\nOpciones del cajero:");
    System.out.println("1) Retirar dinero");
    System.out.println("2) Hacer depósitos");
    System.out.println("3) Consultar saldo");
    System.out.println("4) Quejas");
    System.out.println("5) Ver últimos movimientos");
    System.out.println("9) Salir del cajero");
    System.out.print("Ingrese su opción: ");
  }

  private static void retirarDinero(Scanner scanner) {
    System.out.println("\nSaldo disponible: $" + saldo);
    System.out.print("Ingrese la cantidad a retirar (múltiplo de $50, no más de $6,000): ");
    BigDecimal cantidadRetiro = scanner.nextBigDecimal();

    if (cantidadRetiro.remainder(new BigDecimal("50")).equals(BigDecimal.ZERO) &&
        cantidadRetiro.compareTo(new BigDecimal("6000")) <= 0 && cantidadRetiro.compareTo(saldo) <= 0) {
      saldo = saldo.subtract(cantidadRetiro);
      movimientos.add(generarMovimiento("Retiro de $" + cantidadRetiro));

      System.out.print("¿Desea donar $200 para la graduación de ch30? (Sí/No): ");
      String respuestaDonacion = scanner.next().toLowerCase();
      if ("sí".equals(respuestaDonacion) || "si".equals(respuestaDonacion)) {
        saldo = saldo.subtract(new BigDecimal("200"));
        System.out.println("¡Gracias por tu donación!");
        movimientos.add(generarMovimiento("Donación de $200 para graduación de ch30"));
      }

      System.out.println("Retiro exitoso. Saldo actual: $" + saldo);
    } else {
      System.out.println("Error en la cantidad ingresada o saldo insuficiente. Intente nuevamente.");
    }
  }

  private static void hacerDepositos(Scanner scanner) {
    System.out.println("\n1) Cuenta de cheques");
    System.out.println("2) Tarjeta de Crédito");

    int tipoCuenta = 0; // Inicializar con un valor predeterminado
    int intentos = 0;

    // Bucle para asegurarse de que se ingrese una opción válida (1 o 2)
    do {
        if (intentosInvalidos >= 3) {
            System.out.println("Demasiados intentos inválidos. Saliendo del sistema.");
            System.exit(0);
        }
        System.out.print("Seleccione la cuenta para depositar (1 o 2): ");

        // Verificar si la entrada es un entero
        if (scanner.hasNextInt()) {
            tipoCuenta = scanner.nextInt();
            // Verificar que la opción seleccionada sea válida
            if (tipoCuenta != 1 && tipoCuenta != 2) {
                System.out.println("Opción inválida. Por favor, ingrese 1 o 2.");
                intentosInvalidos++;
                intentos++;
            } else {
                // Restablecer intentos si la opción es válida
                intentos = 0;
            }
        } else {
            System.out.println("Opción inválida. Por favor, ingrese 1 o 2.");
            intentosInvalidos++;
            intentos++;
            scanner.next(); // Limpiar el buffer del scanner
        }
    } while ((tipoCuenta != 1 && tipoCuenta != 2) && intentos < 3);

    if (intentos >= 3) {
        System.out.println("Demasiados intentos inválidos. Saliendo del sistema.");
        System.exit(0);
    }

    System.out.print("Ingrese la cantidad a depositar (múltiplo de $50): ");

    // Continuar con el resto de la lógica para el depósito
    BigDecimal cantidadDeposito = scanner.nextBigDecimal();

    if (cantidadDeposito.remainder(new BigDecimal("50")).equals(BigDecimal.ZERO)) {
      if (tipoCuenta == 1) {
        saldo = saldo.add(cantidadDeposito);
        movimientos.add(generarMovimiento("Depósito a cuenta de cheques de $" + cantidadDeposito));
        System.out.println("Depósito exitoso. Nuevo saldo: $" + saldo);
      } else if (tipoCuenta == 2) {
    	saldo = saldo.subtract(cantidadDeposito);
        movimientos.add(generarMovimiento("Depósito a Tarjeta de Crédito de $" + cantidadDeposito));
        System.out.println("Depósito exitoso. Nuevo saldo: $" + saldo);
      }
    } else {
      System.out.println("Error en la cantidad ingresada. El depósito debe ser múltiplo de $50. Intente nuevamente.");
    }
  }

  private static void consultarSaldo() {
    System.out.println("\nSaldo actual: $" + saldo);
  }

  private static void quejas() {
    System.out.println("\nNo disponible por el momento, intente más tarde.");
  }

  private static void verUltimosMovimientos() {
    System.out.println("\nÚltimos movimientos:");

    for (String movimiento : movimientos) {
      System.out.println(movimiento);
    }
  }

  private static void salir() {
    System.out.println("\nGracias por usar el cajero automático. ¡Hasta luego!");
    System.exit(0);
  }

  private static String generarMovimiento(String descripcion) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    String fechaHora = dateFormat.format(new Date());
    return fechaHora + " " + descripcion;
  }
}