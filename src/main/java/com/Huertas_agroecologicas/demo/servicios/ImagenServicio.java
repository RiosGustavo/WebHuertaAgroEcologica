/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.ImagenRepositorio;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class ImagenServicio {
    
     @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guardar(MultipartFile archivo) throws Exception {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
    /// con este metodo podemos actulaizar la imagen  del usuario 

    public Imagen actualizar(MultipartFile archivo, String id) throws Exception, IOException {
        if (archivo != null) {
            try {
                Imagen imagen = new Imagen();

                if (id != null) {
                    Optional<Imagen> respuesta = imagenRepositorio.findById(id);

                    if (respuesta.isPresent()) {
                        imagen = respuesta.get();
                    }

                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                 System.err.println(e.getMessage());
               
            }
        }
        return null;
    }

    
    //// con este metodo ponemos una imagen por defecto si el usuario no eleige una al registrarse
    public Imagen obtenerImagenPorDefecto() throws IOException {
        // Ruta relativa a la carpeta 'static/img' donde se encuentra la imagen por defecto
        String rutaImagenPorDefecto = "static/img/logo.png"; // Actualiza con el nombre y extensión de tu imagen por defecto

        Resource resource = new ClassPathResource(rutaImagenPorDefecto);
        byte[] contenido = Files.readAllBytes(resource.getFile().toPath());

        Imagen imagen = new Imagen();
        imagen.setMime("image/png"); // Actualiza con el MIME type correspondiente a tu imagen por defecto
        imagen.setNombre("logo.png"); // Actualiza con el nombre de tu imagen por defecto
        imagen.setContenido(contenido);

        return imagenRepositorio.save(imagen);
    }

    
}
