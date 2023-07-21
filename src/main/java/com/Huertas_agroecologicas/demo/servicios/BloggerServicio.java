/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Blogger;
import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.enumeraciones.Rol;
import com.Huertas_agroecologicas.demo.repositorios.BloggerRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.ConsumidorRepositorio;
import java.util.ArrayList;
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
public class BloggerServicio {

    @Autowired
    private BloggerRepositorio bloggerRepositorio;

    @Autowired
    private ConsumidorRepositorio consumidorRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public Blogger crearBlogger(String id) {

        Consumidor consumidor = consumidorRepositorio.findById(id).get();

        Blogger blog = new Blogger();

        blog.setNombreApellido(consumidor.getNombreConsumidor());
        blog.setDni(consumidor.getDni());
        blog.setDireccion(consumidor.getDireccion());
        blog.setEmail(consumidor.getEmail());
        blog.setFechaAlta(new Date());
        blog.setAltaBaja(Boolean.TRUE);
        blog.setPassword(consumidor.getPassword());
        blog.setRoles(Rol.BLOG);
        blog.setImagen(consumidor.getImagen());

        bloggerRepositorio.save(blog);
        consumidorRepositorio.deleteById(id);

        return blog;

    }

    
    @Transactional
    public void modificarBlogger(MultipartFile archivo, String id, String nombreApellido, String direccion)
            throws Exception {

        validarModificar(archivo, nombreApellido, direccion);
        Optional<Blogger> respuesta = bloggerRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Blogger blogger = respuesta.get();

            blogger.setNombreApellido(nombreApellido);
            blogger.setDireccion(direccion);

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = blogger.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                blogger.setImagen(imagen);
            }
            bloggerRepositorio.save(blogger);
        }
    }

    @Transactional
    public void darDeBajaBlogger(String id) throws Exception {

        Optional<Blogger> respuesta = bloggerRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Blogger blogger = respuesta.get();
            blogger.setAltaBaja(false);

            bloggerRepositorio.save(blogger);
        }
    }

    @Transactional
    public void darDeAltaBlogger(String id) throws Exception {

        Optional<Blogger> respuesta = bloggerRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Blogger blogger = respuesta.get();
            blogger.setAltaBaja(true);

            bloggerRepositorio.save(blogger);
        }
    }

    public Blogger getOne(String id) {
        return bloggerRepositorio.getOne(id);
    }

    public List<Blogger> search(String termino, String estado, String orden) {

        List<Blogger> bloggers = new ArrayList();
        bloggers = bloggerRepositorio.search(termino, estado, orden);
        return bloggers;
    }

    public List<Blogger> search2(String estado, String orden) {

        List<Blogger> bloggers = new ArrayList();
        bloggers = bloggerRepositorio.search2(estado, orden);
        return bloggers;
    }

    public List<Blogger> listarBloggers() {
        List<Blogger> bloggers = new ArrayList();
        bloggers = bloggerRepositorio.findAll();
        return bloggers;
    }

    private void validarModificar(MultipartFile archivo, String nombreApellido, String direccion) throws Exception {

        if (nombreApellido.isEmpty() || nombreApellido == null) {
            throw new Exception("El nombre y apellido no puede estar vacío.");
        }

        if (direccion.isEmpty() || direccion == null) {
            throw new Exception("La dirección no puede estar vacía.");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
    }
    
}
