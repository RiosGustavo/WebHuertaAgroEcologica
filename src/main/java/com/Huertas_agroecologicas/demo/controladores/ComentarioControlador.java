/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.servicios.ComentarioServicio;
import com.Huertas_agroecologicas.demo.servicios.CultivoServicio;
import com.Huertas_agroecologicas.demo.servicios.HuertaServicio;
import com.Huertas_agroecologicas.demo.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/comentario")
public class ComentarioControlador {
    
    @Autowired
    private ComentarioServicio comentarioServicio;
    
   @Autowired
    private HuertaServicio huertaServicio;
    
    @Autowired
    private CultivoServicio cultivoServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @PostMapping("/huerta/{idHuerta}/comentar")
    public String comentarHuerta(@PathVariable String idHuerta, @PathVariable String idCultivo, @RequestParam() String mensaje, ModelMap modelo, HttpSession session) throws Exception{
        
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            
            comentarioServicio.crearComentario(idHuerta, idCultivo, logueado.getId(), mensaje);
            modelo.put("exito", "Comentario creado exitosamente!");
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("huerta", huertaServicio.getOne(idHuerta));
            modelo.put("cultivo", cultivoServicio.getOne(idCultivo));
            modelo.put("mensaje", mensaje);
        }
        
        return "redirect:/huerta/huerta_one/{idHuerta}#comentarios";
        
    }
    
    
     @PostMapping("/cultivo/{idCultivo}/comentar")
    public String comentarCultivo(@PathVariable String idHuerta, @PathVariable String idCultivo, @RequestParam() String mensaje, ModelMap modelo, HttpSession session) throws Exception{
        
        try {
            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            
            comentarioServicio.crearComentario(idHuerta, idCultivo, logueado.getId(), mensaje);
            modelo.put("exito", "Comentario creado exitosamente!");
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("huerta", huertaServicio.getOne(idHuerta));
            modelo.put("cultivo", cultivoServicio.getOne(idCultivo));
            modelo.put("mensaje", mensaje);
        }
        
        return "redirect:/cultivo/cultivo_one/{idCultivo}#comentarios";
        
    }
    ///dar de baja comentario en huertas
    
    @GetMapping("/{idHuerta}/baja/{idComentario}")
    public String bajaComentarioHuerta(@PathVariable String idComentario ,@PathVariable String idHuerta) throws Exception{
        comentarioServicio.darDeBajaComentario(idComentario);
        
        return "redirect:/huerta/huerta_one/{idHuerta}#comentarios";
        
    }
    
    ///dar de baja comentario en cultivos
    
    @GetMapping("/{idCultivo}/baja/{idComentario}")
    public String bajaComentarioCultivo(@PathVariable String idComentario ,@PathVariable String idCultivo) throws Exception{
        comentarioServicio.darDeBajaComentario(idComentario);
        
        return "redirect:/cultivo/cultivo_one/{idCultivo}#comentarios";
        
    }
    
    
    
    
    
    
    
}
