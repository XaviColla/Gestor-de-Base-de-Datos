package gestor;


/**
 *
 * @author Christian Collaguazo
 */
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MezclaIntercalada{
    private  int N;//Total de archivos
    private  int Nm;//Mitad de archivos
    private File []arcAux;
    private String []nomArch;
    private FileWriter []archvosW;
    private String directorio;
    private String direccion;
    private boolean orden = true;
    private int numcam = 0;
    public MezclaIntercalada(int N,boolean orden , int numcam) {
        this.N = N;
        this.numcam = numcam;
        this.nomArch = new String[this.N];
        arcAux = new File[N];
        this.Nm = N/2;
        archvosW = new FileWriter[this.Nm];
        if (!orden){
        this.orden = false;        
        }
        
    }
    public void realizarMezcla(String archivo_leer, int campo) throws ParseException, IOException{  
        File arc = new File(archivo_leer);
        directorio = arc.getParent();
        direccion = archivo_leer;
        CrearArchivos(N);
        String anterior;
        int []aux1 = new int[N];
        int []aux2 = new int[N];
        Object [] flujos = new Object[N];
        CsvReader archivoentrada = null;
        CsvWriter archivosalida = null;
        String []auxNombres = new String[Nm];
        int  aux3;
        int numTramos;
        
        boolean [] validaciones = new boolean[Nm];
            numTramos = distribuir(archivo_leer,campo);
            for(int i = 0; i < N; i++)
                aux1[i] = i;
            do{
                aux3 = (numTramos < Nm) ? numTramos : Nm;
                for(int i = 0; i < aux3; i++){
                    flujos[aux1[i]] = new CsvReader(nomArch[aux1[i]]);
                    aux2[i] = aux1[i];
                }
                int j = Nm; 
                numTramos = 0;
                for(int i = j; i < N;i++)
                    flujos[aux1[i]] = new CsvWriter(new FileWriter(nomArch[aux1[i]], true), ',');
                for( int n = 0; n < aux3; n++){
                    archivoentrada = (CsvReader)flujos[aux2[n]];
                    archivoentrada.readRecord();
                    auxNombres[n] = archivoentrada.get(campo);
                }
                while(aux3 > 0){
                    numTramos++; 
                    for(int i = 0; i < aux3; i++){
                        validaciones[i] = true;
                    }
                    archivosalida = (CsvWriter)flujos[aux1[j]];
                    while (!finDeTramos(validaciones,aux3)){
                        int n;
                        n = minimo(auxNombres, validaciones, aux3,campo);
                        archivoentrada = (CsvReader)flujos[aux2[n]];
                        /*archivosalida.write(archivoentrada.get(0));
                        archivosalida.write(archivoentrada.get(1));
                        archivosalida.write(archivoentrada.get(2));
                        archivosalida.write(archivoentrada.get(3));*/
                        for (int i = 0; i < numcam; i++) {
                            archivosalida.write(archivoentrada.get(i));
                        }
                        archivosalida.endRecord();
                        anterior = auxNombres[n];
                        archivoentrada.readRecord();
                        if(archivoentrada.get(2) != "") {
                            auxNombres[n] = archivoentrada.get(campo);
                            if (CompararCampos(anterior, auxNombres[n], campo) == false) 
                                validaciones[n] = false;
                        }
                        else{
                            aux3--; 
                            archivoentrada.close(); 
                            aux2[n] = aux2[aux3]; 
                            auxNombres[n] = auxNombres[aux3];
                            validaciones[n] = validaciones[aux3]; 
                            validaciones[aux3] = false;
                        }
                    }
                    j = (j < N-1) ? j+1 : Nm; 
                }
                for(int i = Nm; i < N; i++){
                    archivosalida = (CsvWriter)flujos[aux1[i]];
                    archivosalida.close();
                }
                    
                    //Cambio entrada<->salida    
                
                for (int i = 0; i < Nm; i++){
                    int a; 
                    a = aux1[i];
                    aux1[i] = aux1[i+Nm]; 
                    aux1[i+Nm] = a; 
                    File archBor = new File(nomArch[aux1[i+Nm]]);
                    archBor.delete();
                }
            }while (numTramos > 1);
            File f1 = new File(nomArch[aux1[0]]);
            File f2 = new File(archivo_leer);
            f2.delete();
            f2 = new File(archivo_leer);
            f1.renameTo(f2);
            for(int i = 0; i < N; i++){
                File archBor = new File(nomArch[i]);
                archBor.delete();
            }
             
        
    }
        
     public  int distribuir(String archivo_leer,int campo) throws IOException, ParseException{    // ->> aqui 
        int numArchEs, numerodeTramos;
        this.orden =  orden; 
        String anterior = null;
        String clave; 
        CsvReader archivoOriginal = new CsvReader(archivo_leer);
        CsvWriter []archivodeEntrada= new CsvWriter[Nm];
        /* switch(campo + 1)
        {
            case 1: 
                anterior = "-9999999";
                break;
            case 2: 
                anterior = "!!!!!!!!!";
                break;
            case 3:
                anterior = "false";
                break;
            case 4:
                anterior = "11/11/1111";
                break;
        }*/
        if(this.orden == true){        
            anterior = "■■■■■";
        }else{
            anterior = "!!!!!!!!!";
        }
        //anterior = "■■■■■";
        //anterior = "!!!!!!!!!";
        for (numArchEs = 0; numArchEs < Nm; numArchEs++) {
            archivodeEntrada[numArchEs] = new CsvWriter(new FileWriter(arcAux[numArchEs], true), ',');          
        }
        numArchEs = 0;   
        numerodeTramos = 0; 
        while (archivoOriginal.readRecord()) {
           
            clave =  archivoOriginal.get(campo);
            while(CompararCampos(anterior, clave, campo) == true) {
                /*archivodeEntrada[numArchEs].write(archivoOriginal.get(0));
                archivodeEntrada[numArchEs].write(archivoOriginal.get(1));
                archivodeEntrada[numArchEs].write(archivoOriginal.get(2));
                archivodeEntrada[numArchEs].write(archivoOriginal.get(3));*/
                for (int i = 0; i < numcam; i++) {
                     archivodeEntrada[numArchEs].write(archivoOriginal.get(i));
                }
                archivodeEntrada[numArchEs].endRecord();
                anterior = clave;
                archivoOriginal.readRecord();
                if(archivoOriginal.get(0).equals("")){
                    break;
                }
                clave = archivoOriginal.get(campo);
            }
            if(archivoOriginal.get(0).equals("") == false){
                numerodeTramos++; // nuevo tramo 
                numArchEs = (numArchEs < Nm-1) ? numArchEs+1 : 0;
                /*archivodeEntrada[numArchEs].write(archivoOriginal.get(0));
                archivodeEntrada[numArchEs].write(archivoOriginal.get(1));
                archivodeEntrada[numArchEs].write(archivoOriginal.get(2));
                archivodeEntrada[numArchEs].write(archivoOriginal.get(3));*/
                for (int i = 0; i < numcam; i++) {
                     archivodeEntrada[numArchEs].write(archivoOriginal.get(i));
                }
                archivodeEntrada[numArchEs].endRecord();
                anterior = clave;
            }
        }
        numerodeTramos++; 
        archivoOriginal.close();
        for (numArchEs = 0; numArchEs < Nm; numArchEs++)  
            archivodeEntrada[numArchEs].close(); 
        
        return numerodeTramos;
    }
    public boolean finDeTramos(boolean [] activo, int n) {
        boolean s = true;
        for(int k = 0; k < n; k++) {
            if (activo[k])  s = false; 
        }
        return s; 
    }
    public int minimo(String [] vectorTramo, boolean [] activo, int n, int campo) throws ParseException   // ->> aqui 
    {
        int indice;  
        String valor1 = null; 
        
        indice = 0; 
        /*switch(campo + 1)
        {
            case 1:
                valor1 = "999999999"; 
                break;
            case 2: 
                valor1 = "■■■■■■";
                break;
            case 3:
                valor1 = "true";
                break;
            case 4:
                valor1 = "11/11/2111";
                break;      
        }*/
        this.orden = orden;
        
        if(this.orden == true){
            valor1 = "         ";
        }else{
            valor1 = "■■■■■■";
        }    
        //valor1 = "         ";
        //valor1 = "■■■■■■";
        for ( int i = 0; i < n; i++) {
            if (activo[i] && (CompararCampos(vectorTramo[i], valor1, campo) == true)) {
                valor1 = vectorTramo[i];
                indice = i;     
            }
            //System.out.println(vectorTramo[i]);
        }
        return indice;
    }
    public boolean CompararCampos(String camp1, String camp2, int campo) throws ParseException{      // ->> aqui 
           int aux1=0, aux2=0; // variables en los que se guardaran los valores numericos] 
           /*switch(campo + 1)
           {
                case 1:
                   aux1 = Integer.parseInt(camp1);
                   aux2 = Integer.parseInt(camp2);
                   break;
                case 4:
                   SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                   Date fecha1 = fecha.parse(camp1);
                   Date fecha2 = fecha.parse(camp2);
                    if ( fecha1.before(fecha2)   ||  (fecha1.equals(fecha2))){
                       aux1=0;asc o des
                       aux2=1;
                    }  
                    else{
                       aux1=1;
                       aux2=0;
                   }
                   break;
                default: 
                    if(camp1.compareTo(camp2) <=0){
                        aux1=0;
                        aux2=1;
                    }
                    else{
                        aux1=1;
                        aux2=0;
                    }
                    break;
           }*/ 
           //--------------------------------------------------------------------------------------------------------------------
            this.orden = orden;
            if(this.orden == true){
            if (camp1.compareTo(camp2) > 0) {
                aux1 = 0;
                aux2 = 1;
            } else {
                aux1 = 1;
                aux2 = 0;
            }

            if (aux1 <= aux2) {
                return true;
            } else {
                return false;
            }

        } else {

            if (camp1.compareTo(camp2) <= 0) {
                aux1 = 0;
                aux2 = 1;
            } else {
                aux1 = 1;
                aux2 = 0;
            }
            if (aux1 <= aux2) {
                return true;
            } else {
                return false;
            }
        }

            
           
           /*if(camp1.compareTo(camp2) <=0){
                aux1=0;
                aux2=1;
            }
            else{
                aux1=1;
                aux2=0;
            }
           if (aux1 <= aux2){
               return true;
           }else{
           return false;
           }
               */
               
    }
    public void invertirOrden() throws  IOException
    {
        File file = new File(direccion);
        FileReader archivoOriginal = new FileReader(direccion);
        BufferedReader original = new BufferedReader(archivoOriginal);
        
        FileWriter archivoaux1 = new FileWriter(directorio + "archaux1.csv",true);
        BufferedWriter arcaux1 = new BufferedWriter(archivoaux1);
        
        
        String linea;
        int cont = 1;
        while((linea = original.readLine()) != null)
        {
            if(cont == 1)
            {
                arcaux1.write(linea);
                arcaux1.newLine();
                arcaux1.close();
                archivoaux1.close();
                cont++;
            }
            else
            {
                File file1 = new File(directorio + "archaux1.csv");
                File file2 = new File(directorio + "archaux2.csv");
                file1.renameTo(file2);
                archivoaux1 = new FileWriter(directorio + "archaux1.csv",true);
                arcaux1 = new BufferedWriter(archivoaux1);
                arcaux1.write(linea);
                arcaux1.newLine();
                FileReader archivolecaux2 = new FileReader(directorio + "archaux2.csv");
                BufferedReader auxarchlec = new BufferedReader(archivolecaux2);
                String linea2;
                while((linea2 = auxarchlec.readLine()) != null)
                {
                    arcaux1.write(linea2);
                    arcaux1.newLine();
                }
                arcaux1.close();
                archivoaux1.close();
                archivolecaux2.close();
                auxarchlec.close();
                File file3 = new File(directorio + "archaux2.csv");
                file3.delete();     
            }
        }
                
    }
    
    public String reverse(String palabra) {
    if (palabra.length() == 1)
      return palabra;
    else 
      return reverse(palabra.substring(1)) + palabra.charAt(0);
    }
    public void CrearArchivos(int numArchivos)
    {
        for(int i = 0 ; i < numArchivos ; i++)
        {
            String nombreArchivos =directorio+"\\archAux";
            nombreArchivos += i+1;
            nomArch[i] = nombreArchivos +".csv";
            System.out.println(nombreArchivos);
            arcAux[i] = new File(nomArch[i]);
        }
    }
    
}
