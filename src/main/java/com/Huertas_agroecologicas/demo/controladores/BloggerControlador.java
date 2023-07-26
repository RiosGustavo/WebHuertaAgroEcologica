/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Blogger;
import com.Huertas_agroecologicas.demo.entiddes.Noticia;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.servicios.BloggerServicio;
import com.Huertas_agroecologicas.demo.servicios.NoticiaServicio;
import com.Huertas_agroecologicas.demo.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/blogger")
public class BloggerControlador {
    
    @Autowired
    private BloggerServicio bloggerServicio;
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private NoticiaServicio  noticiaServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_ADM', 'ROLE_CON','ROLE_BLOG','ROLE_PRO')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Blogger blogger = bloggerServicio.getOne(usuario.getId());
        modelo.addAttribute("blogger", blogger);
        
        
        return "perfil_blogger.html";
    }
    
    
    @PreAuthorize("hasAnyRole('ROLE_ADM', 'ROLE_CON','ROLE_BLOG','ROLE_PRO')")
    @GetMapping("/perfil/{id}")
    public String modificacionPerfil(MultipartFile archivo, @PathVariable String id,
            @RequestParam String nombreApellido, @RequestParam String direccion, ModelMap modelo, HttpSession session){
        
         Usuario usuario = (Usuario) session.getAttribute("usuariosession");
         Blogger blogger = bloggerServicio.getOne(usuario.getId());
         
         try {
              bloggerServicio.modificarBlogger(archivo, id, nombreApellido, direccion);
              modelo.put("exito", "Blogger actulizado correctamente");
              modelo.addAttribute("blogger", blogger);
              
              return "perfil_blogger.html";
        } catch (Exception e) {
            modelo.addAttribute("blogger", blogger);
            modelo.put("error", e.getMessage());
            
            return "perfil_blogger.html";
        }
        
        
        
    }
    
    ///PANEL BLOGGER
    @PreAuthorize("hasAnyRole('ROLE_BLOG')")
    @GetMapping("panel-principal")
    public String panelBlogger(ModelMap modelo, HttpSession session){
        /// CONSUMIDOR
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        Usuario usuario = usuarioServicio.getOne(logueado.getId());
        Blogger blogger = bloggerServicio.getOne(logueado.getId());
        
        List<Noticia> noticias = noticiaServicio.noticiasPorBlogger(logueado.getId());
        
        modelo.addAttribute("blogger", blogger);
        modelo.addAttribute("nocticias", noticias);
        
        return "panel_bloguer.html";
        
    }
    
    // Dar de baja un Blogger
    @GetMapping("baja/{id}")
    public String darBaja(@PathVariable String id) throws Exception{
        bloggerServicio.darDeBajaBlogger(id);
        
        return "redirect:/admin/bloggers";
    }
    
    // Dar de Alta un Blogger
    @GetMapping("alta/{id}")
    public String darAlta(@PathVariable String id) throws Exception{
        bloggerServicio.darDeAltaBlogger(id);
        
        return "redirect:/admin/bloggers";
    }
    
}
