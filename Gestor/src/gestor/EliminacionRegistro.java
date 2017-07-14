/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import com.csvreader.CsvReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author estbm
 */
public class EliminacionRegistro extends EliminacionTemplate {
    String nombreTabla;
    String valorCampoClave;
    private int posicionCampoClave;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.valorCampoClave = (String) args[1];
        this.posicionCampoClave = (int) args[2];
    }
    
    @Override
    protected void RealizarOperacion() {
        try{
            
            File mod = new File("filesBD\\" + this.nombreTabla + ".BD");
            if(mod.exists())
            {
                
                //Renombrar el archivo
                mod.renameTo(new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD"));
                CsvReader flujoEntrada =  null;
                PrintWriter flujoSalida = null;
                boolean entro = false;
                try{
                    flujoEntrada = new CsvReader("filesBD\\" + this.nombreTabla + "AuxTabla77.BD");
                    flujoSalida = new PrintWriter("filesBD\\" + this.nombreTabla + ".BD");
                    
                }catch(Exception io)
                {
                    //flujoEntrada.close();
                    entro = true;
                    System.out.println("se han eliminado registros");    
                }
                
                //Lectura y escritura
                /*flujoEntrada.readRecord();
                flujoSalida.println(flujoEntrada.getRawRecord());*/
                if(!entro)
                {
                    while(flujoEntrada.readRecord()){
                        if(!flujoEntrada.get(this.posicionCampoClave).equals(this.valorCampoClave))
                            flujoSalida.println(flujoEntrada.getRawRecord());
                    }

                    flujoEntrada.close();
                    flujoSalida.close();

                    //Borrar el archivo auxiliar
                    new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD").delete();
                    flujoEntrada.close();
                    flujoSalida.close();
                }
                else
                {
                    File prueba = new File("filesBD\\" + this.nombreTabla + ".BD");
                    FileWriter escritura = new FileWriter(prueba);
                    BufferedWriter bw = new BufferedWriter(escritura);
                    bw.close();
                    escritura.close();
                }
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error inesperado, algo salio mal con los archivos internos");
        }
    }

    @Override
    protected void ActualizarMETA() {
        try{
        //Si el archivo META existe, leer y escribir su contenido
            if(new File(this.fileMETA).exists())
            {
                File file = new File(this.fileMETA);
                //Renombrar el archivo
                file.renameTo(new File(file.getParent() + "\\" + "fileAuxMeta77.BD"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "fileAuxMeta77.BD");
                
                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileMETA);//Crear el archivo luego de superar la condicion
                while(flujoEntrada.readRecord()){
                    if(!this.nombreTabla.equals(flujoEntrada.get(0)))
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    else{
                        flujoSalida.print(flujoEntrada.get(0) + ",");
                        flujoSalida.print((Integer.parseInt(flujoEntrada.get(1))-1) + ",");
                        for(int i = 2; i<flujoEntrada.getColumnCount(); i++){
                            flujoSalida.print(flujoEntrada.get(i));
                            if(i+1!=flujoEntrada.getColumnCount())
                                flujoSalida.print(",");
                        }
                        flujoSalida.println();
                    }
                }
                    
                flujoSalida.close();
                flujoEntrada.close();
                
                
                //Borrar el archivo auxiliar
                new File(file.getParent() + "\\" + "fileAuxMeta77.BD").delete();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        }
    }
}
