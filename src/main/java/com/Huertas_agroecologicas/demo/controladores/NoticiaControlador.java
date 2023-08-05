/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Noticia;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.repositorios.NoticiaRepositorio;
import com.Huertas_agroecologicas.demo.servicios.NoticiaServicio;
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
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @GetMapping("noticia_one/{idNoticia}")
    public String id(@PathVariable String idNoticia, ModelMap modelo) {

        modelo.put("noticia", noticiaServicio.getOne(idNoticia));

        return "noticia_one.html";
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_CON')")
    @GetMapping("/crear")
    public String crear(HttpSession session){
        if(session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja() ){
            return"redirect:/blogger/panel-principal";
        }
        return "noticia_form.html";
    }
    
    @PostMapping("/creacion")
    public String creacion(@RequestParam String titulo, @RequestParam String descripcion, @RequestParam String cuerpo, @RequestParam MultipartFile archivo,
       @RequestParam String video, ModelMap modelo, HttpSession session){
        
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            noticiaServicio.crearNoticia(archivo, titulo, descripcion, cuerpo, cuerpo, video);
            modelo.put("exito", "noticia creada correctamente");
        } catch (Exception e) {
            
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("titulo", titulo);
            modelo.addAttribute("descripcion", descripcion);
            modelo.addAttribute("cuerpo", cuerpo);
            modelo.addAttribute("video", video);
        }
        return "noticia_form.html";
        
    }
    
    @GetMapping("/modificar/{idNoticia}")
    public String modificar(@PathVariable String idNoticia, ModelMap modelo){
        modelo.put("hoticia", noticiaServicio.getOne(idNoticia));
        return "noticia_modificar.html";
        
    }
    
    @PostMapping("/modificar/{idNoticia}")
    public String modificacion(@PathVariable String idNoticia, MultipartFile archivo,
            String titulo, String descripcion, String cuerpo, String video, ModelMap modelo){
        
        try {
            noticiaServicio.modificarNoticia(archivo, idNoticia, titulo, descripcion, cuerpo, cuerpo);
            modelo.put("exito", "La Noticiase ha modificdo exitosamente");
            
            return "redirect:/noticia/lista";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("noticia", noticiaServicio.getOne(idNoticia));
            return "noticia_modificar.html";
        }
        
    }
    
    @GetMapping("/eliminar/{idNoticia}")
    public String eliminar(@RequestParam @PathVariable String idNoticia) throws Exception{
        noticiaServicio.bajaNoticia(idNoticia);
        
        return "index.html";
        
    }
    
    
    
    
    
    
    
    
    

    @GetMapping("/lista")
    public String listadoNoticias(@RequestParam(required = false) String termino, ModelMap modelo, HttpSession session) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        List<Noticia> noticias = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {

            noticias = noticiaServicio.BuscarNoticiaPorTitulo(termino.toLowerCase());

            if (noticias.isEmpty()) {

                noticias = noticiaRepositorio.findAllOrderByfecha_altaDesc();
                modelo.put("error", "No se encontró nada con el término ingresado, Intente de otra manera");
            }

        } else {

            noticias = noticiaRepositorio.findAllOrderByfecha_altaDesc();
        }

        modelo.addAttribute("noticias", noticias);

        return "noticia_list.html";

    }

}
