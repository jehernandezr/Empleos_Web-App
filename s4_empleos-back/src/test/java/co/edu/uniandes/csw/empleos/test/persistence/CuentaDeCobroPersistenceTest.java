/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.persistence;

import co.edu.uniandes.csw.empleos.entities.CuentaDeCobroEntity;
import co.edu.uniandes.csw.empleos.persistence.CuentaDeCobroPersistence;
import static java.util.Collections.list;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 *
 * @author Santiago Tangarife
 */
@RunWith(Arquillian.class)
public class CuentaDeCobroPersistenceTest {

    @Inject
    private CuentaDeCobroPersistence persistance;

    @PersistenceContext
    private EntityManager em;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addPackage(CuentaDeCobroEntity.class.getPackage())
                .addPackage(CuentaDeCobroPersistence.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");
    }

    /**
     * Prueba el metodo create con una cuenta de cobro aleatoria
     */
    @Test
    public void createCuentaDeCobroEntity() {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);

        CuentaDeCobroEntity cu = persistance.create(cuenta);

        Assert.assertNotNull(cu);

        CuentaDeCobroEntity entity = em.find(CuentaDeCobroEntity.class, cu.getId());

        Assert.assertEquals(entity.getNumeroCuentaDeCobro(), cu.getNumeroCuentaDeCobro());
        Assert.assertEquals(entity.getContratista(), cu.getContratista());
        Assert.assertEquals(entity.getFecha(), cu.getFecha());
    }

    /**
     * prueba el metodo find de Cuenta de cobro
     */
    @Test
    public void findCuentaDeCobroEntity() {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);

        //Crea una cuenta de cobro aleatoria
        CuentaDeCobroEntity cu = persistance.create(cuenta);
        Assert.assertNotNull(cu);

        Long id = cu.getId();
        //crea una cuenta nula, suponiendo que el id +1 no existe
        CuentaDeCobroEntity nula = persistance.find(id + 1);
        Assert.assertNull(nula);
        //busca una a cuenta por su id
        CuentaDeCobroEntity esta = persistance.find(id);
        Assert.assertNotNull(esta);

        Assert.assertEquals(cu.getContratista(), esta.getContratista());
        Assert.assertEquals(cu.getNumeroCuentaDeCobro(), esta.getNumeroCuentaDeCobro());
        Assert.assertEquals(cu.getFecha(), esta.getFecha());
    }

    /**
     * prueba el metodo findAll de Cuenta de cobro
     */
    @Test
    public void findAllCuentaDeCobroEntity() {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);

        //Crea una cuenta de cobro aleatoria
        CuentaDeCobroEntity c1 = persistance.create(cuenta);
        Assert.assertNotNull(c1);

        CuentaDeCobroEntity c2 = persistance.create(cuenta);
        Assert.assertNotNull(c2);

        CuentaDeCobroEntity c3 = persistance.create(cuenta);
        Assert.assertNotNull(c3);

        //busca una a cuenta por su id
        List<CuentaDeCobroEntity> cuentas = persistance.findAll();
        for (int i = 0; i < cuentas.size(); i++) {
            CuentaDeCobroEntity esta = cuentas.get(i);

            if (esta.getId().equals(c1.getId())) {
                Assert.assertEquals(esta.getContratista(), c1.getContratista());
                Assert.assertEquals(esta.getNumeroCuentaDeCobro(), c1.getNumeroCuentaDeCobro());
                Assert.assertEquals(esta.getFecha(), c1.getFecha());
            }
            else if (esta.getId().equals(c2.getId())) {
                Assert.assertEquals(esta.getContratista(), c2.getContratista());
                Assert.assertEquals(esta.getNumeroCuentaDeCobro(), c2.getNumeroCuentaDeCobro());
                Assert.assertEquals(esta.getFecha(), c2.getFecha());
            }
            else if (esta.getId().equals(c3.getId())) {
                Assert.assertEquals(esta.getContratista(), c3.getContratista());
                Assert.assertEquals(esta.getNumeroCuentaDeCobro(), c3.getNumeroCuentaDeCobro());
                Assert.assertEquals(esta.getFecha(), c3.getFecha());
            }
        }
    }
    
    /**
     * Prueba el metodo update de cuenta de cobro
     */
    @Test
    public void updateCuentaDeCobroEntity()
    {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta = factory.manufacturePojo(CuentaDeCobroEntity.class);
        
        CuentaDeCobroEntity cu= persistance.create(cuenta);
        Assert.assertNotNull(cu);
        //se hace una copia y se la modifica
        CuentaDeCobroEntity cn= cu;
        cn.setContratista("Losarig");
        cn.setFecha(new Date());
        cn.setNumeroCuentaDeCobro(51920);
        //Se la actualiza
        CuentaDeCobroEntity cn2= persistance.update(cn);
        Assert.assertNotNull(cn2);
        
        Assert.assertEquals(cn.getContratista(), cn2.getContratista());
        Assert.assertEquals(cn.getFecha(), cn2.getFecha());
        Assert.assertEquals(cn.getNumeroCuentaDeCobro(), cn2.getNumeroCuentaDeCobro());
    }
    
    /**
     * 
     */
    @Test
    public void deleteCuentaDeCobroEntity()
    {
        PodamFactory factory = new PodamFactoryImpl();
        CuentaDeCobroEntity cuenta= factory.manufacturePojo(CuentaDeCobroEntity.class);
        CuentaDeCobroEntity cu = persistance.create(cuenta);
        Long id = cu.getId();
        persistance.delete(id);
        Assert.assertNull(persistance.find(id));
    }
}