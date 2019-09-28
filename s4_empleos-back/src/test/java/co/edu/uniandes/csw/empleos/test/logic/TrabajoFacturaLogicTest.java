/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.test.logic;

import co.edu.uniandes.csw.empleos.ejb.TrabajoFacturaLogic;
import co.edu.uniandes.csw.empleos.ejb.TrabajoLogic;
import co.edu.uniandes.csw.empleos.ejb.FacturaLogic;
import co.edu.uniandes.csw.empleos.entities.TrabajoEntity;
import co.edu.uniandes.csw.empleos.entities.FacturaEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import co.edu.uniandes.csw.empleos.persistence.FacturaPersistence;
import co.edu.uniandes.csw.empleos.persistence.TrabajoPersistence;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


/**
 *
 * @author David Dominguez
 */
@RunWith(Arquillian.class)
public class TrabajoFacturaLogicTest {
    
    private PodamFactory factory = new PodamFactoryImpl();

    @Inject
    private TrabajoLogic trabajoLogic;
    @Inject
    private FacturaLogic facturaLogic;

    @Inject
    private TrabajoFacturaLogic trabajoFacturaLogic;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private UserTransaction utx;

    private List<TrabajoEntity> caldata = new ArrayList<TrabajoEntity>();
    private List<FacturaEntity> data = new ArrayList<FacturaEntity>();

    /**
     * @return Devuelve el jar que Arquillian va a desplegar en Payara embebido.
     * El jar contiene las clases, el descriptor de la base de datos y el
     * archivo beans.xml para resolver la inyección de dependencias.
     */
    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addPackage(TrabajoEntity.class.getPackage())
                .addPackage(TrabajoLogic.class.getPackage())
                .addPackage(TrabajoPersistence.class.getPackage())
                .addPackage(FacturaEntity.class.getPackage())
                .addPackage(FacturaPersistence.class.getPackage())
                .addPackage(FacturaLogic.class.getPackage())
                .addPackage(TrabajoFacturaLogic.class.getPackage())
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml");

    }

    /**
     * Configuración inicial de la prueba.
     */
    @Before
    public void configTest() {
        try {
            utx.begin();
            clearData();
            insertData();
            utx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        em.createQuery("delete from TrabajoEntity").executeUpdate();
        em.createQuery("delete from FacturaEntity").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las
     * pruebas.
     */
    private void insertData() {


        for (int i = 0; i < 3; i++) {
            FacturaEntity entity = factory.manufacturePojo(FacturaEntity.class);
            em.persist(entity);
            data.add(entity);
            if (i == 0) {
                caldata.get(i).setFactura(entity);
            }
        }
        
         for (int i = 0; i < 3; i++) {
            TrabajoEntity trabajos = factory.manufacturePojo(TrabajoEntity.class);
            em.persist(trabajos);
            caldata.add(trabajos);
        }

    }
    
    /**
     * Prueba para remplazar las instancias de Books asociadas a una instancia
     * de Editorial.
     */
    @Test
    public void replaceFacturaTest() {
        TrabajoEntity entity = caldata.get(0);
        trabajoFacturaLogic.replaceFactura(entity.getId(), data.get(1).getId());
        entity = trabajoLogic.getTrabajo(entity.getId());
        Assert.assertEquals(entity.getOferta(), data.get(1));
    }
    
}
