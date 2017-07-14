/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author estbm
 */
public class ModificacionTabla extends ModificacionTemplate {

    String nombreTabla;
    String nombreCampo;
    String nuevoCampo;
    private int []longitudesCapos; 

    private int posCampo = -1;
    private ArrayList<String> valCampos;

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.nombreCampo = (String) args[1];
        this.nuevoCampo = (String) args[2];
        this.longitudesCapos = (int[])args[3];
        this.valCampos = (ArrayList<String>) (List) args[4];
        
    }

    @Override
    protected void RealizarOperacion() {
        /*try {

            File mod = new File("filesBD\\" + this.nombreTabla + ".BD");
            if (mod.exists()) {

                //Renombrar el archivo
                mod.renameTo(new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD"));
                CsvReader flujoEntrada = new CsvReader("filesBD\\" + this.nombreTabla + "AuxTabla77.BD");

                //Busqueda de la posicion del campo
                this.posCampo = -1;
                flujoEntrada.readRecord();

                for (int i = 0; i < flujoEntrada.getColumnCount(); i++) {
                    if (flujoEntrada.get(i).trim().equals(this.nombreCampo)) {
                        this.posCampo = i;
                        this.valCampos.add(this.nuevoCampo);
                    } else {
                        this.valCampos.add(flujoEntrada.get(i));
                    }
                }
                if (this.posCampo == -1) {
                    throw new Error("Lo sentimos, no se ha encontrado el campo a modificar");
                }
                
                //Cambiar la cabecera del archivo
                String valorAgregar = "";
                int cont = 0;
                for (String valor : valCampos) {
                    valorAgregar += valor;
                    if(valor.length() < longitudesCapos[cont])
                    {
                        for (int i = valor.length(); i < longitudesCapos[cont]; i++) {
                            valorAgregar += " ";
                        }
                    }
                    valorAgregar += ",";
                    cont++;
                }
                valorAgregar = valorAgregar.substring(0 , valorAgregar.length()-1);
                PrintWriter flujoSalida = new PrintWriter("filesBD\\" + this.nombreTabla + ".BD");//Crear el archivo luego de superar la condicion
                flujoSalida.println(valorAgregar);

                //El siguiente proceso es por si el archivo contiene registros, es decir una tabla con registros
                //Lectura y escritura
                while (flujoEntrada.readRecord()) {
                    flujoSalida.println(flujoEntrada.getRawRecord());
                }

                flujoEntrada.close();
                flujoSalida.close();

                //Borrar el archivo auxiliar
                new File("filesBD\\" + this.nombreTabla + "AuxTabla77.BD").delete();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error inesperado, algo salio mal con los archivos internos");
        }*/
    }

    @Override
    protected void ActualizarMETA() {
        try {
            //Si el archivo META existe, leer y escribir su contenido
            if (new File(this.fileMETA).exists()) {
                File file = new File(this.fileMETA);
                //Renombrar el archivo
                file.renameTo(new File(file.getParent() + "\\" + "fileAuxMeta77.BD"));
                CsvReader flujoEntrada = new CsvReader(file.getParent() + "\\" + "fileAuxMeta77.BD");

                //Lectura y escritura
                PrintWriter flujoSalida = new PrintWriter(this.fileMETA);//Crear el archivo luego de superar la condicion
                while (flujoEntrada.readRecord()) {
                    if (!this.nombreTabla.equals(flujoEntrada.get(0))) {
                        flujoSalida.println(flujoEntrada.getRawRecord());
                    } else {
                        for (int i = 0; i < 3; i++) {
                            flujoSalida.print(flujoEntrada.get(i) + ",");
                        }
                        /*if(nombreCampo.equals(flujoEntrada.get(3)))
                            flujoSalida.print(nuevoCampo + ",");
                        else
                            flujoSalida.print(flujoEntrada.get(3) + ",");*/
                        for (int i = 0; i < longitudesCapos.length; i++) {
                            flujoSalida.print(longitudesCapos[i] + ",");
                        }
                        flujoSalida.print(this.valCampos.toString().replace("[", "").replace("]", "").replace(" ", ""));
                        flujoSalida.print("," + "ENCRIPT" + ",");
                        for (int i = 3 + longitudesCapos.length * 2 + 1; i < flujoEntrada.getColumnCount() ; i++) {
                            if(nombreCampo.equals(flujoEntrada.get(i)))
                                flujoSalida.print(nuevoCampo);
                            else
                                flujoSalida.print(flujoEntrada.get(i));
                            if(i != flujoEntrada.getColumnCount()-1)
                            {
                                flujoSalida.print(",");
                            }
                            else
                            {
                                flujoSalida.println("");
                            }
                        }
                    }
                }

                flujoEntrada.close();
                flujoSalida.close();

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
