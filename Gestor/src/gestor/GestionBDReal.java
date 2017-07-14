/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Interfaz.Interfaz;

/**
 *
 * @author estbm
 */
public class GestionBDReal implements GestionBD {
private static GestionBDReal newInstance = new GestionBDReal();

    private GestionBDReal() {
    }
    public static GestionBDReal getIntance()
    {
        return newInstance;
    }
    @Override
    public void Peticion(Object[] args) {

        //LLAMAR AL ABSTRACT FACTORY
        // TODO add your handling code here:
        if (args.length <= 0) {
            throw new Error("Lo sentimos, algo ha salido mal");
        }

        String caso = (String) args[0];
        System.out.println("------------------------------------------");
        switch (caso) {
            case "CREARTABLA": {
                String nombreTabla = (String) args[1];
                /*ArrayList<String> campos = (ArrayList<String>) (List) args[2];
                String campoClave = (String) args[3];
                int longitudCampos = (int) args[4];

                System.out.println("Nombre tabla: " + nombreTabla);
                System.out.println("Campos: " + campos);
                System.out.println("Campo clave: " + campoClave);
                System.out.println("Longitud: " + longitudCampos);*/

                //Instanciación a través del Abstract Factory
                ProcesosFactory factory = new TablaFactory();
                CreacionTemplate crear = factory.creacionProceso();
                Object[] arg = {args[1], args[2], args[3], args[4] , args[5]};
                crear.operation(arg);
                Interfaz.estadoOperacion = "Se ha creado la tabla " + nombreTabla + "";
                break;
            }
            case "MODIFICARTABLA": {
                String nombreTabla = (String) args[1];
                String nombreCampo = (String) args[2];
                String nuevoCampo = (String) args[3];

                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Nombre del campo a modificar: " + nombreCampo);
                System.out.println("Nuevo valor del campo: " + nuevoCampo);

                //Instanciación a través del Abstract Factory
                ProcesosFactory factory = new TablaFactory();
                ModificacionTemplate modificar = factory.modificacionProceso();
                Object[] arg = {args[1], args[2], args[3],args[4],args[5]};
                modificar.operation(arg);

                Interfaz.estadoOperacion = "Se ha modificado la tabla " + nombreTabla + "";
                break;
            }
            case "ELIMINARTABLA": {
                String nombreTabla = (String) args[1];
                System.out.println("Tabla: " + nombreTabla);

                //Instanciación a través del Abstract Factory
                ProcesosFactory factory = new TablaFactory();
                EliminacionTemplate eliminar = factory.eliminacionProceso();
                Object[] arg = {args[1]};
                eliminar.operation(arg);

                Interfaz.estadoOperacion = "Se ha eliminado la tabla " + nombreTabla + "";
                break;
            }
            case "CREARREGISTRO": {
                String nombreTabla = (String) args[1];
                ArrayList<String> valoresCampos = (ArrayList<String>) (List) args[2];

                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Valores de los campos: " + valoresCampos);

                //Instanciación a través del Abstract Factory
                ProcesosFactory factory = new RegistroFactory();
                CreacionTemplate crear = factory.creacionProceso();
                Object[] arg = {args[1], args[2] , args[3]};
                crear.operation(arg);

                Interfaz.estadoOperacion = "Se agrego el registro a la tabla " + nombreTabla + "";
                break;
            }
            case "MODIFICARREGISTRO": {
                String nombreTabla = (String) args[1];
                String campoClave = (String) args[2];
                String nombreCampo = (String) args[3];
                String valorCampo = (String) args[4];

                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Campo clave: " + campoClave);
                System.out.println("Nombre del campo a modificar: " + nombreCampo);
                System.out.println("Nuevo valor del campo: " + valorCampo);

                //Instanciación a través del Abstract Factory
                ProcesosFactory factory = new RegistroFactory();
                ModificacionTemplate modificar = factory.modificacionProceso();
                Object[] arg = {args[1], args[2], args[3], args[4], args[5],args[6],args[7]};
                modificar.operation(arg);

                Interfaz.estadoOperacion = "Se modifico un registro de la tabla " + nombreTabla + "";
                break;
            }
            case "ELIMINARREGISTRO": {
                String nombreTabla = (String) args[1];
                String campoClave = (String) args[2];

                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Campo clave: " + campoClave);

                //Instanciación a través del Abstract Factory
                ProcesosFactory factory = new RegistroFactory();
                EliminacionTemplate eliminar = factory.eliminacionProceso();
                Object[] arg = {args[1], args[2], args[3]};
                eliminar.operation(arg);

                Interfaz.estadoOperacion = "Se elimino un registro de la tabla " + nombreTabla + "";
                break;
            }
            case "SELECCIONAR": {
                String nombreTabla = (String) args[1];
                String nombreCampo = (String) args[2];
                String valorCampo = (String) args[3];
                int valor = (int) args[4];
                String tipoOrdenamiento = (String) args[5];
                int contArch = (int) args[6];
                System.out.println("Tabla: " + nombreTabla);
                System.out.println("Campo: " + nombreCampo);
                System.out.println("Valor: " + valorCampo);
                System.out.println(valor);

                Object[] arg = {args[1], args[2], args[3], args[4] , args[5] , args[6]};
                try {
                    FactoryAlternoBD factory = new FactorySelTabla();
                    ((SeleccionarTabla)factory.getInstance(arg)).Visualizar();
                    //new SeleccionarTabla(arg).Visualizar();
                } catch (IOException ex) {
                    throw new Error(ex.getMessage());
                }

                Interfaz.estadoOperacion = "Se seleccionarion registros de la tabla " + nombreTabla + "";
                break;
            }
            case "UNIR": {
                String nombreTabla1 = (String) args[1];
                String nombreTabla2 = (String) args[2];
                String nombreCampo = (String) args[3];

                System.out.println("Tabla 1: " + nombreTabla1);
                System.out.println("Tabla 2: " + nombreTabla2);
                System.out.println("Campo: " + nombreCampo);

                Object[] arg = {args[1], args[2], args[3] ,args[4] ,args[5]};
                //new JOIN(arg).Visualizar();

               Interfaz.estadoOperacion = "Se uniron las tablas " + nombreTabla1 + " y " + nombreTabla2;
                break;
            }
            default:
                throw new Error("Lo sentimos, algo ha salido mal");
        }
    }
}
