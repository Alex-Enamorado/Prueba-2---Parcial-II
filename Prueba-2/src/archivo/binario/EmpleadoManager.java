package archivo.binario;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class EmpleadoManager {
    private RandomAccessFile rcods, remps;
    
    private void initCode() throws IOException{
        if(rcods.length() == 0){
            rcods.writeInt(1);
        }
    }
    
    private int getCode() throws IOException{
        rcods.seek(0);
        int code = rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(code+1);
        return code;
    }
    
    public EmpleadoManager(){
        try{
            //1 Asegurar el folder raiz.
            File root = new File("company");
            root.mkdir();
            rcods = new RandomAccessFile("company/codigos.emp","rw");
            remps = new RandomAccessFile("company/empleados.emp","rw");
        } catch(IOException e){
            
        }
    }
    
    // Formato empleado.emp (int code, String nombre, double salario, long fechaContratacion, long fechaDespido)
    public void addEmployee(String name, double salary)throws IOException{
        remps.seek(remps.length());
        int code = getCode();
        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(salary);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0);


        //Archivos individuales

    }
    private String employeeFolder(int code){
        return "company/empleado"+code;
    }

    private RandomAccessFile salesFilefor(int code)throws IOException{
        String dirPadre=employeeFolder(code);
        int yearActual=Calendar.getInstance().get(Calendar.YEAR);
        String path= dirPadre+"/ventas"+yearActual+".emp";
        return new RandomAccessFile(path,"rw");

    }
    /*
    Formato ventasYear.emp
    double ventaMes
    boolean pagó
     */


    private void createYearSalesFileFor(int code)throws IOException{
        RandomAccessFile ryear=salesFilefor(code);

        if(ryear.length()==0){
            for(int mes=0;mes<12;mes++){
                ryear.writeDouble(0);
                ryear.writeBoolean(false);

            }
        }
    }

    private void createEmployeeFolders(int code)throws IOException{
        File dir=new File(employeeFolder(code));
        dir.mkdir();
        createYearSalesFileFor(code);
    }
    //FORMATO IMPRIMIR employeeList
    //Codigo - Nombre - Salario - Contratacion
    //NO EMPLEADO DESPEDIDOS

    public void employeeList() throws IOException{
        remps.seek(0);
        while(remps.getFilePointer() < remps.length()){
            int code = remps.readInt();
            String name = remps.readUTF();
            double salary = remps.readDouble();
            Date fechaC=new Date(remps.readLong());
            long despido = remps.readLong();

            if(despido == 0){

                System.out.println(code + " - " + name + " - " + salary + " - " + fechaC);
            }
        }
    }

    private boolean isEmployeeActive(int code) throws IOException{
        remps.seek(0);
        while(remps.getFilePointer() < remps.length()){
            int codigo = remps.readInt();
            long puntero = remps.getFilePointer();
            remps.readUTF();
            remps.readDouble();
            remps.readLong();
            long despido = remps.readLong();


            if(codigo == code && despido == 0){
                remps.seek(puntero);
                return true;
            }
        }
        return false;
    }

    public boolean fireEmployee(int code) throws IOException{
        if(!isEmployeeActive(code)){
            return false;
        }
        
        remps.readUTF();
        remps.readDouble();
        remps.readLong();
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        return true;
    }

    public void addSaleToEmployee(int code, double monto) throws IOException{
        if(!isEmployeeActive(code)){
            return;
        }
        int mes = Calendar.getInstance().get(Calendar.MONTH);
        RandomAccessFile rventas = salesFilefor(code);
        rventas.seek(0);
        for(int i = 0; i < mes; i++){
            rventas.readDouble();
            rventas.readBoolean();
        }


        long puntero = rventas.getFilePointer();
        double ventas = rventas.readDouble();
        rventas.seek(puntero);
        rventas.writeDouble(ventas + monto);
    }



    
    private RandomAccessFile billsFilefor(int code) throws IOException{
        String dirPadre = employeeFolder(code);
        String path = dirPadre + "/recibos.emp";
        return new RandomAccessFile(path, "rw");
    }

}
