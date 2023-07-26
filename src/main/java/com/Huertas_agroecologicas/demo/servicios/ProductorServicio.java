/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.entiddes.Productor;
import com.Huertas_agroecologicas.demo.enumeraciones.Rol;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.HuertaRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.ProductorRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.Huertas_agroecologicas.demo.repositorios.CultivoRepositorio;

/**
 *
 * @author User
 */
@Service
public class ProductorServicio {

    @Autowired
    private ProductorRepositorio productorRepositorio;

    @Autowired
    private HuertaRepositorio huertaRepositorio;

    @Autowired
    private CultivoRepositorio cultivoRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrarProductor(MultipartFile archivo, String nombreProductor, String dni,
            String direccion, String email, String password, String password2) throws MiException, Exception {

//        Optional<Huerta> respuestaHuerta = huertaRepositorio.findById(idHuerta);
//        Optional<Cultivo> respuestaCosecha = cultivoRepositorio.findById(idCultivo);

    //    if (respuestaHuerta.isPresent() && respuestaCosecha.isPresent()) {

            validar(archivo, nombreProductor, dni, direccion, email, password, password2);

//            Huerta huerta = respuestaHuerta.get();
//            Cultivo cultivo = respuestaCosecha.get();
//            List<Cultivo> cultivos = new ArrayList();

            Productor productor = new Productor();

            productor.setNombreProductor(nombreProductor);
            productor.setDni(dni);
            productor.setDireccion(direccion);
            productor.setEmail(email);
//            productor.setHuerta(huerta);
//            productor.setCultivos(cultivos);
            
            productor.setAltaBaja(Boolean.FALSE);
            productor.setFechaAlta(new Date());
            productor.setPassword(new BCryptPasswordEncoder().encode(password));
            productor.setRoles(Rol.PRO);

            if (archivo.getSize() == 0) {
                Imagen imagen = imagenServicio.obtenerImagenPorDefecto();
                productor.setImagen(imagen);
            } else {
                Imagen imagen = imagenServicio.guardar(archivo);
                productor.setImagen(imagen);
            }

            productorRepositorio.save(productor);

       // }

    }

    @Transactional
    public void modificarProductor(MultipartFile archivo, String idProductor,  String nombreProductor, String dni,
            String direccion) throws MiException, Exception {

        Optional<Productor> respuesta = productorRepositorio.findById(idProductor);

        if (idProductor == null || idProductor.isEmpty()) {
            throw new MiException("Debe ingrear un id del productor");

        }
        validarModificar(archivo, nombreProductor, dni, direccion);

        if (respuesta.isPresent()) {

            Productor productor = respuesta.get();

//            Optional<Huerta> respuestaHuerta = huertaRepositorio.findById(idHuerta);
//            Optional<Cultivo> respuestaCultivo = cultivoRepositorio.findById(idCultivo);

//            if (respuestaHuerta.isPresent()) {
//                Huerta huerta = respuestaHuerta.get();
//                productor.setHuerta(huerta);
//            }
//
//            if (respuestaCultivo.isPresent()) {
//                Cultivo cultivo = respuestaCultivo.get();
//                List<Cultivo> cultivos = new ArrayList();
//                 productor.setCultivos(cultivos);
//            }

            productor.setNombreProductor(nombreProductor);
            productor.setDni(dni);
            productor.setDireccion(direccion);

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = productor.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                productor.setImagen(imagen);
            }

            productorRepositorio.save(productor);

        }

    }

    @Transactional
    public Productor getOne(String idProductor) {
        return productorRepositorio.getOne(idProductor);
    }

    @Transactional
    public List<Productor> listarProductores() {
        List<Productor> productores = new ArrayList();
        productores = productorRepositorio.findAll();
        return productores;
    }
    
     public List<Productor> search(String termino, String estado,String orden) {

        List<Productor> productores = new ArrayList();
        productores = productorRepositorio. search(termino, estado,orden);
        return productores;
    }
    
    public List<Productor> search2(String estado,String orden) {

        List<Productor> productores = new ArrayList();
        productores = productorRepositorio. search2(estado,orden);
        return productores;
    }
    
    @Transactional
    public void darDeBajaProductor(String idProuctor) throws MiException{
        
       Optional<Productor> respuesta = productorRepositorio.findById(idProuctor);
       
       if(respuesta.isPresent()){
           Productor productor = respuesta.get();
           productor.setAltaBaja(Boolean.FALSE);
           productorRepositorio.save(productor);
       }
    }
    
    
     @Transactional
    public void darDeAltaProductor(String idProductor) throws MiException{
        
       Optional<Productor> respuesta = productorRepositorio.findById(idProductor);
       
       if(respuesta.isPresent()){
           Productor productor = respuesta.get();
           productor.setAltaBaja(Boolean.TRUE);
           productorRepositorio.save(productor);
       }
    }
    
    
    public List<Huerta> obtenerHuertasPorProductor(String idProductor){
        
        List<Huerta> huertas = new ArrayList();
        
        huertas = huertaRepositorio.huertasPorProductor(idProductor);
        
        return huertas;
        
    }
    
    public List<Cultivo> obtenerCultivosPorProductor(String idProductor){
        
        List<Cultivo> cultivos = new ArrayList();
        
        cultivos = cultivoRepositorio.buscarCultivoPorProductor(idProductor);
        
        return cultivos;
        
    }
    
    
    

    private void validar(MultipartFile archivo, String nombreProductor, 
            String dni, String direccion, String email, String password, String password2) throws MiException  {
       
        // Verificar si el email ya existe en la base de datos
        Productor usuarioExistente = productorRepositorio.buscarPorEmail(email);
        if (usuarioExistente != null) {
            throw new MiException("El email ingresado ya está registrado");
        }

        if (nombreProductor.isEmpty() || nombreProductor == null) {
            throw new MiException("Debe ingrear el nombre del Productor");
        }

        if (dni.isEmpty() || dni == null) {
            throw new MiException("Debe ingrear el dni del Productor");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("Debe ingrear la direccion del Productor");
        }
       

        if (email.isEmpty() || email == null) {
            throw new MiException("Debe debe ingresar email valido de su Productor");
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

    private void validarModificar(MultipartFile archivo, String nombreProductor, String dni, String direccion) throws MiException {
         if (nombreProductor.isEmpty() || nombreProductor == null) {
            throw new MiException("El nombre del productor no puede estar vacío.");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new MiException("La dirección no puede estar vacía.");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new MiException("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
        
    }
    
    

    

}
