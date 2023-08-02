/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.entiddes.Publicacion;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.ConsumidorRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.HuertaRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.ImagenRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.PublicacionRepositorio;
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
import com.Huertas_agroecologicas.demo.repositorios.CultivoRepositorio;
import com.Huertas_agroecologicas.demo.repositorios.UsuarioRepositorio;

/**
 *
 * @author User
 */
@Service
public class PublicacionServicio {

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private HuertaRepositorio huertaRepositorio;

    @Autowired
    private CultivoRepositorio cultivohaRepositorio;

    @Autowired
    private ConsumidorRepositorio consumidorRepositorio;

    @Transactional
    public void crearPublicacion(MultipartFile archivo, String titulo, String descripcion, String cuerpo,
            String youtubeUrl, Usuario usuario, String idHuerta, String idCultivo) throws MiException, Exception {

        if (usuario.getRoles().equals("ROLE_ADM") || usuario.getRoles().equals("ROLE_PRO") || usuario.getRoles().equals("ROLE_CON")) {

            Optional<Huerta> respuestaHuerta = huertaRepositorio.findById(idHuerta);
            Optional<Cultivo> respuestaCultivo = cultivohaRepositorio.findById(idCultivo);
            Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(usuario.getId());

            Publicacion publicacion = new Publicacion();

            publicacion.setTitulo(titulo);
            publicacion.setDescripcion(descripcion);
            publicacion.setCuerpo(cuerpo);
            publicacion.setFechaAlta(new Date());
            publicacion.setAltaBaja(Boolean.TRUE);

            Imagen imagen = imagenServicio.guardar(archivo);

            publicacion.setImagen(imagen);

            /// ACA IMPLEMENTACMOS OBTENER EL ID DEL VIDEO DE YOUTUBE EL CUAL SERA AGREGADO A LA PUBLICACION
            if (youtubeUrl != null) {
                publicacion.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
            }

            if (respuestaHuerta.isPresent()) {
                publicacion.setHuerta(respuestaHuerta.get());
            }
            if (respuestaCultivo.isPresent()) {
                publicacion.setCultivo(respuestaCultivo.get());
            }

            if (respuestaUsuario.isPresent()) {
                publicacion.setUsuario(usuario);

            }

            publicacionRepositorio.save(publicacion);
        }
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
    public void modificarPublicacion(MultipartFile archivo, String idPublicacion, String titulo, String descripcion, String cuerpo, String youtubeUrl) throws Exception {

        validar(archivo, titulo, descripcion, cuerpo);

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setTitulo(titulo);
            publicacion.setDescripcion(descripcion);
            publicacion.setCuerpo(cuerpo);
            if (youtubeUrl != null) {
                publicacion.setVideo(getEmbeddedYouTubeUrl(youtubeUrl));
            }

            String idImagen = null;
            if (archivo.getSize() > 0) {
                idImagen = publicacion.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                publicacion.setImagen(imagen);
            }
            publicacionRepositorio.save(publicacion);
        }

    }

    public Publicacion getOne(String idPublicacion) {
        return publicacionRepositorio.getOne(idPublicacion);
    }

    @Transactional
    public List<Publicacion> publicacionesPorHuerta(String idHuerta) {

        List<Publicacion> publicaciones = new ArrayList();
        publicaciones = publicacionRepositorio.buscarPorHuerta(idHuerta);
        return publicaciones;
    }

    @Transactional
    public List<Publicacion> publicacionesCultivo(String idCultivo) {

        List<Publicacion> publicaciones = new ArrayList();
        publicaciones = publicacionRepositorio.buscarPorCultivo(idCultivo);
        return publicaciones;
    }

    @Transactional
    public List<Publicacion> publicacionesPorHuertaActivas(String idHuerta) {
        List<Publicacion> publicaciones = new ArrayList();

        publicaciones = publicacionRepositorio.publicacionesActivasPorHuerta(idHuerta);

        return publicaciones;
    }

    @Transactional
    public List<Publicacion> publicacionesPorCultivoActivas(String idCultivo) {
        List<Publicacion> publicaciones = new ArrayList();

        publicaciones = publicacionRepositorio.publicacionesActivasPorCultivo(idCultivo);

        return publicaciones;
    }

    @Transactional
    public List<Publicacion> listarPublicacionesActivas() {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.listadoPublicacionesActivas();
        return publicaciones;
    }

    @Transactional
    public List<Publicacion> listarPublicaciones() {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.findAll();
        return publicaciones;
    }

    @Transactional
    public List<Publicacion> publicacionPorTitulo(String titulo) {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.buscarPorTitulo(titulo);
        return publicaciones;
    }

    @Transactional
    public List<Publicacion> publicacionPorFecha() {
        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.findAllOrderByfecha_altaDesc();
        return publicaciones;
    }

    @Transactional
    public List<Publicacion> obtenerPublicacionesPorHuertasDeConsumidor(Consumidor consumidor) {
        return publicacionRepositorio.buscarPorHuerta(consumidor.getId());

    }

    @Transactional
    public List<Publicacion> obtenerPublicacionesPorCultivosDeConsumidor(Consumidor consumidor) {
        return publicacionRepositorio.buscarPorCultivo(consumidor.getId());
    }

    ///////////////////////////////////////}
    public List<Publicacion> search(String termino, String estado, String orden) {

        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.search(termino, estado, orden);
        return publicaciones;
    }

    public List<Publicacion> search2(String estado, String orden) {

        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.search2(estado, orden);
        return publicaciones;
    }

    public List<Publicacion> search3(String termino, String estado, String orden) {

        List<Publicacion> publicaciones = new ArrayList<>();
        publicaciones = publicacionRepositorio.search3(termino, estado, orden);
        return publicaciones;
    }

    //////////////////////////////////////////
    @Transactional
    public void darDeBajaPublicacion(String idPublicacion) throws Exception {

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setAltaBaja(false);
            publicacionRepositorio.save(publicacion);
        }

    }

    @Transactional
    public void darDeAltaPublicacion(String idPublicacion) throws Exception {

        Optional<Publicacion> respuesta = publicacionRepositorio.findById(idPublicacion);

        if (respuesta.isPresent()) {
            Publicacion publicacion = respuesta.get();
            publicacion.setAltaBaja(true);
            publicacionRepositorio.save(publicacion);
        }

    }

    private void validar(MultipartFile archivo, String titulo, String descripcion, String cuerpo) throws Exception {
        if (titulo.isEmpty() || titulo == null) {
            throw new Exception("El título no puede estar vacío.");
        }
        if (descripcion.isEmpty() || descripcion == null) {
            throw new Exception("La descripcion no puede estar vacía");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new Exception("El cuerpo no puede estar vacío");
        }
        if (archivo.getSize() > 10 * 1024 * 1024) { // 10 MB en bytes
            throw new Exception("El archivo es demasiado grande. Por favor, seleccione una imagen de menos de 10 MB");
        }
    }

}
