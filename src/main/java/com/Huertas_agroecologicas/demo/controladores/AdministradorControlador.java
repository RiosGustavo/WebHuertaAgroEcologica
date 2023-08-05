/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Blogger;
import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Productor;
import com.Huertas_agroecologicas.demo.servicios.BloggerServicio;
import com.Huertas_agroecologicas.demo.servicios.ConsumidorServicio;
import com.Huertas_agroecologicas.demo.servicios.ProductorServicio;
import com.Huertas_agroecologicas.demo.servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/admin")
public class AdministradorControlador {

    @Autowired
    private ProductorServicio productorServicio = new ProductorServicio();

    @Autowired
    private ConsumidorServicio consumidorServicio = new ConsumidorServicio();

    @Autowired
    private BloggerServicio bloggerServicio = new BloggerServicio();

    @Autowired
    private UsuarioServicio usuarioServicio = new UsuarioServicio();

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel_admin.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@RequestParam @PathVariable String id) throws Exception {
        usuarioServicio.cambiarEstado(id);
        return "index.html";
    }

    /// CAMBIAR UN USUARIO A BLOGGER
    @GetMapping("rolBlogger/{id}")
    public String rolBlogger(@PathVariable String id) {
        bloggerServicio.crearBlogger(id);
        return "redirect:/admin/bloggers";
    }

    /// CAMBIAR BLOGGER A CONSUMIDOR
    @GetMapping("/rolConsumidor/{id}")
    public String bloggerAconsumidor(@PathVariable String id) {
        bloggerServicio.bloggerAconsumidor(id);
        return "redirect:/admin/consumidores";
    }

    // ************************ MOTOR DE BUSQUEDA *****************/
    @GetMapping("/consumidores")
    public String busquedaConsumidores(@RequestParam(required = false) String termino,
            @RequestParam(required = false) String estado, @RequestParam(required = false) String orden,
            ModelMap modelo) {

        List<Consumidor> consumidores = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {

            consumidores = consumidorServicio.search(termino, estado, orden);
        } else {
            consumidores = consumidorServicio.search2(estado, orden);
        }

        if (consumidores.isEmpty()) {
            consumidores = consumidorServicio.listarConsumidores();
            modelo.put("error", "No se encontro consumidor con el término ingresado. Intente de otra forma");
        }
        modelo.put("termino", termino);
        modelo.put("estado", estado);
        modelo.put("orden", orden);

        modelo.addAttribute("consumidores", consumidores);

        return "consumidor_list.html";

    }

    @GetMapping("/productores")
    public String BusquedaProductores(@RequestParam(required = false) String termino,
            @RequestParam(required = false) String estado, @RequestParam(required = false) String orden,
            ModelMap modelo) {

        List<Productor> productores = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {

            productores = productorServicio.search(termino, estado, orden);
        } else {
            productores = productorServicio.search2(estado, orden);
        }

        if (productores.isEmpty()) {
            productores = productorServicio.listarProductores();
            modelo.put("error", "No se encontro productor con el término ingresado. Intente de otra forma");
        }

        modelo.put("termino", termino);
        modelo.put("estado", estado);
        modelo.put("orden", orden);

        modelo.addAttribute("productores", productores);
        return "productor_list.html";

    }
    
    
    @GetMapping("/bloggers")
    public String BusquedaBloggers(@RequestParam(required = false) String termino,
            @RequestParam(required = false) String estado, @RequestParam(required = false) String orden,
            ModelMap modelo){
        
         List<Blogger> bloggers = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {

            bloggers = bloggerServicio.search(termino, estado, orden);
        } else {
            bloggers = bloggerServicio.search2(estado, orden);
        }

        if (bloggers.isEmpty()) {
            bloggers = bloggerServicio.listarBloggers();
            modelo.put("error", "No se encontro Blogger con el término ingresado. Intente de otra forma");
        }

        modelo.put("termino", termino);
        modelo.put("estado", estado);
        modelo.put("orden", orden);

        modelo.addAttribute("bloggers", bloggers);
        return "bloggers_list.html";
    }
}
