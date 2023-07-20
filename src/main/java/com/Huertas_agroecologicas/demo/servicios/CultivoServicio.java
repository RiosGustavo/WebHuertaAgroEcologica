/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.CultivoRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.EstadisticaRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.HuertaRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.ImagenRepositorio;
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
public class CultivoServicio {

    @Autowired
    private CultivoRepositorio cultivoRepositorio;

    @Autowired
    private HuertaRepositorio huertaRepositorio;

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private EstadisticaRepositorio estadisticaRepositorio;

    @Transactional
    public void crearCultivo(MultipartFile archivo, String nombreCultivo, String descripcion, Integer precio, Integer stock, String idHuerta) throws MiException, Exception {

        validar(archivo, nombreCultivo, descripcion, precio, stock);

        List<Huerta> huertas = new ArrayList();

        Cultivo cultivo = new Cultivo();

        cultivo.setNombreCultivo(nombreCultivo);
        cultivo.setDescripcion(descripcion);
        cultivo.setPrecio(precio);
        cultivo.setStock(stock);
        cultivo.setFechaAlta(new Date());
        cultivo.setAltaBaja(Boolean.TRUE);

        Imagen imagen = imagenServicio.guardar(archivo);
        cultivo.setImagen(imagen);

        cultivo.setHuertas(huertas);

        cultivoRepositorio.save(cultivo);

    }

    @Transactional
    public void actualizarCultivo(MultipartFile archivo, String nombreCultivo, String descripcion,
            Integer precio, Integer stock, String idCultivo) throws Exception {

        validar(archivo, nombreCultivo, descripcion, precio, stock);

        Optional<Cultivo> respuesta = cultivoRepositorio.findById(idCultivo);

        if (respuesta.isPresent()) {
            Cultivo cultivo = respuesta.get();

            cultivo.setNombreCultivo(nombreCultivo);
            cultivo.setDescripcion(descripcion);
            cultivo.setPrecio(precio);
            cultivo.setStock(stock);

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = cultivo.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                cultivo.setImagen(imagen);

            }
            cultivoRepositorio.save(cultivo);
        }

    }

    @Transactional
    public Cultivo getOne(String id) {
        return cultivoRepositorio.getOne(id);
    }

    @Transactional
    public List<Cultivo> listarCultivos() {
        List<Cultivo> cultivos = new ArrayList();

        cultivos = cultivoRepositorio.findAll();

        return cultivos;
    }

    @Transactional
    public List<Cultivo> listarCultivosActivos() {
        List<Cultivo> cultivos = new ArrayList();

        cultivos = cultivoRepositorio.buscarPorEstado();
        return cultivos;
    }

    public List<Cultivo> search(String termino, String estado, String orden) {

        List<Cultivo> cultivos = new ArrayList();
        cultivos = cultivoRepositorio.search(termino, estado, orden);
        return cultivos;
    }

    public List<Cultivo> search2(String estado, String orden) {

        List<Cultivo> cultivos = new ArrayList();
        cultivos = cultivoRepositorio.search2(estado, orden);
        return cultivos;
    }

    public List<Cultivo> buscarHuertasPorNombreCultivo(String nombreCultivo) {

        List<Cultivo> cultivos = new ArrayList();
        cultivos = cultivoRepositorio.buscarPorNombreCultivo(nombreCultivo);

        return cultivos;
    }

    @Transactional
    public void darDeBajaCultivo(String idCultivo) throws MiException {
        Optional<Cultivo> respuesta = cultivoRepositorio.findById(idCultivo);

        if (respuesta.isPresent()) {
            Cultivo cultivo = respuesta.get();
            cultivo.setAltaBaja(Boolean.FALSE);
            cultivoRepositorio.save(cultivo);

        }
    }

    @Transactional
    public void darDeAltaCultivo(String idCultivo) throws MiException {
        Optional<Cultivo> respuesta = cultivoRepositorio.findById(idCultivo);

        if (respuesta.isPresent()) {
            Cultivo cultivo = respuesta.get();
            cultivo.setAltaBaja(Boolean.TRUE);
            cultivoRepositorio.save(cultivo);

        }
    }
    
    
     public List<Cultivo> cultivosPorHuerta(String idHuerta) {

        List<Cultivo> cultivos = new ArrayList();

        cultivos = cultivoRepositorio.buscarPorHuertas(idHuerta);
        return cultivos;
    }
    
    
    

    private void validar(MultipartFile archivo, String nombreCultivo, String descripcion, Integer precio, Integer stock) throws Exception {
       
        
        if (nombreCultivo.isEmpty() || nombreCultivo == null) {
            throw new Exception("El nombre  del cultivo no puede estar vacío.");
        }

        if (descripcion.isEmpty() || descripcion == null) {
            throw new Exception("La descripcion del cultivo no puede estar vacío.");
        }
        
         if (precio <= 0 || precio == null) {
            throw new Exception("el precio no puede estar vacío o ser negativo.");
        }
         
           if (stock <= 0 || stock == null) {
            throw new Exception("el stock no puede estar vacío o ser negativo.");
        }

        
        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
        
    }
    
    
    ///// metodo opcional para dar de baja los cultivos pasados cierto itempo 
    public void darDeBajaCultivosAntiguos() {
        List<Cultivo> cultivosActivos = cultivoRepositorio.findByAltaBajaTrue();
                
        Date fechaActual = new Date();

        for (Cultivo cultivo : cultivosActivos) {
            Date fechaAlta = cultivo.getFechaAlta();
            long diferenciaTiempo = fechaActual.getTime() - fechaAlta.getTime();
            long diasPasados = diferenciaTiempo / (1000 * 60 * 60 * 24);

            if (diasPasados >= 30) {
                cultivo.setAltaBaja(Boolean.FALSE);
                        

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fechaAlta);
                calendar.add(Calendar.DAY_OF_MONTH, 30);

                Date fechaBaja = calendar.getTime();

                cultivo.setFechaBaja(fechaBaja);
                cultivoRepositorio.save(cultivo);
            }
        }
    }

}



