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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import gestor.AES;

/**
 *
 * @author estbm
 */
public class GestionBDProxy implements GestionBD {
    private final GestionBDReal nuevaBD = GestionBDReal.getIntance(); 
    
    static int caso = 0;
    
    static final int CREARTABLA = 101;
    static final int MODIFICARTABLA = 102;
    static final int ELIMINARTABLA = 103;
    
    static final int CREARREGISTRO = 201;
    static final int MODIFICARREGISTRO = 202;
    static final int ELIMINARREGISTRO = 203;
    
    static final int SELECCIONARTABLAS = 301;
    static final int JOIN = 302;
    
    
    private boolean BuscarTabla(String nombreTabla){    
        if(!new File("filesBD\\META.bd").exists())
            return false;
        String fileMETA = "filesBD\\META.bd";
        try {
            CsvReader ar = new CsvReader(fileMETA);
            while(ar.readRecord()){
                if(ar.get(0).equals(nombreTabla)){
                    ar.close();
                    return true;
                }   
            }
            ar.close();
        } 
        catch (FileNotFoundException ex) {
            throw new Error("Error, algo salio mal con los archivos internos");
        } catch (IOException ex) {
            throw new Error("Error, algo salio mal con los archivos internos");
        }
        return false;
    }
    
    @Override
    public void Peticion(Object[] args) {
        //El argumento en la posicion cero es el comando
        String comando = (String) args[0];
        //Asignacion tipo
        if(comando.replace(" ", "").contains("CREARTABLA"))
            caso = CREARTABLA;
        else if(comando.replace(" ", "").contains("MODIFICARTABLA"))
            caso = MODIFICARTABLA;
        else if(comando.replace(" ", "").contains("ELIMINARTABLA"))
            caso = ELIMINARTABLA;
        else if(comando.replace(" ", "").contains("CREARREGISTRO"))
            caso = CREARREGISTRO;
        else if(comando.replace(" ", "").contains("MODIFICARREGISTRO"))
            caso = MODIFICARREGISTRO;
        else if(comando.replace(" ", "").contains("BORRARREGISTRO"))
            caso = ELIMINARREGISTRO;
        else if(comando.replace(" ", "").contains("SELECCIONARDE"))
            caso = SELECCIONARTABLAS;
        else if(comando.replace(" ", "").contains("UNIR"))
            caso = JOIN;
        else
            throw new SecurityException("Lo sentimos, no se ha entendido esa orden.");
        
        //SENTENCIAS DEL LENGUAJE: 
        switch (caso) {
            case CREARTABLA:
            {
                int i = "CREAR TABLA ".length();
                String nombreTabla = null;
                String campoClave = null;
                //int longitudCampos = 0;
                List<String> campos = new ArrayList<>();
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                StringBuffer atributo = new StringBuffer();
                //Nombre de la tabla
                for(i=i+0;i<comando.length();i++)
                {
                    if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                    else break;
                }
                //Palabra campo
                nombreTabla = atributo.toString();
                atributo = new StringBuffer();
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                
                //Palabra reservada CAMPOS
                for(i=i+0;i<comando.length();i++)
                {
                    if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                    else break;
                }
                
                if(!atributo.toString().equals("CAMPOS"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada CAMPOS");
                for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                
                //Listado de Campos
                
                atributo = new StringBuffer();
                for(i = i+0; i<comando.length();i++) 
                    atributo.append(comando.charAt(i));
                
                if(!atributo.toString().contains("CLAVE"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada CLAVE");
                
                //Listado de campos
                int contadordeCampos = 0;
                String comando2 = atributo.toString().replace(" ", "");
                atributo = new StringBuffer();
                for(i = 0; i<comando2.indexOf("CLAVE"); i++){
                    if(comando2.charAt(i)!=','){
                        atributo.append(comando2.charAt(i));
                    }
                    else{
                        contadordeCampos++;
                        campos.add(atributo.toString());
                        atributo = new StringBuffer();
                    }
                }
                contadordeCampos++;
                System.out.println("contador = " + contadordeCampos);
                campos.add(atributo.toString());
                //Campo clave
                atributo = new StringBuffer();
                for(i = i+0; i<comando2.length();i++) 
                    atributo.append(comando2.charAt(i));
                if(!atributo.toString().contains("LONGITUD"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada LONGITUD");
                comando = atributo.toString().replace("CLAVE", ""); 
                atributo = new StringBuffer();
                for(i = 0; i<comando.indexOf("LONGITUD"); i++)
                    atributo.append(comando.charAt(i));
                
                campoClave = atributo.toString();
                comando = comando.substring(comando.indexOf("LONGITUD"));
                //Longitud de los campos
                int vectorlongitudes[] = new int[contadordeCampos];
                String longitudestex[];
                comando = comando.replace("LONGITUD", "");
                try{
                    comando2 = comando.substring(0, comando.indexOf("ENCRIPTADO"));
                }
                catch (Exception e){
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada ENCRIPTADO");
                } 
                longitudestex = comando2.split(",");
                if(longitudestex.length != contadordeCampos){
                    throw new SecurityException("Error en sintaxis, tiene que ingresar las logitudes de todos los campos");
                } 
                System.out.println(comando);
                comando = comando.replace(comando2 , "");
                comando = comando.replace("ENCRIPTADO", "");
                String CampoEncriptar[] = comando.split(",");
                System.out.println(CampoEncriptar);
                System.out.println(campos.toString());
                for (int j = 0; j < CampoEncriptar.length; j++) {
                    if(!campos.contains(CampoEncriptar[j]))
                        throw new SecurityException("Error, el campo " + CampoEncriptar[j]+" a encritar no se encuentra en los campos ingresados");
                    if(CampoEncriptar[j].equals(campoClave))
                        throw new SecurityException("Error en sintaxis, el campo clave no se puede encriptar");
                }
                
                
                try{
                for (int j = 0; j < contadordeCampos; j++) {
                   vectorlongitudes[j] =  Integer.parseInt(longitudestex[j]);
                } 
                }
                catch (Exception e){
                    throw new SecurityException("Error, las longitudes ingresadas son incorrectas");
                }
                for (int j = 0; j < contadordeCampos; j++) {
                    System.out.println(vectorlongitudes[j]);
                    if(vectorlongitudes[j]<=0)
                        throw new SecurityException("Error, la longitud ingresada no puede ser negativa o cero");
                }
                     
                //Validacion de tabla existente
                if(this.BuscarTabla(nombreTabla))
                    throw new SecurityException("Error, el nombre de la tabla que se desea crear ya existe");
                //Validacion campo clave existente
                if(campos.contains(campoClave) == false)
                    throw new SecurityException("Error, el campo clave no esta dentro de los campos especificados");
                
                /**
                * Solicitud aceptada correctamente
                **/
                System.out.println(longitudestex[0]);
                Object[] arg = {"CREARTABLA",nombreTabla,campos,campoClave,longitudestex,CampoEncriptar};
                nuevaBD.Peticion(arg);
                
                break;
            }
            case MODIFICARTABLA://----------------------------------------------
                {
                    int i = "MODIFICAR TABLA ".length();
                    
                    String nombreTabla = null;
                    String nombreCampo = null;
                    String nuevoCampo = null;
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    
                    
                    //Palabra reservada Campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CAMPO"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CAMPO");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreCampo = atributo.toString();
                    if(nombreCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre del campo a modificar");
                    
                    //Palabra reservada POR
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("POR"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada POR");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Nuevo valor del campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nuevoCampo = atributo.toString();
                    if(nuevoCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nuevo valor del campo");
                    
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                    
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    //Validacion campo existente
                    ArrayList<String> campos = new ArrayList<>();
                    String fileMETA = "filesBD\\META.bd";
                    boolean boolClave = false;
                    
                    int vectorLogitudes2[] = null;
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla)){
                                int contador = 0;
                                for (int j = 3; j < ar.getColumnCount(); j++) {
                                    if(!ar.get(j).equals("ENCRIP"))
                                    {
                                       contador++;
                                    }
                                    else
                                    {
                                        break;
                                    }      
                                }
                                int numcapos = contador/2;//ar.getColumnCount() - ((ar.getColumnCount() + 4)/2);
                                vectorLogitudes2 = new int[numcapos];
                                for (int j = 3; j < 3 + numcapos ; j++){ //(ar.getColumnCount() + 4)/2; j++) {
                                    //System.out.println("-----" + Integer.parseInt(ar.get(j)));
                                    vectorLogitudes2[j - 3] = Integer.parseInt(ar.get(j));
                                }
                                for (int j = 3 + numcapos ; j < 3 + contador ; j++){ //(ar.getColumnCount() + 4)/2; j < ar.getColumnCount();j++){
                                    campos.add(ar.get(j));
                                }
                                if(ar.get(2).equals(nombreCampo))
                                    boolClave = true;
                            }
                                                   
                        }
                        ar.close();
                    }
                    catch (FileNotFoundException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    } catch (IOException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    }
                    
                    if(!campos.contains(nombreCampo))
                        throw new SecurityException("Error, el nombre del campo especificado no existe");
                    
                    if(boolClave==true)
                        throw new SecurityException("Error, no se puede modificar el campo clave");
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"MODIFICARTABLA",nombreTabla,nombreCampo,nuevoCampo,vectorLogitudes2,campos};
                    nuevaBD.Peticion(arg);
                    
                    break;
                }
            case ELIMINARTABLA: //----------------------------------------------
                {
                    String nombreTabla = null;
                    
                    int i = "ELIMINAR TABLA ".length();
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"ELIMINARTABLA",nombreTabla};
                    nuevaBD.Peticion(arg);
                    
                    break;
                }
            case CREARREGISTRO: //----------------------------------------------
                {
                    int i = "CREAR REGISTRO ".length();
                    
                    String nombreTabla = null;
                    List<String> valoresCampos = new ArrayList<>();
                    
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada CLAVE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("VALOR"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada VALOR");
                    
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)
                        if(comando.charAt(i)!=' ')
                            atributo.append(comando.charAt(i));
                            
                    comando = atributo.toString();
                    //Valores de campos
                    
                    atributo = new StringBuffer();
                    for(i = 0; i<comando.length(); i++){
                        if(comando.charAt(i)!=',')
                            atributo.append(comando.charAt(i));
                        else
                        {
                            valoresCampos.add(atributo.toString());
                            atributo = new StringBuffer();
                        }
                    }
                    valoresCampos.add(atributo.toString());
                    
                    //Validacion valores null
                    
                    if(valoresCampos.contains(""))
                        throw new SecurityException("Error, uno de los valores de campos es nulo");
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    
                    //Validaciones longitud y numero de campos
                    int nroCampos = - 1;
                    int longitud = -1;
                    String campoClave = null;
                    String camposVector[] = null;
                    String fileMETA = "filesBD\\META.bd";
                    int vectorLogitudes2[] = null;
                    String vectorEncriptar[] = null;
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla))
                            {
                                int contador = 0;
                                for (int j = 3; j < ar.getColumnCount(); j++) {
                                    if(!ar.get(j).equals("ENCRIP"))
                                    {
                                       contador++;
                                    }
                                    else
                                    {
                                        break;
                                    }      
                                }
                                
                                int numcapos = contador/2;//ar.getColumnCount() - ((ar.getColumnCount() + 4)/2);
                                vectorLogitudes2 = new int[numcapos];
                                vectorEncriptar = new String[ar.getColumnCount() - (3 + contador)];
                                camposVector = new String[numcapos];
                                for (int j = 3 + numcapos; j < 3 + contador; j++) {
                                    camposVector[j - (3 + numcapos)] = ar.get(j);
                                    System.out.print(ar.get(j) + "---");
                                }
                                System.out.println("");
                                for (int j = 3; j < 3 + numcapos; j++) {
                                    System.out.println("-----" + Integer.parseInt(ar.get(j)));
                                    vectorLogitudes2[j - 3] = Integer.parseInt(ar.get(j));
                                }
                                for(int j = 3 + contador ; j < ar.getColumnCount();j++)
                                {
                                    vectorEncriptar[j - (3 + contador)] = ar.get(j);
                                }
                                nroCampos = numcapos;
                                campoClave = ar.get(2);
                            }    
                        }
                        ar.close();
                    }
                   
                    catch(IOException | NumberFormatException e){throw new Error("Error, algo salio mal con los archivos internos");}
                    int cont1 = 0;
                    for(String valor : camposVector)
                    {
                        for (int j = 0; j < vectorEncriptar.length; j++) {
                            if(valor.equals(vectorEncriptar[j]))
                            {    
                                 String valoraux = valoresCampos.get(cont1);
                                 valoresCampos.remove(cont1);
                                 valoresCampos.add(cont1, AES.Encriptar(valoraux));
                                 if(vectorLogitudes2[cont1] < valoresCampos.get(cont1).length())
                                    throw new Error("Error, una longitud de los campos encritados sobrepasa el tamaño del campo");
                            }
                        }
                        cont1++;
                        
                    }    
                   // String pos[]
                    if(nroCampos ==-1 || campoClave == null)
                        throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                    
                    if(valoresCampos.size()!=nroCampos)
                        throw new SecurityException("Error, el numero total de valores ingresados no coincide con el numero de campos registrados");
                    
                    for(int k = 0; k<valoresCampos.size();k++){
                        if(valoresCampos.get(k).length()>vectorLogitudes2[k])
                            throw new SecurityException("Error, en uno de los campos excede la longitud maxima posible");
                    }
                    
                    //Validacion de que el campo clave no se repita
                    try {
                        
                        CsvReader ar = new CsvReader("filesBD\\"+nombreTabla+".BD");
                        /*int posicion = -1;
                        ar.readRecord();
                        for(int k = 0; k<ar.getColumnCount(); k++){
                            System.out.println("val: " + ar.get(k));
                            if(ar.get(k).equals(campoClave)){
                                posicion = k;
                            }
                        }*/
                        int posicion = -1;
                        int cont = 0;
                        for(String valor : camposVector)
                        { 
                            System.out.println(valor);
                            if(valor.equals(campoClave))
                            {
                                posicion = cont;
                                System.out.println(posicion);
                                break;
                                
                            }
                            cont++;      
                        }
                            
                        
                        /*if(posicion == -1)
                            throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                        */
                        while(ar.readRecord()){
                            if(ar.get(posicion).equals(valoresCampos.get(posicion)))
                                throw new SecurityException("Error, el valor correspondiente al campo clave ya existe");
                        }
                        ar.close();
                        
                    }catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");
                    }catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}
                   
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"CREARREGISTRO",nombreTabla,valoresCampos,vectorLogitudes2};
                    nuevaBD.Peticion(arg);
                    
                    break;
                }
            case MODIFICARREGISTRO: //------------------------------------------
                {
                    int i = "MODIFICAR REGISTRO ".length();
                    
                    String nombreTabla = null;
                    String valorCampoClave = null;
                    String nombreCampo = null;
                    String valorCampo = null;
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada CLAVE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CLAVE"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CLAVE");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    
                    //Campo clave 
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    valorCampoClave = atributo.toString();
                    if(valorCampoClave.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el campo clave");
                    
                    //Palabra reservada CAMPO
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CAMPO"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CAMPO");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreCampo = atributo.toString();
                    if(nombreCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre del campo a modificar");
                    
                    //Palabra reservada POR
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("POR"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada POR");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                    //Nuevo valor del campo
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    valorCampo = atributo.toString();
                    if(valorCampo.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nuevo valor del campo");
                    
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                    
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    //Validacion tabla con registros
                    try {

                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        int cont = 0;
                        while(read.readRecord()) cont++;
                        if(cont<=1) throw new SecurityException("Error, la tabla especificada no cuenta con registros");
                        read.close();
                    } 
                    catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");}
                    catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}
                    
                    //Validaciones de existencia y longitud
                    String campoClaveReal = null;
                    String fileMETA = "filesBD\\META.bd";
                    int posicionCampo = -1;
                    int vectorLogitudes2[] = null;
                    String vetorCampos[] = null;
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            //Existencia del campo
                            if(ar.get(0).equals(nombreTabla))
                            {   
                                int contador = 0;
                                for (int j = 3; j < ar.getColumnCount(); j++) {
                                    if(!ar.get(j).equals("ENCRIP"))
                                    {
                                      // System.out.println(ar.get(j));
                                       contador++;
                                    }
                                    else
                                    {
                                        break;
                                    }      
                                }
                                int numcapos = contador/2;
                                //int numcapos = ar.getColumnCount() - ((ar.getColumnCount() + 4)/2);
                                vectorLogitudes2 = new int[numcapos];
                                vetorCampos = new String[numcapos];
                                
                                for (int j = 3; j < 3 + numcapos; j++) {
                                    //System.out.println("-----" + Integer.parseInt(ar.get(j)));
                                    vectorLogitudes2[j - 3] = Integer.parseInt(ar.get(j));
                                }
                                for (int j = 3 + numcapos; j < 3 + contador ; j++) {
                                    vetorCampos[j - (3 + numcapos)] = ar.get(j);
                                }
                                boolean estadoEncontrado = false;
                                boolean estadoEncontradoClave = false;
                                campoClaveReal = ar.get(2);
                                for(int w = 3+numcapos;w< 3 + contador;w++){
                                    if(ar.get(w).equals(nombreCampo)){
                                        estadoEncontrado = true;
                                    }
                                    if(ar.get(w).equals(campoClaveReal)){
                                        estadoEncontradoClave = true;
                                        posicionCampo = w;
                                    }
                                }
                                posicionCampo = posicionCampo - contador;//(ar.getColumnCount() + 4)/2;
                                
                                //Errores varios
                                if(estadoEncontrado == false)
                                    throw new SecurityException("Error, el nombre del campo especificado no existe");
                                if(estadoEncontradoClave == false)
                                    throw new SecurityException("Error, el nombre del campo clave especificado no existe");
                                if(ar.get(2).equals(nombreCampo))
                                    throw new SecurityException("Lo sentimos, no es posible modificar el valor del campo clave");
                                if(valorCampo.length()> vectorLogitudes2[posicionCampo])
                                    throw new SecurityException("Error, el nuevo valor del campo excede el maximo de longitud establecido");
                                
                                break;
                            }    
                        }
                        ar.close();
                        
                        
                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        boolean estadoEncontrado = false;
                        //read.readRecord();
                        while(read.readRecord()){
                            //System.out.println("");
                            if(read.get(posicionCampo).equals(valorCampoClave)){
                                estadoEncontrado = true;
                                break;
                            }
                        }
                        read.close();
                        read.close();
                        if(estadoEncontrado==false)
                            throw new SecurityException("Error, no existe un registro con ese valor de campo clave");
                    }
                    catch(IOException | NumberFormatException e){throw new Error("Error, algo salio mal con los archivos internos");}
                    
                    if(campoClaveReal == null)
                        throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"MODIFICARREGISTRO",nombreTabla,valorCampoClave,nombreCampo,valorCampo,posicionCampo,vectorLogitudes2,vetorCampos};
                    nuevaBD.Peticion(arg);
                    
                    break;
                }
            case ELIMINARREGISTRO: //-------------------------------------------
                {
                    String nombreTabla = null;
                    String campoClave = null;
                    
                    int i = "BORRAR REGISTRO ".length();
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada CLAVE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("CLAVE"))
                        throw new SecurityException("Error en sintaxis, falta la palabra reservada CLAVE");
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    
                    //Campo clave 
                    atributo = new StringBuffer();
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    campoClave = atributo.toString();
                    if(campoClave.isEmpty())
                        throw new SecurityException("Error, no se ha especificado el campo clave");
                    
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ')
                            throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                    }
                            
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla))
                        throw new SecurityException("Error, el nombre de la tabla especificada no existe");
     
                    //Validacion tabla con registros
                    try {

                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        int cont = 0;
                        while(read.readRecord()) cont++;
                        if(cont<=0) 
                            throw new SecurityException("Error, la tabla especificada no cuenta con registros");
                        read.close();
                        read.close();
                    } 
                    catch (FileNotFoundException ex) {throw new Error("Error, algo salio mal con los archivos internos");}
                    catch (IOException ex) {throw new Error("Error inesperado, algo salio mal");}
                    
                    //Validacion de valor de campo clave existente
                    String fileMETA = "filesBD\\META.bd";
                    int posicion = -1;
                    int contador = 0;
                    try {
                        String campoClaveReal = null;
                        
                        CsvReader ar = new CsvReader(fileMETA);
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla)){
                                campoClaveReal = ar.get(2);
                                
                                for (int j = 3; j < ar.getColumnCount(); j++) {
                                    if(!ar.get(j).equals("ENCRIP"))
                                    {
                                        //System.out.println(ar.get(j));
                                       contador++;
                                    }
                                    else
                                    {
                                        break;
                                    } 
                                }
                                int numcapos = contador/2;
                                for(int k = 3 + numcapos; k< 3 + contador; k++){
                                    if(ar.get(k).equals(campoClaveReal)){
                                        posicion = k;
                                        break;
                                    }
                                }
                                break;
                            }    
                        }
                        posicion = posicion - (3 + contador/2);
                        ar.close();
                        ar.close();
                        if(campoClaveReal == null || posicion == -1)
                            throw new SecurityException("Error interno, no se han encontrado los datos de la tabla especificada");
                        
                        CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        boolean estadoEncontrado = false;
                        System.out.println("hola mundo");
                        //read.readRecord();
                        while(read.readRecord()){
                            if(read.get(posicion).equals(campoClave)){
                                estadoEncontrado = true;
                                break;
                            }
                        }
                        read.close();
                        read.close();
                        if(estadoEncontrado==false)
                            throw new SecurityException("Error, no existe un registro con ese valor de campo clave");
                    } 
                    catch (FileNotFoundException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    } catch (IOException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    }
                    
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"ELIMINARREGISTRO",nombreTabla, campoClave, posicion};
                    nuevaBD.Peticion(arg);
                    
                    break;
                }
            case SELECCIONARTABLAS://-------------------------------------------
                {
                    int i = "SELECCIONAR DE ".length();
                    
                    String nombreTabla = null;
                    String nombreCampo = null;
                    String valorCampo = null;
                    
                    for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;
                    
                    StringBuffer atributo = new StringBuffer();
                
                    //Nombre de la tabla
                    for(i=i+0;i<comando.length();i++)
                    {
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    nombreTabla = atributo.toString();
                    
                    if(nombreTabla.isEmpty()) throw new SecurityException("Error, no se ha especificado el nombre de la tabla");
                    
                    atributo = new StringBuffer();
                    
                    //Palabra reservada DONDE
                    
                    for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                    for(i=i+0;i<comando.length();i++){
                        if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                        else break;
                    }
                    
                    if(!atributo.toString().equals("DONDE")) throw new SecurityException("Error en sintaxis, falta la palabra reservada DONDE");
                    
                    atributo = new StringBuffer();
                    for(i = i +0; i<comando.length(); i++) atributo.append(comando.charAt(i));
                    
                    if(!atributo.toString().contains("=")) throw new SecurityException("Error en sintaxis, falta el operador =");
                    
                    comando = atributo.toString().replace(" ", "");
                    //Palabra del campo de atributo campo
                    atributo = new StringBuffer();
                    for(i = 0; i<comando.indexOf("=");i++) atributo.append(comando.charAt(i));
                    
                    nombreCampo = atributo.toString();
                    
                    if(!comando.contains("\"")) throw new SecurityException("Error en sintaxis, faltan las comillas \" para especificar el valor de busqueda");
                    if(comando.charAt(i+1)!='\"') throw new SecurityException("Error en sintaxis, faltan las comillas \" para especificar el valor de busqueda");
                    
                    
                    //Valor
                    atributo = new StringBuffer();
                    for(i = i +2; i<comando.length(); i++)atributo.append(comando.charAt(i));
                    comando = atributo.toString();
                    if(!comando.toString().contains("\"")) throw new SecurityException("Error en sintaxis, faltan las comillas \" para especificar el valor de busqueda");
                    
                    atributo = new StringBuffer();
                    for(i = 0; i<comando.indexOf("\""); i++) atributo.append(comando.charAt(i));
                    
                    valorCampo = atributo.toString();
                    if(nombreCampo.isEmpty()) throw new SecurityException("Error, no se ha especificado el nombre del campo");
                    
                    if(valorCampo.isEmpty()) throw new SecurityException("Error, no se ha especificado el valor del campo");
                    
                    /*
                    for(i = i+1; i<comando.length(); i++) 
                        if(comando.charAt(i)!=' ') throw new SecurityException("Error, existen caracteres de mas en la sentencia");*/
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla)) throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    int cont2 = 0;
                    for(i = 0; i<comando.indexOf("\""); i++) {
                        atributo.append(comando.charAt(i));
                        cont2++;
                    }
                    
                    String tipoOrdenamiento = "";
                    int numArch = -1;
                    //valorCampo = atributo.toString();
                    if(comando.contains("ORDENADO"))
                    {
                        if (comando.contains("VER")) 
                        {
                             comando = comando.replace("ORDENADO", "");
                              
                             tipoOrdenamiento = comando.substring(comando.indexOf("\"") + 1 , comando.indexOf("VER"));
                            if(!tipoOrdenamiento.equals("asc") && !tipoOrdenamiento.equals("desc"))
                            {
                                throw new SecurityException("Error, escribio mal la orden de ordenamiento.");
                            }
                            comando = comando.substring(comando.indexOf("VER") , comando.length());
                            comando = comando.replace("VER", "");
                            //comando = comando.substring(1, comando.length());
                            try
                            {
                                Integer.parseInt(comando);
                            }
                            catch(Exception io)
                            {
                                throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                            }
                            if (Integer.parseInt(comando) < 0) {
                                throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                            }
                            numArch = Integer.parseInt(comando);
                            
                        }
                        else
                        {
                            comando = comando.replace("ORDENADO", ""); 
                            tipoOrdenamiento = comando.substring(comando.indexOf("\"") + 1 , comando.length());
                            if(!tipoOrdenamiento.equals("asc") && !tipoOrdenamiento.equals("desc"))
                            {
                                throw new SecurityException("Error, escribio mal la orden de ordenamiento.");
                            }
                        }
                        
                    }
                    else if (comando.contains("VER"))
                    {
                        comando = comando.substring(comando.indexOf("VER") , comando.length());
                        comando = comando.replace("VER", "");
                        try
                        {
                            Integer.parseInt(comando);
                        }
                        catch(Exception io)
                        {
                            throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                        }
                        if (Integer.parseInt(comando) < 0) {
                            throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                        }
                        numArch = Integer.parseInt(comando);
                    }
                    //if(nombreCampo.isEmpty()) throw new SecurityException("Error, no se ha especificado el nombre del campo");
                    
                    //if(valorCampo.isEmpty()) throw new SecurityException("Error, no se ha especificado el valor del campo");
                    
                    
                   /* for(i = i+1; i<comando.length(); i++) 
                        if(comando.charAt(i)!=' ') throw new SecurityException("Error, existen caracteres de mas en la sentencia");*/
                    System.out.println();
                    System.out.println(comando);
