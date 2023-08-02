/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.entiddes.Productor;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.ConsumidorRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.CultivoRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.HuertaRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.ProductorRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.PublicacionRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class HuertaServicio {

    @Autowired
    private HuertaRepositorio huertaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private ProductorRepositorio productorRepositorio;

    @Autowired
    private CultivoRepositorio cultivoRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;
    
    @Autowired
    private ConsumidorRepositorio consumidorRepositorio;

    @Transactional
    public void crearHuerta(MultipartFile archivo, String nombreHuerta, String cuerpo, String id) throws MiException, Exception {

        validar(archivo, nombreHuerta, cuerpo);
        Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(id);
        List<Cultivo> cultivos = new ArrayList();

        Huerta huerta = new Huerta();

        if (respuestaUsuario.isPresent()) {

            Usuario usuario = respuestaUsuario.get();

            if (usuario.getRoles().toString().equalsIgnoreCase("PRO")) {
                Productor pro = (Productor) respuestaUsuario.get();
                huerta.setProductor(pro);

            }
        }

        huerta.setNombreHuerta(nombreHuerta);
        huerta.setCuerpo(cuerpo);
        huerta.setAltaBaja(Boolean.TRUE);
        huerta.setFechaAlta(new Date());
        Imagen imagen = imagenServicio.guardar(archivo);
        huerta.setImagen(imagen);
        huerta.setCultivos(cultivos);

        huertaRepositorio.save(huerta);

    }
    
    @Transactional
    public void actualizarHuerta(MultipartFile archivo, String nombreHuerta, String cuerpo, String idHuerta ) throws MiException, Exception{
        validar(archivo, nombreHuerta, cuerpo);
        
        Optional<Huerta> respuesta = huertaRepositorio.findById(idHuerta);
        
        if(respuesta.isPresent()){
            Huerta huerta = respuesta.get();
            
            huerta.setNombreHuerta(nombreHuerta);
            huerta.setCuerpo(cuerpo);
            
            String idImagen = null;
            if(archivo.getSize() > 0){
                idImagen  = huerta.getImagen().getId();
                Imagen iamgen = imagenServicio.actualizar(archivo, idImagen);
                huerta.setImagen(iamgen);
            }
            huertaRepositorio.save(huerta);
            
        }
    }
    
    @Transactional
    public Huerta getOne(String idHuerta){
        return huertaRepositorio.getOne(idHuerta);
    }
    
    @Transactional
    public List<Huerta> listarHuertas(){
        List<Huerta> huertas = new ArrayList();
        
        huertas = (List<Huerta>) huertaRepositorio.findAll();
        return huertas;
    }
    
    @Transactional
   public List<Huerta> listarHuertasActivas(){
       List<Huerta> huertas = new ArrayList();
       
       huertas = huertaRepositorio.buscarPorEstado();
       return huertas;
   }
   
    public List<Huerta> search(String termino, String estado, String orden) {

        List<Huerta> huertas = new ArrayList();
        huertas = huertaRepositorio.search(termino, estado, orden);
        return huertas;
    }

    public List<Huerta> search2(String estado, String orden) {

        List<Huerta> huertas = new ArrayList();
        huertas = huertaRepositorio.search2(estado, orden);
        return huertas;
    }

    public List<Huerta> buscarHuertasPorNombreHuerta(String nombreHuerta) {

        List<Huerta> huertas = new ArrayList();
        huertas = huertaRepositorio.buscarPorHuerta(nombreHuerta);
        return huertas;
    }
    
    @Transactional
    public void darDeBajaHuerta(String idHuerta) throws MiException {
        Optional<Huerta> respuesta = huertaRepositorio.findById(idHuerta);

        if (respuesta.isPresent()) {
            Huerta huerta = respuesta.get();
            huerta.setAltaBaja(Boolean.FALSE);
            huertaRepositorio.save(huerta);

        }
    }

      @Transactional
    public void darDeAltaHuerta(String idHuerta) throws MiException {
        Optional<Huerta> respuesta = huertaRepositorio.findById(idHuerta);

        if (respuesta.isPresent()) {
            Huerta huerta = respuesta.get();
            huerta.setAltaBaja(Boolean.TRUE);
            huertaRepositorio.save(huerta);

        }
    }

    public Huerta huertasPorProductor(String id) {

        Huerta huerta = new Huerta();

        huerta = huertaRepositorio.huertaPorProductor(id);
        return huerta;
    }
    
    
    public List<Huerta> huertasPorConsumidor(String id){
        
        List<Huerta> huertas = new ArrayList();
        
        huertas = huertaRepositorio.buscarHuertaPorConsumior(id);
        
        return huertas;
    }
 
    
    
    public void validar(MultipartFile archivo, String nombreHuerta, String cuerpo) throws Exception {

        if (nombreHuerta.isEmpty() || nombreHuerta == null) {
            throw new Exception("El nombre  de la Huerta no puede estar vacío.");
        }

        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new Exception("El cuerpo de la campaña no puede estar vacío.");
        }

        
        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }

    }
///// metodo opcional para dar de baja las huertas pasados cierto itempo 
    public void darDeBajaHuertasAntiguas() {
        List<Huerta> huertasActivas = huertaRepositorio.findByAltaBajaTrue();
        Date fechaActual = new Date();

        for (Huerta huerta : huertasActivas) {
            Date fechaAlta = huerta.getFechaAlta();
            long diferenciaTiempo = fechaActual.getTime() - fechaAlta.getTime();
            long diasPasados = diferenciaTiempo / (1000 * 60 * 60 * 24);

            if (diasPasados >= 30) {
                huerta.setAltaBaja(Boolean.FALSE);
                        

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaAlta);
                calendar.add(Calendar.DAY_OF_MONTH, 30);

                Date fechaBaja = calendar.getTime();

                huerta.setFechaBaja(fechaBaja);
                huertaRepositorio.save(huerta);
            }
        }
    }
    

    

}
