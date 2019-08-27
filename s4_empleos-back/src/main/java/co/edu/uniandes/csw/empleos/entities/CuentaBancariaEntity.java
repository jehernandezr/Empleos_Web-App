
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;


/**
 * @author je.hernandezr
 */
@Entity
public class CuentaBancariaEntity extends BaseEntity{

    
    private int numeroCuenta;
    
    private String fecha;
   
    
    
    public CuentaBancariaEntity()
    {
        
    }
    
  public CuentaBancariaEntity(int pNumeroCuenta, String pFecha)
  {
      numeroCuenta=pNumeroCuenta;
      fecha=pFecha;
  }
   
    /**
     * @return the numeroCuenta
     */
    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * @param numeroCuenta the numeroCuenta to set
     */
    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

}