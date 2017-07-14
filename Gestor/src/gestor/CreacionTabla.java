/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author estbm
 */
public class CreacionTabla extends CreacionTemplate{
     private String nombreTabla;
    private ArrayList<String> campos;
    private String campoClave;
    private String longitudCampos[];
    private String campoEncriptar[];
    private String longitudes;
    

    @Override
    protected void IdentificarParametros(Object[] args) {
        this.nombreTabla = (String) args[0];
        this.campos = (ArrayList<String>) (List) args[1];
        this.campoClave = (String) args[2];
        this.longitudCampos = (String[]) args[3];
        this.campoEncriptar = (String[]) args[4];
        
    }
    
    @Override
    protected void RealizarOperacion() {
        
        new File("filesBD\\").mkdirs();//Crea el directorio en caso de que no exista
        /*String camposAgregar = "";
        for (int i = 0; i < longitudCampos.length; i++) {
            System.out.println(campos.get(i).length());
            System.out.println(Integer.parseInt(longitudCampos[i]));
            if(campos.get(i).length() < Integer.parseInt(longitudCampos[i]))
            {
                camposAgregar+= campos.get(i);
                for (int j = campos.get(i).length(); j < Integer.parseInt(longitudCampos[i]); j++) {
                    camposAgregar+=" ";
                }
                camposAgregar += ",";
                
            }
        }
        camposAgregar = camposAgregar.substring(0, camposAgregar.length()-1);*/
        try{
            PrintWriter flujoSalida = new PrintWriter("filesBD" + "\\" + nombreTabla + ".bd"); 
            //flujoSalida.println(camposAgregar);
            flujoSalida.close();
            
        }
        catch(Exception e ){
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");
        } 
        //en caso de que el usuario quiera ponerle la cabesera en la tabla
    }

    @Override
    protected void ActualizarMETA() {
        try {
            File file = new File(this.fileMETA);
            longitudes = "";
            for (int i = 0; i < longitudCampos.length; i++) {
                longitudes += longitudCampos[i] + ",";
            }
            longitudes = longitudes.substring(0, longitudes.length()-1);
            String camposencriptar = "";
            for (int i = 0 ; i < campoEncriptar.length ; i++)
            {
                camposencriptar += campoEncriptar[i] + ",";
            }
            camposencriptar = camposencriptar.substring(0 , camposencriptar.length()-1);
            if(file.exists()){
                Scanner scan =  new Scanner(new File(this.fileMETA)).useDelimiter("\\A");
                String contenido = scan.next();
                scan.close();
                file.delete();
                PrintWriter wr = new PrintWriter(this.fileMETA);
                System.out.println("Contenido:-" + contenido);
                if(!contenido.equals("\n"))
                    wr.print(contenido + "\n");
                //wr.print(contenido + "\n");
                wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave + "," + 
                longitudes + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", "") + "," + "ENCRIP" + "," + camposencriptar);
                wr.close();
            }
            else{
                PrintWriter wr = new PrintWriter(this.fileMETA);
                wr.println(this.nombreTabla + "," + "0" + "," + this.campoClave + "," + 
                longitudes + "," + this.campos.toString().replace("[", "").replace("]", "").replace(" ", "") + "," + "ENCRIP" + "," + camposencriptar);
                wr.close();
            }
        } catch (FileNotFoundException ex) {
            throw new Error("Lo sentimos, algo salio mal con los archivos internos");}
    }
}
