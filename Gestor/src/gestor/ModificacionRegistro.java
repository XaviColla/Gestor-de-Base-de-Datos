/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import com.csvreader.CsvReader;
import com.sun.javafx.geom.Vec2d;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author estbm
 */
public class ModificacionRegistro extends ModificacionTemplate{
     String nombreTabla;
    String campoClave;
    String nombreCampo;
    String valorCampo;
    private int posicionCampoClave;
    private int[] logitudesCampos;
    private String[] campos;
    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.campoClave = (String) args[1];
        this.nombreCampo = (String) args[2];
        this.valorCampo = (String) args[3];
        this.posicionCampoClave = (int) args[4];
        this.logitudesCampos = (int[]) args[5];
        this.campos = (String[]) args[6];
        
    }
    
    @Override
    protected void RealizarOperacion() {
        try{
            
            File mod = new File("filesBD\\" + this.nombreTabla + ".BD");
            if(mod.exists())
            {
                
                //Renombrar el archivo
                mod.renameTo(new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD"));
                CsvReader flujoEntrada = new CsvReader("filesBD\\" + this.nombreTabla + "AuxTabla77.BD");
                PrintWriter flujoSalida = new PrintWriter("filesBD\\" + this.nombreTabla + ".BD");
                
                System.out.println("Antes...");
                
                //Lectura y escritura
                //flujoEntrada.readRecord();
                //flujoSalida.println(flujoEntrada.getRawRecord());
                int posicionCampoCambio = -1;
                System.out.println("Com: " + flujoEntrada.getRawRecord());
                for(int i = 0; i< campos.length ; i++){//flujoEntrada.getColumnCount();i++){
                    if(campos[i].equals(this.nombreCampo)){//if(flujoEntrada.get(i).equals(this.nombreCampo)){
                        posicionCampoCambio = i;
                        break;
                    }
                }
                
                if(posicionCampoCambio==-1)
                    throw new Error("Error inesperado, no se pudo acceder a los datos de la tabla");

                while(flujoEntrada.readRecord()){
                    if(!flujoEntrada.get(this.posicionCampoClave).equals(this.campoClave))
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    else{
                        for(int i = 0; i<flujoEntrada.getColumnCount(); i++){
                            String palabra = "";
                            if(i!=posicionCampoCambio){
                                palabra += flujoEntrada.get(i);
                                if(flujoEntrada.get(i).length() < logitudesCampos[i])
                                {
                                    for (int j = flujoEntrada.get(i).length(); j < logitudesCampos[i]; j++) {
                                        palabra += " ";
                                    }
                                }
                                flujoSalida.print(palabra); 
                            }    
                            else
                            {
                                palabra = "";
                                palabra += valorCampo;
                                if(valorCampo.length() < logitudesCampos[i])
                                {
                                    for (int j = valorCampo.length(); j < logitudesCampos[i]; j++) {
                                        palabra += " ";
                                    }
                                }
                                flujoSalida.print(palabra); 
                            }
                                
                            if(i+1 == flujoEntrada.getColumnCount())
                                flujoSalida.print("\n");
                            else
                                flujoSalida.print(",");
                        }
                        flujoSalida.println("");
                    }
                }
                flujoEntrada.close();
                flujoSalida.close();
                
                //Borrar el archivo auxiliar
                new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD").delete();
                flujoEntrada.close();
                flujoSalida.close();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error inesperado, algo salio mal con los archivos internos");
        }
    }

    @Override
    protected void ActualizarMETA() {
        //Actualizaciones AQUI
    }
}
