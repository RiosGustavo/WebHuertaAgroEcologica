/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Productor;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.servicios.CultivoServicio;
import com.Huertas_agroecologicas.demo.servicios.HuertaServicio;
import com.Huertas_agroecologicas.demo.servicios.ProductorServicio;
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
@RequestMapping("/productor")
public class ProductorControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProductorServicio productorServicio;

    @Autowired
    private HuertaServicio huertaServicio;

    @Autowired
    private CultivoServicio cultivoServicio;

    @PreAuthorize("hasAnyRole('ROLE_PRO')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
       
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Productor productor = productorServicio.getOne(usuario.getId());
        modelo.addAttribute("productor", productor);
       
        return "perfil_productor.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PRO')")
    @PostMapping("/perfil/{id}")
    public String modificacionPerfil(MultipartFile archivo, @PathVariable String id, @RequestParam String NombreProductor,
            @RequestParam String dni, @RequestParam String direccion, ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Productor productor = productorServicio.getOne(usuario.getId());
        
        try {
            productorServicio.modificarProductor(archivo, id, NombreProductor, dni, direccion);
            modelo.put("exito", "datos actualizados correctamente");
            modelo.addAttribute("productor", productor);
            
            return "perfil_productor.html";
        } catch (Exception e) {
            modelo.addAttribute("productor", productor);
            modelo.put("error", e.getMessage());
            
            return "perfil_productor.html";
        }

    }
    
    ////PANEL PRODUCTOR
    @PreAuthorize("hasAnyRole('ROLE_PRO')")
    @GetMapping("/panel-principal")
    public String panelProductor(ModelMap modelo, HttpSession session ){
        
        /// PRODUCTOR
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = usuarioServicio.getOne(logueado.getId());
        Productor productor = productorServicio.getOne(logueado.getId());
        
        /// HUERTA DEL PROCUTOR
        Huerta huerta =productor.getHuerta(); /// obtenemos la huerta asociada al productor


        /// CULTIVOS DEL PRODUCTOR
        
        if(huerta != null){
            
        List<Cultivo> cultivos = cultivoServicio.cultivosPorHuerta(huerta.getIdHuerta());
        modelo.addAttribute("cultivos", cultivos);
        }
        modelo.addAttribute("productor", productor);
        modelo.addAttribute("huerta", huerta); /// agregamos al huerta al modelo
        
        return "panel_productor.html";
        
    }
    
    /// DAR DE BAJA AL PRODUCTOR
    @GetMapping("/baja/{id}")
    public String darBajaProductor(@PathVariable String id){
        
        try {
            productorServicio.darDeBajaProductor(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/productores";
        
    }
    
     /// DAR DE ALTA AL PRODUCTOR
    @GetMapping("/alta/{id}")
    public String darAltaProductor(@PathVariable String id){
        
        try {
            productorServicio.darDeAltaProductor(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/productores";
        
    }
    
    

}
