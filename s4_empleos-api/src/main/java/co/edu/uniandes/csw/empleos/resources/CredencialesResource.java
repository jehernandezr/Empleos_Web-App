/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.empleos.resources;

import co.edu.uniandes.csw.empleos.dtos.ContratistaDTO;
import co.edu.uniandes.csw.empleos.dtos.CredencialDTO;
import co.edu.uniandes.csw.empleos.dtos.EstudianteDTO;
import co.edu.uniandes.csw.empleos.dtos.TokenDTO;
import co.edu.uniandes.csw.empleos.dtos.UsuarioDTO;
import co.edu.uniandes.csw.empleos.ejb.ContratistaLogic;
import co.edu.uniandes.csw.empleos.ejb.CredencialesLogic;
import co.edu.uniandes.csw.empleos.ejb.EstudianteLogic;
import co.edu.uniandes.csw.empleos.ejb.TokenLogic;
import co.edu.uniandes.csw.empleos.ejb.Tokenizer;
import co.edu.uniandes.csw.empleos.entities.CredencialesEntity;
import co.edu.uniandes.csw.empleos.entities.TokenEntity;
import co.edu.uniandes.csw.empleos.exceptions.BusinessLogicException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author David Dominguez
 */
@Path("credenciales")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class CredencialesResource {

    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private CredencialesLogic credencialLogic;
    
    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private EstudianteLogic estudianteLogic;
    
    // Variable para acceder a la lógica de la aplicación. Es una inyección de dependencias.
    @Inject
    private ContratistaLogic contratistaLogic;

    @Inject
    private TokenLogic tokenLogic;
    
    TokenDTO fooDTO(String m) {
        TokenDTO t = new TokenDTO();
        t.setToken(m);
        return t;
    }

    @POST
    public TokenDTO createCredencial(UsuarioDTO usuario, @QueryParam("correo") String correo, @QueryParam("pass") String pass, @QueryParam("tipo") String type) throws BusinessLogicException {
        CredencialDTO credencial = new CredencialDTO();
        credencial.setCorreo(correo);
        credencial.setTipo(type);
        credencial.setContrasenia(pass);
        
        CredencialDTO c = new CredencialDTO(credencialLogic.createCredencial(credencial.toEntity()));

        String tipo = c.getTipo();
        String token = Tokenizer.getToken();

        TokenEntity tokenE = new TokenEntity();
        tokenE.setTipo(tipo);
        tokenE.setToken(token);
        TokenDTO nuevoTokenDTO = new TokenDTO(tokenLogic.createToken(tokenE));
        
        if(type.equals("Estudiante")) {
            EstudianteDTO estudiante = new EstudianteDTO();
            estudiante.setId(usuario.getId());
            estudiante.setIdMedioDepago(usuario.getIdMedioDepago());
            estudiante.setNombre(usuario.getNombre());
            estudiante.setCarrera(usuario.getCarrera());
            estudiante.setCalificacionPromedio(usuario.getCalificacionPromedio());
            estudiante.setSemestre(usuario.getSemestre());
            estudiante.setHorarioDeTrabajo(usuario.getHorarioDeTrabajo()); 
            estudiante.setCorreo(usuario.getEmail());
            estudianteLogic.crearEstudiante(estudiante.toEntity());
        } else if (type.equals("Contratista")) {
            ContratistaDTO contratista = new ContratistaDTO();
            contratista.setId(usuario.getId());
            contratista.setEsExterno(usuario.getEsExterno());
            contratista.setNombre(usuario.getNombre());
            contratista.setEmail(usuario.getEmail());
            contratista.setRutaImagen(usuario.getRutaImagen());
            contratistaLogic.createContratista(contratista.toEntity());
        }
        
        return nuevoTokenDTO;
    }

    @GET
    public TokenDTO autenticar(@QueryParam("correo") String correo, @QueryParam("pass") String pass) throws BusinessLogicException {        
        List<CredencialesEntity> c = credencialLogic.getCredenciales();
        CredencialesEntity credencialUsuario = null;
        boolean found = false;
        for (CredencialesEntity credencial : c) {
            if(credencial != null) {
               String c_correo = credencial.getCorreo();
                String c_pass = credencial.getContrasenia();
                if(c_correo != null && c_pass != null) {
                    if (c_correo.equals(correo) && c_pass.equals(pass)) {
                        found = true;
                        credencialUsuario = credencial;
                    }
                }
            } 
        }

        if (!found) {
            return null;
        } else {
            String tipo = credencialUsuario.getTipo();
            String token = Tokenizer.getToken();

            TokenEntity tokenE = new TokenEntity();
            tokenE.setTipo(tipo);
            tokenE.setToken(token);
            TokenDTO nuevoTokenDTO = new TokenDTO(tokenLogic.createToken(tokenE));
 
            return nuevoTokenDTO;
        }
    }

    /**
     * Busca el estudiante con el id asociado recibido en la URL y lo devuelve.
     *
     * @param estudianteId Identificador del estudiante que se esta buscando.
     * Este debe ser una cadena de dígitos.
     * @return JSON {@link EstudianteDTO} - El estudiante buscado
     * @throws WebApplicationException {@link WebApplicationExceptionMapper} -
     * Error de lógica que se genera cuando no se encuentra el estudiante.
     */
    /*
    @GET
    @Path("{estudiantesId: \\d+}")
    public EstudianteDetailDTO getEstudiante(@PathParam("estudiantesId") Long estudianteId) {
        EstudianteEntity calEntity = estudianteLogic.getEstudiante(estudianteId);
        if (calEntity == null) {
            throw new WebApplicationException("El recurso /estudiantes/" + estudianteId + " no existe.", 404);
        }
        EstudianteDetailDTO calDTO = new EstudianteDetailDTO(calEntity);
        return calDTO;
    }
     */
    /**
     * Busca y devuelve todos los estudiantes que existen en la aplicacion.
     *
     * @return JSONArray {@link EstudianteDTO} - Los estudiantes encontrados en
     * la aplicación. Si no hay ninguna retorna una lista vacía.
     */
    /*
    @GET
    public List<EstudianteDetailDTO> getEstudiantes() {
        List<EstudianteDetailDTO> estudiantes = listEntity2DTO(estudianteLogic.getEstudiantes());
        return estudiantes;
    }
     */
}
