/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import com.csvreader.CsvReader;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTable;

/**
 *
 * @author estbm
 */
public class SeleccionarTabla {

    private final String nombreTabla;
    private final String nombreCampo;
    private final String valorCampo;
    private int posicion;
    private String ordenamiento;
    private int numarch;
    protected final String fileMETA = "filesBD\\META.BD";

    public SeleccionarTabla(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.nombreCampo = (String) args[1];
        this.valorCampo = (String) args[2];
        // System.out.println((int)args[3]);
        this.posicion = (int) args[3];
        this.ordenamiento = (String) args[4];
        this.numarch = (int) args[5];
    }

    public void Visualizar() throws IOException {
        try {

            //Numero de filas y columnas
            File path = new File("filesBD\\" + nombreTabla + ".bd");
            CsvReader reader = new CsvReader("filesBD\\" + nombreTabla + ".bd");
            int filas = 0;
            int columnas = 0;
            while (reader.readRecord()) {
                if (reader.get(posicion).equals(valorCampo)) {
                    filas++;
                }
                columnas = reader.getColumnCount();
            }
            reader.close();
            filas++;

            //Conseguir las cabeceras
            CsvReader ar = new CsvReader(this.fileMETA);
            int contador = 0;
            String[] campos = null;
            String[] logitudes = null;
            String[] camposEncriptados = null;
            //int numArchivos = 0;
            while (ar.readRecord()) {
                if (ar.get(0).equals(nombreTabla)) {
                    for (int j = 3; j < ar.getColumnCount(); j++) {
                        if (!ar.get(j).equals("ENCRIP")) {
                            System.out.println(ar.get(j));
                            contador++;
                        } else {
                            break;
                        }
                    }
                    int numcampos = contador / 2;
                    campos = new String[numcampos];
                    logitudes = new String[numcampos];
                    camposEncriptados = new String[ar.getColumnCount() - (3 + (contador + 1))];
                    for (int i = 3 + numcampos; i < 3 + contador; i++) {
                        campos[i - (3 + numcampos)] = ar.get(i);
                    }
                    for (int i = 3; i < 3 + numcampos; i++) {
                        logitudes[i - 3] = ar.get(i);
                    }
                    for (int i = 3 + (contador + 1); i < ar.getColumnCount(); i++) {
                        camposEncriptados[i - (3 + (contador + 1))] = ar.get(i);
                    }
                    break;
                }
            }
            reader = new CsvReader("filesBD\\" + nombreTabla + ".bd");
            /*reader.readRecord();
            String matriz[][] = new String[filas][columnas];
            String cabecera[] = new String[columnas];
            for (int i = 0; i<columnas; i++){
                cabecera[i] = reader.get(i);
            
            }*/
            //Conseguir el cuerpo de la tabla
            int i = 0;
            boolean entre = false;
            while (reader.readRecord()) {
                /*if (numarch == -2) {
                    File prueba = new File("filesBD\\" + nombreTabla + "Seleccionada.bd");
                    FileWriter escritura = new FileWriter(prueba);
                    BufferedWriter bw = new BufferedWriter(escritura);
                    try {
                        String frase = "";
                        for (int l = 0; l < reader.getColumnCount(); l++) {
                            frase+= reader.get(l);
                            for (int j = 0; j < frase.length(); j++) {
                                frase += " ";
                            }
                            frase += ",";
                        }
                        frase = frase.substring(0, frase.length() - 1);
                        bw.write(frase);
                        bw.newLine();
                        while (reader.readRecord()) {
                            frase = "";
                            for (int l = 0; l < reader.getColumnCount(); l++) {
                                frase+= reader.get(l);
                                for (int j = 0; j < frase.length(); j++) {
                                    frase += " ";
                                }
                                frase += ",";
                            }
                            frase = frase.substring(0, frase.length() - 1);
                            System.out.print(frase);
                            bw.write(frase);
                            bw.newLine();
                            //reader.readRecord();
                        }
                    } catch (Exception io) {
                    }
                    bw.close();
                    escritura.close();

                } else */
                if (reader.get(posicion).equals(valorCampo)) {
                    for (int j = 0; j < columnas; j++) {
                        File prueba = new File("filesBD\\" + nombreTabla + "Seleccionada.bd");
                        FileWriter escritura = new FileWriter(prueba);
                        BufferedWriter bw = new BufferedWriter(escritura);
                        if (numarch == -1) {
                            
                            /*numarch = 1;
                            try {
                                for (int k = posicion; k < posicion; k++) {
                                    String frase = "";
                                    for (int l = 0; l < reader.getColumnCount(); l++) {
                                        frase+= reader.get(l);
                                        for (int r = 0; r < frase.length(); r++) {
                                            frase += " ";
                                        }
                                        frase += ",";
                                    }
                                    frase = frase.substring(0, frase.length() - 1);
                                    System.out.print(frase);
                                    bw.write(frase);
                                    bw.newLine();
                                    reader.readRecord();
                                    
                                }
                            } catch (Exception io) {
                            }
                            bw.close();
                            escritura.close();
                            break;*/
                            //-----------------------------------------------------
                            String frase = "";
                            for (int l = 0; l < reader.getColumnCount(); l++) {
                                frase += reader.get(l);
                                for (int r = reader.get(l).length() ; r < Integer.parseInt(logitudes[l]) - 1; r++) {
                                    frase += " ";
                                }
                                frase+= "|";
                                frase += ",";
                            }
                            frase = frase.substring(0, frase.length() - 1);
                            
                            bw.write(frase);
                            bw.newLine();
                            while (reader.readRecord()) {
                                frase = "";
                                for (int l = 0; l < reader.getColumnCount(); l++) {
                                    frase += reader.get(l);
                                    for (int r = reader.get(l).length(); r < Integer.parseInt(logitudes[l]) - 1; r++) {
                                        frase += " ";
                                    }
                                    frase+= "|";
                                    frase += ",";
                                }
                                frase = frase.substring(0, frase.length() - 1);
                                
                                //System.out.print(frase);
                                bw.write(frase);
                                bw.newLine();
                                entre = true;
                                //reader.readRecord();
                            }
                            bw.close();
                            escritura.close();
                        } else {
                            try {
                                for (int k = posicion; k < posicion + numarch; k++) {
                                    String frase = "";
                                    for (int l = 0; l < reader.getColumnCount(); l++) {
                                        frase += reader.get(l);
                                        for (int r = reader.get(l).length(); r < Integer.parseInt(logitudes[l]) -1 ; r++) {
                                            frase += " ";
                                        }
                                        frase += "|";
                                        frase += ",";
                                    }
                                    frase = frase.substring(0, frase.length() - 1);
                                    
                                  //  System.out.print(frase);
                                    bw.write(frase);
                                    bw.newLine();
                                    reader.readRecord();
                                }
                            } catch (Exception io) {
                            }
                            bw.close();
                            escritura.close();
                            
                            entre = true;
                            break;
                            
                        }
                        if (entre) {
                            break;
                        }
                    }

                    //i++;   
                }
                if (entre) {
                    break;
                }
            }
            reader.close();
            if(ordenamiento.equals("asc"))
            {
                try{
                    System.runFinalization();
                    System.gc();
                    MezclaIntercalada nuevaMescla = new MezclaIntercalada(40 ,  false , campos.length);
                    nuevaMescla.realizarMezcla("filesBD\\" + nombreTabla + "Seleccionada.bd", posicion); 
                    System.runFinalization();
                    System.gc();
                }
                catch(Exception io)
                {
                    throw new Error("Lo sentimos, algo salio mal con los archivos internos");
                }
                    
            }
            else if(ordenamiento.equals("desc"))
            {
                try{
                    System.runFinalization();
                    System.gc();
                    MezclaIntercalada nuevaMescla = new MezclaIntercalada(40 ,  true , campos.length);
                    nuevaMescla.realizarMezcla("filesBD\\" + nombreTabla + "Seleccionada.bd", posicion); 
                    System.runFinalization();
                    System.gc();
                }
                catch(Exception io)
                {
                    throw new Error("Lo sentimos, algo salio mal con los archivos internos");
                }
                
            }
            
            
            //TablaRegistrada tabla = new TablaRegistrada();
            //tabla.setTitle("SELECCION DE: " + nombreTabla + ".BD por el CAMPO: " + nombreCampo + " con el VALOR de: "+ valorCampo);

            //Creacion de la tabla
            /*JTable table = new JTable(matriz, cabecera); 
            table.setPreferredScrollableViewportSize(new Dimension(filas, columnas));*/
            //tabla.jScrollPane1.setViewportView(new JTable(matriz, cabecera));
            //tabla.setVisible(true);
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error inesperado, algo salio mal con los archivos internos");
        }
    }
}
