/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestor;

/**
 *
 * @author Christian Collaguazo
 */
public class FactorySelTabla extends FactoryAlternoBD{

    @Override
    public Object getInstance(Object []args) {
        return new SeleccionarTabla(args);
    }
    
}
