/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.entiddes.Comentario;
import com.Huertas_agroecologicas.demo.entiddes.Consumidor;
import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Productor;
import com.Huertas_agroecologicas.demo.entiddes.Publicacion;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.HuertaRepositorio;
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
@RequestMapping("/huerta")
public class HuertaControlador {

    @Autowired
    private HuertaServicio huertaServicio;

    @Autowired
    private HuertaRepositorio huertaRepositorio;

    @Autowired
    private ProductorServicio productorServicio; //// como agregamos el consumidor asi mismo se agrega el productor 
    
    @Autowired
    private ConsumidorServicio consumidorServicio;

    @Autowired
    private CultivoServicio cultivoServicio;

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_PRO')")
    @GetMapping("/registrar")
    public String registrar(HttpSession session) {

        if (session.getAttribute("usuariosession") == null || !((Usuario) session.getAttribute("usuariosession")).getAltaBaja()) {

            return "redirect:/productor/panel-principal";

        }
        return "huerta_form.html";
    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String nombreHuerta, @RequestParam String cuerpo,
            ModelMap modelo, HttpSession session) throws MiException, Exception {

        try {

            Usuario logueado = (Usuario) session.getAttribute("usuariosession");
            //// es el productor logueado que crea la huerta     
            huertaServicio.crearHuerta(archivo, nombreHuerta, cuerpo, logueado.getId());
            modelo.put("exito", "la huerta fue creada exitosamente");

        } catch (MiException e) {
            modelo.put("error", e.getMessage());
            modelo.addAttribute("nombreHuerta", nombreHuerta);
            modelo.addAttribute("cuerpo", cuerpo);

        }
        return "huerta_form.html";
    }

    @GetMapping("/modificar/{idHuerta}")
    public String modificar(@PathVariable String idHuerta, ModelMap modelo) {

        modelo.put("huerta", huertaServicio.getOne(idHuerta));

        return "huerta_modificar.html";

    }

    @PostMapping("/modificar/{idHuerta}")
    public String modficar(MultipartFile archivo, @PathVariable String idHuerta,
            String nombreHuerta, String cuerpo, ModelMap modelo) throws MiException, Exception {
        try {
            huertaServicio.actualizarHuerta(archivo, nombreHuerta, cuerpo, idHuerta);
            modelo.put("exito", "Huerta actulizada exitosamente");

            return "redirect:/huerta/lista";

        } catch (MiException e) {

            modelo.put("huerta", huertaServicio.getOne(idHuerta));
            modelo.put("error", e.getMessage());

            return "huerta_modificar.html";

        }

    }

    @GetMapping("/baja/{idHuerta}")
    public String darBajaHuerta(@PathVariable String idHuerta) throws Exception {
        huertaServicio.darDeBajaHuerta(idHuerta);
        return "redirect:/huerta/lista";
    }

    @GetMapping("/alta/{idHuerta}")
    public String darAltaHuerta(@PathVariable String idHuerta) throws Exception {
        huertaServicio.darDeAltaHuerta(idHuerta);
        return "redirect:/huerta/lista";
    }

    @GetMapping("huerta_one/idHuerta")
    public String mostarDetalleHuerta(@PathVariable String idHuerta, ModelMap modelo, HttpSession session) {

        try {
            /// obetenrmos el usuario de la secion
            Usuario usuario = (Usuario) session.getAttribute("usuariosession");
            /// obtenemos la lista de los elementos asociados a la huerta
            List<Cultivo> cultivos = cultivoServicio.cultivosPorHuerta(idHuerta);
            Huerta huerta = huertaServicio.getOne(idHuerta);
           
            List<Consumidor> consumidores = consumidorServicio.consumidoresPorHuerta(idHuerta);
            List<Publicacion> publicaciones = publicacionServicio.publicacionesPorHuertaActivas(idHuerta);
            List<Comentario> comentarios = comentarioServicio.comentariosPorHuerta(idHuerta);
            /// agregasmos las listas y el oebjeto huerta al modelo para que pueda ser accesible desde huerta_one.html
            modelo.put("cultivos", cultivos);
            modelo.put("consumidores", consumidores);
            modelo.put("productor", huerta.getProductor());
            modelo.put("publicaciones", publicaciones);
            modelo.put("comentarios", comentarios);
            modelo.put("huerta", huerta);

            return "huerta_one.html";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect:/huerta";
        }

    }

    ///*****************************MOTOR DE BUSQUEDA**************************/
    @GetMapping("/lista")
    public String listarHuertas(@RequestParam(required = false) String termino, @RequestParam(required = false) String estado,
            @RequestParam(required = false) String orden, ModelMap modelo) {

        List<Huerta> huertas = new ArrayList<>();

        if (termino != null && !termino.isEmpty()) {
            huertas = huertaServicio.search(termino, estado, orden);
        } else {
            huertas = huertaServicio.search2(estado, orden);
        }

        if (huertas.isEmpty()) {

            huertas = huertaRepositorio.findAllOrderByfecha_altaDesc();
            modelo.put("error", "No se encontró Huerta con el término ingresado. Intente de otra forma");
        }

        modelo.put("termino", termino);
        modelo.put("estado", estado);
        modelo.put("orden", orden);
        modelo.addAttribute("huertas", huertas);

        return "huerta_list.html";

    }

}
