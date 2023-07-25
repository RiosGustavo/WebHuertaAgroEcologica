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
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.CultivoRepositorio;
import com.Huertas_agroecologicas.demo.servicios.ComentarioServicio;
import com.Huertas_agroecologicas.demo.servicios.ConsumidorServicio;
import com.Huertas_agroecologicas.demo.servicios.CultivoServicio;
import com.Huertas_agroecologicas.demo.servicios.HuertaServicio;
import com.Huertas_agroecologicas.demo.servicios.ProductorServicio;
import com.Huertas_agroecologicas.demo.servicios.PublicacionServicio;
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
@RequestMapping("/cultivo")
public class CultivoControlador {

    @Autowired
    private CultivoServicio cultivoServicio;

    @Autowired
    private CultivoRepositorio cultivoRepositorio;

    @Autowired
    private HuertaServicio huertaServicio;

    @Autowired
    private PublicacionServicio publicacionservicio;

    @Autowired
    private ComentarioServicio comentarioServicio;
    
    @Autowired
    private ProductorServicio productorServicio;
    
     @Autowired
    private ConsumidorServicio consumidorServicio;
    
    @PreAuthorize("hasAnyRole('ROLE_PRO')")
    @GetMapping("/registrar")
    public String registrar(HttpSession session) {

        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {

            return "redirect:/productor/panel-principal";

        }
        return "huerta_form.html";
    }
    
     @PostMapping("/registro")
     public String registro(MultipartFile archivo,  @RequestParam String nombreCultivo, @RequestParam  String descripcion,
             @RequestParam  Integer precio,  @RequestParam  Integer stock, @RequestParam  String idHuerta, 
             ModelMap modelo, HttpSession session ) throws MiException, Exception{
         
          try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            //// es el productor logueado que crea la Cultivo    
            cultivoServicio.crearCultivo(archivo, nombreCultivo, descripcion, precio, stock, idHuerta);
                   
            modelo.put("exito", "la huerta fue creada exitosamente");

        } catch (MiException e) {
            Huerta  huerta = (Huerta) huertaServicio.listarHuertas();
            
            modelo.addAttribute("nombreCultivo", nombreCultivo);
            modelo.addAttribute("descripcion", descripcion);
            modelo.addAttribute("precio", precio);
            modelo.addAttribute("stock", stock);
            modelo.addAttribute("huerta", huerta);
            
            
            modelo.put("error", e.getMessage());
            

        }
        return "huerta_form.html";
         
     }
     
      @GetMapping("/modificar/{idCultivo}")
    public String modificar(@PathVariable String idCultivo, ModelMap modelo) {

        modelo.put("cultivo", cultivoServicio.getOne(idCultivo));

        return "cultivo_modificar.html";

    }
    
    @PostMapping("/modificar/{idCultivo}")
    public String modficar(MultipartFile archivo, @PathVariable String idCultivo,
            String nombreCultivo, String descripcion, Integer precio, Integer stock,
            ModelMap modelo) throws MiException, Exception {
        try {
            cultivoServicio.actualizarCultivo(archivo, nombreCultivo, descripcion, precio, stock, idCultivo);
            modelo.put("exito", "Cultivo actulizado exitosamente");

            return "redirect:/cultivo/lista";

        } catch (MiException e) {

            modelo.put("cultivo", cultivoServicio.getOne(idCultivo));
            modelo.put("error", e.getMessage());

            return "cultivo_modificar.html";

        }

    }
    
    
    @GetMapping("/baja/{idCultivo}")
    public String darBajaCultivo(@PathVariable String idCultivo) throws Exception {
        cultivoServicio.darDeBajaCultivo(idCultivo);
                
        return "redirect:/cultivo/lista";
    }

    @GetMapping("/alta/{idCultivo}")
    public String darAltaCultivo(@PathVariable String idCultivo) throws Exception {
        cultivoServicio.darDeAltaCultivo(idCultivo);
        return "redirect:/cultivo/lista";
    }

    @GetMapping("cultivo_one/idCultivo")
    public String mostarDetalleCultivo(@PathVariable String idCultivo, ModelMap modelo, HttpSession session) {

        try {
            /// obetenrmos el usuario de la secion
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            /// obtenemos la lista de los elementos asociados al cultivo
            
            Cultivo cultivo = cultivoServicio.getOne(idCultivo);
            
            List<Huerta> huertas = cultivoServicio.buscarHuertasPorIdCultivo(idCultivo);
            List<Publicacion> publicaciones = publicacionservicio.publicacionesPorCultivoActivas(idCultivo);
            
            List<Consumidor> consumidores = consumidorServicio.consumidoresPorCultivo(idCultivo);
            List<Comentario> comentarios = comentarioServicio.comentariosPorCultivo(idCultivo);
            
            /// agregasmos las listas y el oebjeto huerta al modelo para que pueda ser accesible desde huerta_one.html
            modelo.put("cultivo", cultivo);
            modelo.put("publicaciones", publicaciones);
            modelo.put("comentarios", comentarios);
            modelo.put("consumidores", consumidores);
            modelo.put("huertas", huertas);

            return "cultivo_one.html";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect:/cultivo";
        }

    }
    
    
    ///*****************************MOTOR DE BUSQUEDA**************************/
    @GetMapping("/lista")
    public String listarCultivos(@RequestParam(required = false) String termino, @RequestParam(required = false) String estado,
            @RequestParam(required = false) String orden, ModelMap modelo) {

        List<Cultivo> cultivos = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            cultivos = cultivoServicio.search(termino, estado, orden);
        } else {
            cultivos = cultivoServicio.search2(estado, orden);
        }

        if (cultivos.isEmpty()) {

            cultivos = cultivoRepositorio.findAllOrderByfecha_altaDesc();
            modelo.put("error", "No se encontró Cultivo con el término ingresado. Intente de otra forma");
        }

        modelo.put("termino", termino);
        modelo.put("estado", estado);
        modelo.put("orden", orden);
        modelo.addAttribute("cultivos", cultivos);

        return "cultivo_list.html";

    }
    
    

}
