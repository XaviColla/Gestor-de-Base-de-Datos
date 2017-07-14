/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

/**
 *
 * @author estbm
 */
public class TablaFactory implements ProcesosFactory {

    @Override
    public CreacionTemplate creacionProceso() {
        return new CreacionTabla();
    }

    @Override
    public ModificacionTemplate modificacionProceso() {
        return new ModificacionTabla();
    }

    @Override
    public EliminacionTemplate eliminacionProceso() {
        return new EliminacionTabla();
    }
}
