/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Publicacion;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.servicios.CultivoServicio;
import com.Huertas_agroecologicas.demo.servicios.HuertaServicio;
import com.Huertas_agroecologicas.demo.servicios.PublicacionServicio;
import com.Huertas_agroecologicas.demo.servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/publicacion")
public class PublicacionControlador {

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private HuertaServicio huertaServicio;
    
    @Autowired
    private CultivoServicio cultivoServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    

   

  

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_PRO','ROLE_CON')")
    @GetMapping("huerta/{idHuerta}/crearPublicacion")
    public String registrarPublicacionHuerta(@PathVariable String idHuerta, ModelMap modelo, HttpSession session) {
        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {
            return "redirect:/huerta/panel-principal";
        }
        modelo.put("huerta", huertaServicio.getOne(idHuerta));
                
        return "publicacion_form.html";
    }

    @PostMapping("huerta/{idHuerta}/creacion")
    public String creacionPublicacionHuerta(MultipartFile archivo,  @RequestParam() String titulo, @RequestParam() String descripcion,
            @RequestParam() String cuerpo, @RequestParam() String video, 
            @PathVariable String idHuerta, @PathVariable String idCultivo,    ModelMap modelo, HttpSession session) {
    
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            publicacionServicio.crearPublicacion(archivo, titulo, descripcion, cuerpo, video, logueado, idHuerta, idCultivo);
                    
                    
            modelo.put("exito", "Publicacion creada exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("huerta", huertaServicio.getOne(idHuerta));
                    
            modelo.put("cultivo", cultivoServicio.getOne(idCultivo));
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("cuerpo", cuerpo);
            modelo.put("archivo", archivo);
            modelo.put("video", video);
            return "publicacion_form.html";
        }
        return "redirect:/huerta/huerta_one/{idHuerta}";
    }
    
    ///***********************************************************************************************
    
    
    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_PRO','ROLE_CON')")
    @GetMapping("cultivo/{idCultivo}/crearPublicacion")
    public String registrarPublicacionCultivo(@PathVariable String idCultivo, ModelMap modelo, HttpSession session) {
        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {
            return "redirect:/cultivo/panel-principal";
        }
        modelo.put("cultivo", cultivoServicio.getOne(idCultivo));
                
        return "publicacion_form.html";
    }

    @PostMapping("cultivo/{idCultivo}/creacion")
    public String creacionPublicacionCultivo(MultipartFile archivo,  @RequestParam() String titulo, @RequestParam() String descripcion,
            @RequestParam() String cuerpo, @RequestParam() String video, 
            @PathVariable String idHuerta, @PathVariable String idCultivo,    ModelMap modelo, HttpSession session) {
    
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            publicacionServicio.crearPublicacion(archivo, titulo, descripcion, cuerpo, video, logueado, idHuerta, idCultivo);
                    
                    
            modelo.put("exito", "Publicacion creada exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("huerta", huertaServicio.getOne(idHuerta));
                    
            modelo.put("cultivo", cultivoServicio.getOne(idCultivo));
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("cuerpo", cuerpo);
            modelo.put("archivo", archivo);
            modelo.put("video", video);
            return "publicacion_form.html";
        }
        return "redirect:/cultivo/cultivo_one/{idCultivo}";
    }
    
    
    
    
    
    ///*************************************************************************************************

    @GetMapping("/modificar/{idPublicacion}")
    public String modificar(@PathVariable String idPublicacion, ModelMap modelo) {
        modelo.put("publicacion", publicacionServicio.getOne(idPublicacion));
        return "publicacion_modificar.html";
    }

    @PostMapping("/modificar/{idPublicacion}")
    public String modificacion(@PathVariable String idPublicacion, @RequestParam(required = false) MultipartFile archivo,
            @RequestParam(required = false) String titulo, @RequestParam(required = false) String descripcion, 
            @RequestParam(required = false) String cuerpo, @RequestParam(required = false) String video, ModelMap modelo) {
        try {
            publicacionServicio.modificarPublicacion(archivo, idPublicacion, titulo, descripcion, cuerpo, video);
            modelo.put("exito", "La publicacion se ha modificado exitosamente!");
        } catch (Exception ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("publicacion", publicacionServicio.getOne(idPublicacion));
            return "publicacion_modificar.html";
        }
        return "redirect:/publicacion/lista";
    }

    @GetMapping("/baja/{idPublicacion}")
    public String darDeBajaPublicacion(@PathVariable String idPublicacion) throws Exception {
        publicacionServicio.darDeBajaPublicacion(idPublicacion);
        return "redirect:/publicacion/lista";
    }

    @GetMapping("/alta/{idPublicacion}")
    public String darDeAltaPublicacion(@PathVariable String idPublicacion) throws Exception {
        publicacionServicio.darDeAltaPublicacion(idPublicacion);
        return "redirect:/publicacion/lista";
    }

    //-----------------------------MOTOR BUSQUEDA---------------------------------
    @GetMapping("/lista")
    public String listadoPublicaciones(@RequestParam(required = false) String termino, 
            @RequestParam(required = false) String estado, @RequestParam(required = false) String orden, ModelMap modelo, HttpSession session) {

        List<Publicacion> publicaciones = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            publicaciones = publicacionServicio.search(termino, estado, orden);

        } else {
            publicaciones = publicacionServicio.search2(estado, orden);
        }
        
         // Agregar las dos líneas de código para obtener las publicaciones relacionadas con huertas y cultivos
    Usuario consumidor = (Usuario) session.getAttribute("usuariosession");
    List<Publicacion> publicacionesHuertas = publicacionServicio.obtenerPublicacionesPorHuertasDeConsumidor((Consumidor) consumidor);
    List<Publicacion> publicacionesCultivos = publicacionServicio.obtenerPublicacionesPorCultivosDeConsumidor((Consumidor) consumidor);

        
        

        if (publicaciones.isEmpty()) {
            publicaciones = publicaciones = publicacionServicio.publicacionPorFecha();
            modelo.put("error", "No se encontró nada con el término ingresado. Intente de otra manera.");
        }

        modelo.put("termino", termino);
        modelo.put("estado", estado);
        modelo.put("orden", orden);
        modelo.addAttribute("publicaciones", publicaciones);

        return "publicacion_list.html";
    }

}
