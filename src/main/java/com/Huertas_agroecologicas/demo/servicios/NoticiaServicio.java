/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Blogger;
import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.entiddes.Noticia;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.ImagenRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.NoticiaRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class NoticiaServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearNoticia(MultipartFile archivo, String titulo, String descripcion, String cuerpo, String youtubeUrl, String idBlogger) throws MiException, Exception {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idBlogger);

        validar(titulo, descripcion, cuerpo);

        Noticia noticia = new Noticia();
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticia.setDescripcion(descripcion);
        noticia.setFechaAlta(new Date());
        noticia.setAltaBaja(Boolean.TRUE);

        Imagen imagen = imagenServicio.guardar(archivo);

        noticia.setImagen(imagen);

        /// ACA IMPLEMENTACMOS OBTENER EL ID DEL VIDEO DE YOUTUBE EL CUAL SERA AGREGADO A LA NOTICIA
        if (youtubeUrl != null) {
            noticia.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
        }

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRoles().toString().equalsIgnoreCase("BLOG")) {
                Blogger blo = (Blogger) respuesta.get();
                noticia.setBlogger(blo);
            }

        }

        noticiaRepositorio.save(noticia);

    }

    /// CON ESTE METODO OBTENEMOS EL IDENTIFICADOR DEL VIDEO DE YOUTUBE EL CUAL QUEREMOS AGREGAR A LA NOTICIA
    private String getEmbeddedYouTubeUrl(String youtubeUrl) {
        // Patron para buscar el identificador del video en la URL
        Pattern pattern = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*");

        // Buscar el identificador del video en la URL
        Matcher matcher = pattern.matcher(youtubeUrl);
        if (matcher.find()) {
            String videoId = matcher.group();

            // Construir la URL de YouTube con el identificador del video en formato embed
            return "https://www.youtube.com/embed/" + videoId;
        } else {
            // Si no se encuentra el identificador del video en la URL, devolvemos null
            return null;
        }
    }

    @Transactional
    public void modificarNoticia(MultipartFile archivo, String idNoticia, String titulo,
            String descripcion, String cuerpo, String youtubeUrl) throws MiException, Exception {

        validar(titulo, descripcion, cuerpo);

        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setTitulo(titulo);
            noticia.setDescripcion(descripcion);
            noticia.setCuerpo(cuerpo);

            /// verificamos que si exista un URL del video
            if (youtubeUrl != null) {
                noticia.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
            }

            String idImagen = null;

            if (archivo.getSize() > 0) {
                idImagen = noticia.getImagen().getId();

                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                noticia.setImagen(imagen);
            }

            noticiaRepositorio.save(noticia);

        }

    }

    @Transactional
    public void eliminarNoticia(String idNoticia) throws MiException {
        noticiaRepositorio.deleteById(idNoticia);
    }

    public Noticia getOne(String idNoticia) {
        return noticiaRepositorio.getOne(idNoticia);
    }

    @Transactional
    public List<Noticia> listarNoticias() {
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.findAll();
        return noticias;
    }

    @Transactional
    public List<Noticia> noticiasPorBlogger(String idBlogger) {
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.noticiasPorBlogger(idBlogger);
        return noticias;
    }

    @Transactional
    public void bajaNoticia(String idNoticia) throws Exception {
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);

        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setAltaBaja(false);
            noticiaRepositorio.save(noticia);
        }
    }

    public List<Noticia> BuscarNoticiaPorTitulo(String titulo) {
        List<Noticia> noticias = new ArrayList();
        noticias = noticiaRepositorio.buscarPorTitulo(titulo);
        return noticias;
    }

    private void validar(String titulo, String descripcion, String cuerpo) throws MiException {
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("El Título no puede ser nulo o estar vacío");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new MiException("La Descripcion no puede ser nula o estar vacía");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MiException("El cuerpo no puede ser nulo o estar vacío");
        }
    }

}
