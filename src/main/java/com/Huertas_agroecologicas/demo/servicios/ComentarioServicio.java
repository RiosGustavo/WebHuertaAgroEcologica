/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Comentario;
import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.ComentarioRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.CultivoRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.HuertaRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class ComentarioServicio {
   
    @Autowired
    private ComentarioRepositorio comentarioRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
   
    
    @Autowired
    private HuertaRepositorio huertaRepositorio;
    
    @Autowired
    private CultivoRepositorio cultivoRepositorio;
    
    @Transactional
    public void crearComentario(String idHuerta, String idCultivo, String idConsumidor ,String mensaje) throws MiException{
        
        
        if(mensaje == null || mensaje.isEmpty()){
        
            throw new MiException("¡No podes hacer un comentario vacío!");
        }
        Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(idConsumidor);
        Optional<Huerta> respuestaHuerta = huertaRepositorio.findById(idHuerta);
        Optional<Cultivo> respuestaCultivo = cultivoRepositorio.findById(idCultivo);
       
        Comentario comentario = new Comentario();
        
        comentario.setAltaBaja(true);
        comentario.setMensaje(mensaje);
        comentario.setFechaPublicacion(new Date());
        
        if (respuestaUsuario.isPresent()) {
            Usuario usuario = respuestaUsuario.get();
            if (usuario.getRoles().toString().equalsIgnoreCase("CON")) {
                Consumidor consumidor = (Consumidor) respuestaUsuario.get();
                comentario.setConsumidor(consumidor);
            }
        }
         if (respuestaHuerta.isPresent()) {
            comentario.setHuerta(respuestaHuerta.get());
        
        }
         
         if (respuestaCultivo.isPresent()) {
            comentario.setCultivo(respuestaCultivo.get());
        
        }
       comentarioRepositorio.save(comentario);
    }
    
   public List<Comentario> comentariosPorHuerta(String idHuerta){
       List<Comentario> comentarios = new ArrayList();
       
       comentarios = comentarioRepositorio.comentariosPorHuerta(idHuerta);
       return comentarios;
   }
   
   public List<Comentario> comentariosPorCultivo(String idCultivo){
       List<Comentario> comentarios = new ArrayList();
       
       comentarios = comentarioRepositorio.comentariosPorHuerta(idCultivo);
       return comentarios;
   }
   
   public List<Comentario> comentariosPorConsumidor(Consumidor consumidor){
       
             
       return comentarioRepositorio.comentariosPorConsumidor(consumidor.getId());
   }
   
   
   
   @Transactional
   public void darDeBajaComentario(String idComentario) throws MiException{
       
       Optional<Comentario> respuesta = comentarioRepositorio.findById(idComentario);
       
       if(respuesta.isPresent()){
           Comentario comentario = respuesta.get();
           comentario.setAltaBaja(Boolean.FALSE);
           comentarioRepositorio.save(comentario);
       }
   }
    
    
    
    
    
}