//comando = comando.replace( "\"" , "");
                    
                    
                    //Validacion de tabla existente
                    if(!this.BuscarTabla(nombreTabla)) throw new SecurityException("Error, el nombre de la tabla especificada no existe");
                    
                    //Validacion de que el campo especificado exista...
                    
                    int posicion = -1;
                    int contador = 0;
                    String fileMETA = "filesBD\\META.bd";
                        
                    try {
                        CsvReader ar = new CsvReader(fileMETA);
                        
                        while(ar.readRecord()){
                            if(ar.get(0).equals(nombreTabla)){
                                for (int j = 3; j < ar.getColumnCount(); j++) {
                                    if(!ar.get(j).equals("ENCRIP"))
                                    {
                                        System.out.println(ar.get(j));
                                       contador++;
                                    }
                                    else
                                    {
                                        break;
                                    } 
                                }
                                int numcampos = contador/2;
                                for(int k = 3 + numcampos ; k < 3 + contador; k++){
                                    if(ar.get(k).equals(nombreCampo)){
                                        posicion = k;
                                        break;
                                    }
                                }
                                break;
                            }    
                        }
                        if(posicion!=-1)
                            posicion = posicion - (3 + contador/2);
                        
                        ar.close();
                        
                    } catch (FileNotFoundException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    } catch (IOException ex) {
                        throw new Error("Error, algo salio mal con los archivos internos");
                    }
                    if(posicion == -1) throw new SecurityException("Error interno, la tabla " + nombreTabla + " no posee ese campo");
                    try{
                    CsvReader read = new CsvReader("filesBD\\"+nombreTabla+".bd");
                        boolean estadoEncontrado = false;
                        while(read.readRecord()){
                            System.out.println(read.get(posicion));
                            if(read.get(posicion).equals(valorCampo)){
                                estadoEncontrado = true;
                                break;
                            }
                        }
                        read.close();
                        read.close();
                        if(estadoEncontrado==false)
                            throw new SecurityException("Error, no existe un registro con ese valor");
                    }
                    catch(IOException | NumberFormatException e){throw new Error("Error, algo salio mal con los archivos internos");}
                    /**
                    * Solicitud aceptada correctamente
                    **/
                    
                    Object[] arg = {"SELECCIONAR",nombreTabla, nombreCampo, valorCampo, posicion, tipoOrdenamiento , numArch};
                    nuevaBD.Peticion(arg);
                    
                    break;
                }
            case JOIN://--------------------------------------------------------
            {
                int i = "UNIR ".length();

                String nombreTabla1 = null;
                String nombreTabla2 = null;
                String nombreCampo = null;

                for (i=i+0;i<comando.length();i++) if(comando.charAt(i)!=' ')break;

                //Listado de Campos

                StringBuffer atributo = new StringBuffer();
                for(i = i+0; i<comando.length();i++) 
                    atributo.append(comando.charAt(i));

                //Listado de campos
                String comando2 = atributo.toString().replace(" ", "");
                atributo = new StringBuffer();
                
                if(!comando.contains("POR"))
                    throw new SecurityException("Error en sintaxis, falta la palabra reservada POR");

                if(!comando.contains(","))
                    throw new SecurityException("Error en sintaxis, falta especificar el nombre de las tablas");
                
                for(i = 0; i<comando2.indexOf("POR"); i++){
                    if(comando2.charAt(i)!=',')
                        atributo.append(comando2.charAt(i));
                    else
                    {
                        nombreTabla1 = atributo.toString();
                        atributo = new StringBuffer();
                        comando = comando.replace(nombreTabla1, "");
                    }
                }
                nombreTabla2 = atributo.toString();
                comando = comando.replace(nombreTabla2, "");
                if(nombreTabla1.isEmpty())
                    throw new SecurityException("Error, no se ha especificado el nombre de la primera tabla");

                if(nombreTabla2.isEmpty())
                    throw new SecurityException("Error, no se ha especificado el nombre de la segunda tabla");
                
                //Palabra reservada POR
                atributo = new StringBuffer();
                i = comando.indexOf("POR")+3;
                
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;

                //Nuevo valor del campo
                atributo = new StringBuffer();
                for (i=i+0;i<comando.length();i++)if(comando.charAt(i)!=' ')break;
                for(i=i+0;i<comando.length();i++){
                    if(comando.charAt(i)!=' ') atributo.append(comando.charAt(i));
                    else break;
                }

                nombreCampo = atributo.toString();
                if(nombreCampo.isEmpty())
                    throw new SecurityException("Error, no se ha especificado el factor de union");

           /*
                for(i=i+0;i<comando.length();i++){
                    if(comando.charAt(i)!=' ')
                        throw new SecurityException("Error, existen caracteres de mas en la sentencia");
                }*/
                
                
                //Validacion de tabla1 existente
                if(!this.BuscarTabla(nombreTabla1))
                    throw new SecurityException("Error, la tabla "+ nombreTabla1 +" no existe");

                if(!this.BuscarTabla(nombreTabla2))
                    throw new SecurityException("Error, la tabla "+ nombreTabla2 +" no existe");
                System.out.println(comando);
                
                //Validacion campo existentes en las tablas
                boolean estadoTabla1 = false;
                boolean estadoTabla2 = false;
                String fileMETA = "filesBD\\META.bd";
                CsvReader read;
                CsvReader read2;
                try {

                    read = new CsvReader(fileMETA);
                    read.readRecord();
                    for(i = 4; i<read.getColumnCount();i++){
                        if(!read.getRawRecord().isEmpty())
                            if(read.get(i).equals(nombreCampo)){
                                estadoTabla1 = true;
                                break;
                            }
                    }
                    read.close();
                    read2 = new CsvReader(fileMETA);
                    read2.readRecord();
                    for(i = 4; i<read2.getColumnCount();i++){
                        if(!read2.getRawRecord().isEmpty())
                            if(read2.get(i).equals(nombreCampo)){
                                estadoTabla2 = true;
                                break;
                            }
                    }
                    read2.close();
                }
                catch (FileNotFoundException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                } catch (IOException ex) {
                    throw new Error("Error, algo salio mal con los archivos internos");
                }
                read2.close();
                read.close();
                
                if(estadoTabla1==false)
                    throw new SecurityException("Error, la tabla " + nombreTabla1 + " no posee el campo " + nombreCampo);
                
                if(estadoTabla2==false)
                    throw new SecurityException("Error, la tabla " + nombreTabla2 + " no posee el campo " + nombreCampo);
                
                System.out.println(comando);

                /**
                * Solicitud aceptada correctamente
                **/
                String tipoOrdenamiento = "";
                int numArch = -1;
                //boolean entro = true;
                if (comando.contains("ORDENADO")) {
                    comando = comando.substring(comando.indexOf("ORDENADO"),  comando.length());
                    if (comando.contains("VER")) {
                        comando = (comando.replace("ORDENADO", "")).trim();

                        tipoOrdenamiento = comando.substring(0, comando.indexOf("VER")).trim();
                        if (!tipoOrdenamiento.equals("asc") && !tipoOrdenamiento.equals("desc")) {
                            throw new SecurityException("Error, escribio mal la orden de ordenamiento.");
                        }
                        comando = comando.substring(comando.indexOf("VER"), comando.length());
                        comando = (comando.replace("VER", "")).trim();
                        //comando = comando.substring(1, comando.length());
                        try {
                            Integer.parseInt(comando);
                        } catch (Exception io) {
                            throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                        }
                        if (Integer.parseInt(comando) < 0) {
                            throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                        }
                        numArch = Integer.parseInt(comando);

                    } else {
                        comando = (comando.replace("ORDENADO", "")).trim();
                        tipoOrdenamiento = comando.substring(0, comando.length());
                        if (!tipoOrdenamiento.equals("asc") && !tipoOrdenamiento.equals("desc")) {
                            throw new SecurityException("Error, escribio mal la orden de ordenamiento.");
                        }
                    }

                } else if (comando.contains("VER")) {
                    comando = comando.substring(comando.indexOf("VER"), comando.length());
                    comando = (comando.replace("VER", "")).trim();
                    try {
                        Integer.parseInt(comando);
                    } catch (Exception io) {
                        throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                    }
                    if (Integer.parseInt(comando) < 0) {
                        throw new SecurityException("Error, los valores del campo VER tienen que ser enteros positivos.");
                    }
                    numArch = Integer.parseInt(comando);
                }
                else
                {
                    for(i=i+0;i<comando.length();i++){
                    if(comando.charAt(i)!=' ')
                        throw new SecurityException("Error, revise la sintaxis");
                    }
                }
                Object[] arg = {"UNIR",nombreTabla1, nombreTabla2, nombreCampo , tipoOrdenamiento , numArch};
                nuevaBD.Peticion(arg);
                
                break;
            }
            default:
                throw new SecurityException("Lo sentimos, no se ha entendido esa orden.");
        }
        
        //Solicitud aprobada - Ubicar las invocaciones en cada uno de los casos
        //new GestionBDReal().Peticion(arg);
    }    
}

