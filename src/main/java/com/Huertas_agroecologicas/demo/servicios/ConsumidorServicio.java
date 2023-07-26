/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Imagen;

import com.Huertas_agroecologicas.demo.enumeraciones.Rol;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.ConsumidorRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.CultivoRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.HuertaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class ConsumidorServicio {

    @Autowired
    private ConsumidorRepositorio consumidorRepositorio;

    @Autowired
    private CultivoRepositorio cultivoRepositorio;

    @Autowired
    private HuertaRepositorio huertaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrarConsumidor(MultipartFile archivo,  String nombreConsumidor, String dni,
            String direccion, String email, String password, String password2) throws MiException, Exception {

//        Optional<Huerta> respuestaHuerta = huertaRepositorio.findById(idHuerta);
//        Optional<Cultivo> irespuestaCultivo = cultivoRepositorio.findById(idCultivo);

        

            validar(archivo, nombreConsumidor, dni, direccion, email, password, password2);

//            Huerta huerta = respuestaHuerta.get();
//            List<Huerta> huertas = new ArrayList();
//
//            Cultivo cultivo = irespuestaCultivo.get();
//            List<Cultivo> cultivos = new ArrayList();

            Consumidor consumidor = new Consumidor();

            consumidor.setNombreConsumidor(nombreConsumidor);
            consumidor.setDni(dni);
            consumidor.setDireccion(direccion);
            consumidor.setEmail(email);
//            consumidor.setHuertas(huertas);
//            consumidor.setCultivos(cultivos);

            consumidor.setAltaBaja(Boolean.FALSE);
            consumidor.setFechaAlta(new Date());
            consumidor.setPassword(new BCryptPasswordEncoder().encode(password));
            consumidor.setRoles(Rol.PRO);

            if (archivo.getSize() == 0) {
                Imagen imagen = imagenServicio.obtenerImagenPorDefecto();
                consumidor.setImagen(imagen);
            } else {
                Imagen imagen = imagenServicio.guardar(archivo);
                consumidor.setImagen(imagen);
            }

            consumidorRepositorio.save(consumidor);

        

    }
    
    
       @Transactional
    public void modificarConsumidor(MultipartFile archivo, String id,  String nombreConsumidor, String dni,
            String direccion) throws MiException, Exception {

        Optional<Consumidor> respuesta = consumidorRepositorio.findById(id);

        if (id == null || id.isEmpty()) {
            throw new MiException("Debe ingrear un id del consumidor");

        }
        validarModificar(archivo, nombreConsumidor, dni, direccion);

        if (respuesta.isPresent()) {

            Consumidor consumidor = respuesta.get();

//            Optional<Huerta> respuestaHuerta = huertaRepositorio.findById(idHuerta);
//            Optional<Cultivo> respuestaCosecha = cultivoRepositorio.findById(idCultivo);

//            if (respuestaHuerta.isPresent()) {
//                Huerta huertas = respuestaHuerta.get();
//                consumidor.setHuertas((List<Huerta>) huertas);
//            }
//
//            if (respuestaCosecha.isPresent()) {
//                Cultivo cultivos = respuestaCosecha.get();
//                 consumidor.setCultivos((List<Cultivo>) cultivos);
//            }

            consumidor.setNombreConsumidor(nombreConsumidor);
            consumidor.setDni(dni);
            consumidor.setDireccion(direccion);

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = consumidor.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                consumidor.setImagen(imagen);
            }

            consumidorRepositorio.save(consumidor);

        }

    }
    
    
    @Transactional
    public Consumidor getOne(String id) {
        return consumidorRepositorio.getOne(id);
    }

    @Transactional
    public List<Consumidor> listarConsumidores() {
        List<Consumidor> consumidores = new ArrayList();
        consumidores =  consumidorRepositorio.findAll();
        return consumidores;
    }
    
    
    
    public List<Consumidor> consumidoresPorHuerta(String idHuerta) {
        
        List<Consumidor> consumidores = new ArrayList();
        
        consumidores = consumidorRepositorio.findByHuertasId(idHuerta);
        
        return consumidores;
    }
    
    public List<Consumidor> consumidoresPorCultivo(String idCultivo) {
        
        List<Consumidor> consumidores = new ArrayList();
        
        consumidores = consumidorRepositorio.findByCultivosId(idCultivo);
        
        return consumidores;
    }
    
    
    
    
     public List<Consumidor> search(String termino, String estado,String orden) {

        List<Consumidor> consumidores = new ArrayList();
        consumidores = consumidorRepositorio. search(termino, estado,orden);
        return consumidores;
    }
    
    public List<Consumidor> search2(String estado,String orden) {

        List<Consumidor> consumidores = new ArrayList();
        consumidores = consumidorRepositorio. search2(estado,orden);
        return consumidores;
    }
    
    @Transactional
    public void darDeBajaConsumidor(String id) throws MiException{
        
       Optional<Consumidor> respuesta = consumidorRepositorio.findById(id);
       
       if(respuesta.isPresent()){
           Consumidor consumidor = respuesta.get();
           consumidor.setAltaBaja(Boolean.FALSE);
           consumidorRepositorio.save(consumidor);
       }
    }
    
    
     @Transactional
    public void darDeAltaConsumidor(String id) throws MiException{
        
       Optional<Consumidor> respuesta = consumidorRepositorio.findById(id);
       
       if(respuesta.isPresent()){
           Consumidor consumidor = respuesta.get();
           consumidor.setAltaBaja(Boolean.TRUE);
           consumidorRepositorio.save(consumidor);
       }
    }
    
    
    
    
    
    

    private void validar(MultipartFile archivo, String nombreConsumidor, String dni, 
            String direccion, String email, String password, String password2) throws MiException {
        
         // Verificar si el email ya existe en la base de datos
        Consumidor usuarioExistente = consumidorRepositorio.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new MiException("El email ingresado ya está registrado");
        }

        if (nombreConsumidor.isEmpty() || nombreConsumidor == null) {
            throw new MiException("Debe ingrear el nombre del Consumidor");
        }

        if (dni.isEmpty() || dni == null) {
            throw new MiException("Debe ingrear el dni del Consumidor");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("Debe ingrear la direccion del Consumidor");
        }
       

        if (email.isEmpty() || email == null) {
            throw new MiException("Debe debe ingresar email valido de su Consumidor");
        }

        if (password == null || password.isEmpty()) {
            throw new MiException("La contraseña no puede estar vacía");
        }
        if (!password.equals(password2)) {

            throw new MiException("Las contraseñas no coinciden. Por favor introduzcalas correctamente");

        }
        if (password.length() < 8) {
            throw new MiException("La contraseña debe tener al menos 8 caracteres");

        }
        
        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new MiException("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
        
    }

    private void validarModificar(MultipartFile archivo, String nombreConsumidor, String dni, String direccion) throws MiException {
          if (nombreConsumidor.isEmpty() || nombreConsumidor == null) {
            throw new MiException("El nombre del consumidor no puede estar vacío.");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("La dirección no puede estar vacía.");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new MiException("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
        
    }

}
