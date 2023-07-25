/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Noticia;
import com.Huertas_agroecologicas.demo.entiddes.Publicacion;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.servicios.CultivoServicio;
import com.Huertas_agroecologicas.demo.servicios.HuertaServicio;
import com.Huertas_agroecologicas.demo.servicios.NoticiaServicio;
import com.Huertas_agroecologicas.demo.servicios.PublicacionServicio;
import com.Huertas_agroecologicas.demo.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private HuertaServicio huertaServicio;
    
    @Autowired
    private CultivoServicio cultivoServicio;
    
    @Autowired
    private PublicacionServicio publicacionServicio;
    
    @Autowired
    private NoticiaServicio noticiaServicio;
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id){
        
        Usuario usuario = usuarioServicio.getOne(id);
        
        byte[] imagen = usuario.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
        
    }
    
    
    @GetMapping("/huerta/{id}")
    public ResponseEntity<byte[]> imagenHuerta(@PathVariable String id){
        
        Huerta huerta = huertaServicio.getOne(id);
        
        
        byte[] imagen = huerta.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
        
    }
    
     @GetMapping("/cultivo/{id}")
    public ResponseEntity<byte[]> imagenCultivo(@PathVariable String id){
        
        Cultivo cultivo = cultivoServicio.getOne(id);
        
        
        byte[] imagen = cultivo.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
        
    }
    
     @GetMapping("/noticia/{id}")
    public ResponseEntity<byte[]> imagenNoticia(@PathVariable String id){
        
        Noticia noticia = noticiaServicio.getOne(id);
        
        
        byte[] imagen = noticia.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
        
    }
    
    
     @GetMapping("/publicacion/{id}")
    public ResponseEntity<byte[]> imagenPublicacion(@PathVariable String id){
        
        Publicacion publicacion = publicacionServicio.getOne(id);
        
        
        byte[] imagen = publicacion.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagen,headers,HttpStatus.OK);
        
    }
    
    
    
    
    
    
    
}
