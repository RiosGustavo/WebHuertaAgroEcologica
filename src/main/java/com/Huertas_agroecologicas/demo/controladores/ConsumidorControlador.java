/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Comentario;
import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Publicacion;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.servicios.ComentarioServicio;
import com.Huertas_agroecologicas.demo.servicios.ConsumidorServicio;
import com.Huertas_agroecologicas.demo.servicios.CultivoServicio;
import com.Huertas_agroecologicas.demo.servicios.HuertaServicio;
import com.Huertas_agroecologicas.demo.servicios.PublicacionServicio;
import com.Huertas_agroecologicas.demo.servicios.UsuarioServicio;
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
@RequestMapping("/consumidor")
public class ConsumidorControlador {
    
     @Autowired
    private UsuarioServicio usuarioServicio;
     
     @Autowired
     private ConsumidorServicio consumidorServicio;
     
     @Autowired
    private HuertaServicio huertaServicio;

    @Autowired
    private CultivoServicio cultivoServicio;
    
    @Autowired
    private PublicacionServicio publicacionServicio;
    
    @Autowired
    private ComentarioServicio comentarioServicio;
    
    
     @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_CON','ROLE_PRO')")
    @GetMapping("/perfil")
       public String perfil(ModelMap modelo, HttpSession session) {
       
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Consumidor consumidor = consumidorServicio.getOne(usuario.getId());
        modelo.addAttribute("consumidor", consumidor);
       
        return "perfil_usuario.html";
    }
       
       @PreAuthorize("hasAnyRole('ROLE_CON')")
    @PostMapping("/perfil/{id}")
    public String modificacionPerfil(MultipartFile archivo, @PathVariable String id, @RequestParam String NombreConsumidor,
                @RequestParam String dni, @RequestParam String direccion, ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Consumidor consumidor = consumidorServicio.getOne(usuario.getId());
        
        try {
            
            
            consumidorServicio.modificarConsumidor(archivo, id,  NombreConsumidor, dni, direccion);
                  
                    
            modelo.put("exito", "datos actualizados correctamente");
            modelo.addAttribute("consumidor", consumidor);
            
            return "perfil_usuario.html";
            
        } catch (Exception e) {
            modelo.addAttribute("consumidor", consumidor);
            modelo.put("error", e.getMessage());
            
            return "perfil_usuario.html";
        }

    }
    
     ////PANEL CONSUMIDOR
    @PreAuthorize("hasAnyRole('ROLE_CON')")
    @GetMapping("/panel-principal")
    public String panelConsumidor(ModelMap modelo, HttpSession session ){
        
        /// CONSUMIDOR
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = usuarioServicio.getOne(logueado.getId());
        Consumidor consumidor = consumidorServicio.getOne(logueado.getId());
        
        /// HUERTA DEL CONSUMIDOR
        List<Huerta> huertas  = huertaServicio.huertasPorConsumidor(logueado.getId()); /// obtenemos las huertas asociadas al consumidor


        // CULTIVOS DEL PRODUCTOR
        
        if(huertas != null){
            
        List<Cultivo> cultivos = cultivoServicio.cultivosPorConsumidor(logueado.getId());/// obtenemos los cultivos asociadas al consumidor
        modelo.addAttribute("cultivos", cultivos);  /// agregamos los cultivos al modelo
        }
        
        List<Publicacion> publicacionesPorCultivo = publicacionServicio.obtenerPublicacionesPorCultivosDeConsumidor(consumidor);
        List<Publicacion> publicacionesPorHuerta = publicacionServicio.obtenerPublicacionesPorHuertasDeConsumidor(consumidor);
        
        modelo.addAttribute("consumidor", consumidor);
        modelo.addAttribute("huertas", huertas); /// agregamos las huerta al modelo
        modelo.addAttribute("publicacionesPorCultivo", publicacionesPorCultivo); /// agregamos las publicaciones por cultivo al modelo
        modelo.addAttribute("publicacionesPorHuerta", publicacionesPorHuerta); /// agregamos las publicaciones por huerta al modelo
        
        return "panel_consumidor.html";
        
    }
    
    
     /// DAR DE BAJA AL CONSUMIDOR
    @GetMapping("/baja/{id}")
    public String darBajaConsumidor(@PathVariable String id){
        
        try {
            consumidorServicio.darDeBajaConsumidor(id);
                  
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/consumidores";
        
    }
    
     /// DAR DE ALTA AL CONSUMIDOR
    @GetMapping("/alta/{id}")
    public String darAltaConsumidor(@PathVariable String id){
        
        try {
            consumidorServicio.darDeAltaConsumidor(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/consumidores";
        
    }
    
    @GetMapping("/listar")
    public String listar() throws Exception {
        consumidorServicio.listarConsumidores();
        
        return "publicacion_list.html";
    }

    @GetMapping("/{id}")
    public String perfilPublicoVotante(@PathVariable String id,ModelMap modelo, HttpSession session) {
        
        Consumidor consumidor = consumidorServicio.getOne(id);
        List<Publicacion> publicacionesHuerta = publicacionServicio.obtenerPublicacionesPorHuertasDeConsumidor(consumidor);
        List<Publicacion> publicacionesCultivo = publicacionServicio.obtenerPublicacionesPorCultivosDeConsumidor(consumidor);
                      
        List<Comentario> comentarios = comentarioServicio.comentariosPorConsumidor(consumidor);
                
        modelo.addAttribute("consumidor", consumidor);
        modelo.addAttribute("publicacionesHuerta", publicacionesHuerta);
        modelo.addAttribute("publicacionesCultivo", publicacionesCultivo);
        modelo.addAttribute("comentarios", comentarios);
        return "perfil_publico_consumidor.html";
    }
    
    
}
