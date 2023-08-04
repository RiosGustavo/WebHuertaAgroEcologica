/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.controladores;

import com.Huertas_agroecologicas.demo.Utility;
import com.Huertas_agroecologicas.demo.entiddes.Cultivo;
import com.Huertas_agroecologicas.demo.entiddes.Huerta;
import com.Huertas_agroecologicas.demo.entiddes.Noticia;
import com.Huertas_agroecologicas.demo.entiddes.Publicacion;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.servicios.ConsumidorServicio;
import com.Huertas_agroecologicas.demo.servicios.CultivoServicio;
import com.Huertas_agroecologicas.demo.servicios.HuertaServicio;
import com.Huertas_agroecologicas.demo.servicios.NoticiaServicio;
import com.Huertas_agroecologicas.demo.servicios.ProductorServicio;
import com.Huertas_agroecologicas.demo.servicios.PublicacionServicio;
import com.Huertas_agroecologicas.demo.servicios.UsuarioServicio;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProductorServicio productorServicio;

    @Autowired
    private HuertaServicio huertaServicio;

    @Autowired
    private CultivoServicio cultivoServicio;

    @Autowired
    private ConsumidorServicio consumidorServicio;

    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private PublicacionServicio publicacionServicio;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/")
    public String index(ModelMap modelo, HttpSession session) {

        List<Huerta> huertas = huertaServicio.listarHuertas();
        modelo.addAttribute("huertas", huertas);

        List<Cultivo> cultivos = cultivoServicio.listarCultivos();
        modelo.addAttribute("cultivos", cultivos);

        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);

        List<Publicacion> publicaciones = publicacionServicio.listarPublicaciones();
        modelo.addAttribute("publicaciones", publicaciones);

        return "index.html";

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o contraseña invalidos");
        }

        return "login.html";

    }

    @GetMapping("/registrar")
    public String registrar() {
        return "register.html";

    }

    @PostMapping("/registro")
    public String registro(MultipartFile archivo, @RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam String rol,
            @RequestParam(required = false) String nombreConsumidor, @RequestParam(required = false) String dni, @RequestParam(required = false) String direccionUsuario,
            @RequestParam(required = false) String nombreProductor, @RequestParam(required = false) String dniProductor, @RequestParam(required = false) String direccionProductor,
            ModelMap modelo) {

        try {
            if (rol.equalsIgnoreCase("consumidor")) {
                consumidorServicio.registrarConsumidor(archivo, nombreConsumidor, dni, direccionUsuario, email, password, password2);
            } else if (rol.equalsIgnoreCase("productor")) {
                productorServicio.registrarProductor(archivo, nombreProductor, dniProductor, direccionProductor, email, password, password2);
            } else {
                usuarioServicio.crearUsuario(archivo, email, password, password2);
            }
            modelo.put("exito", "Usuario registrado correctamente");
            return "register.html";

        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("email", email);
            return " register.html";
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_PRO','ROLE_CON','ROLE_BLOG')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRoles().toString().equals("ADM")) {
            return "redirect:/admin/dashboard";
        }
        if (logueado.getRoles().toString().equals("PRO")) {
            return "redirect:productor/panel-principal";
        }
        if (logueado.getRoles().toString().equals("BLOG")) {
            return "redirect:/blogger/panel-principal";
        }

        if (logueado.getRoles().toString().equals("CON")) {
            String consumidorUrl = String.format("redirect:/consumidor/%s", logueado.getId());
            return consumidorUrl;
        }
        return "index.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_PRO','ROLE_CON')")
    @GetMapping("/perfil/changePassword")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        Usuario usuarioActualizado = usuarioServicio.getOne(usuario.getId());
        modelo.put("usuario", usuario);
        modelo.addAttribute("usuarioActualizado", usuarioActualizado);
        return "change_password.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADM','ROLE_PRO','ROLE_CON')")
    @PostMapping("/perfil/changePassword")
    public String perfil(@RequestParam String claveActual, @RequestParam String id,
            @RequestParam String clave, @RequestParam String clave2, ModelMap model) {
        try {
            usuarioServicio.cambiarClave(claveActual, id, clave, clave2);
            model.put("exito", "La contraseña ha sido actualizada corrctamente");
            return "change_password.html";

        } catch (Exception e) {
            model.put("error", e.getMessage());
            return "change_password.html";
        }

    }

    @GetMapping("/forgot_password")
    public String recuperarContraseñaForm(Model modelo) {
        return "forgot_password_form.html";
    }

    @PostMapping("/forgot_password")
    public String forgot_password(@RequestParam String email, ModelMap model, HttpServletRequest request) throws Exception {

        String token = RandomString.make(45);

        try {
            usuarioServicio.updateResetPasswordToken(token, email);

            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token" + token;

            sendEmail(email, resetPasswordLink);

            model.put("exito", "Ya te enviamos a tu email las instrucciones para recuperar tu contraseña");
        } catch (Exception e) {
            model.put("error", e.getMessage());
        }

        return "forgot_password_form.html";
    }

    public void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@huertasEcologicas.com", "huertas Ecologicas  support");
        helper.setTo(email);

        String Subject = "Aqui esta el link para restablecer tu contraseña";
        String content = "<p> Hola, </p>"
                + "<p> Solicitaste restablecer tu contraseña. </p>"
                + "<p> Haz click en el link de abajo para cambiar tu contraseña </p>"
                + "<p><b><a href=\"" + resetPasswordLink + "\">Restablecer mi contraseña</a><b></p>"
                + "<p>Ignora este email si recordate tu contraseña o no solicitaste restablecerla</p>";

        helper.setSubject(Subject);
        helper.setText(content, true);

        mailSender.send(message);

    }

    @GetMapping("/reset_password")
    public String resetPasswordForm(@Param(value = "token") String token, Model modelo) {

        Usuario usuario = usuarioServicio.obtenerUsuarioPorToken(token);
        modelo.addAttribute("token", token);
        return "reset_password_form.html";

    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, ModelMap modelo) {

        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Usuario usuario = usuarioServicio.obtenerUsuarioPorToken(token);
        modelo.addAttribute("titulo", "restablecer contraseña");

        if (usuario == null) {
            modelo.put("error", "Token inválido");
            return "mensaje.html";

        } else {
            usuarioServicio.updatePassword(usuario, password);
            modelo.put("exito", "Tu contraseña fue cambiada exitosamente");
        }
        return "mensaje.html";
    }

}
