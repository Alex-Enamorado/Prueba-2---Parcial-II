package archivo.binario;
import java.io.IOException;
import java.util.Scanner;


public class EmpleadoMain {
    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        EmpleadoManager manager = new EmpleadoManager();
        int op = 0;

        do {
            System.out.println("\n\n==========MENU========== ");
            System.out.println("1. Agregar Empleado");
            System.out.println("2. Listar Empleado(No Despedidos)");
            System.out.println("3. Agregar Venta");
            System.out.println("4. Pagar Empleado");
            System.out.println("5. Despedir Empleado");
            System.out.println("0. Salir");
            System.out.println("Escoja una opcion: ");
            op = sc.nextInt();
            sc.nextLine();

            switch(op){
                case 1: {
                    System.out.println("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.println("Salario: ");
                    double salario = sc.nextDouble();
                    manager.addEmployee(nombre, salario);
                    break;
                }
                case 2: {
                    manager.employeeList();
                    break;
                }
                case 3: {

                    break;
                }
                case 4: {

                    break;
                }
                case 5: {
                    System.out.println("Codigo: ");
                    int codigo = sc.nextInt();
                    manager.fireEmployee(codigo);
                    break;
                }
            }

        } while(op != 0);
    }
}
