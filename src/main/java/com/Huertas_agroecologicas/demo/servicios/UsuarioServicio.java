/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.enumeraciones.Rol;
import com.Huertas_agroecologicas.demo.excepciones.MiException;
import com.Huertas_agroecologicas.demo.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void crearUsuario(MultipartFile archivo, String email, String password, String password2) throws MiException {

        validar(email, password, password2);

        Usuario usuario = new Usuario();

        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setFechaAlta(new Date());
        usuario.setRoles(Rol.USER);
        usuario.setAltaBaja(Boolean.TRUE);

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void ModificarUsuario(MultipartFile archivo, String id, String email, String password, String password2) throws MiException {

        validar(email, password, password2);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);
            usuarioRepositorio.save(usuario);
        }
        

    }
    ///// METODO PARA CAMBIAR LA CONTRASEÑA DEL USUARIO SI DESEA
    @Transactional
    public void cambiarClave(String claveActual, String id, String clave, String clave2)throws MiException  {
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            if (clave.isEmpty() || clave == null){
                throw new MiException ("La contraseña no puede ser vacía.");
            }
            
            if(clave.length() < 8){
                throw new MiException  ("La contraseña nueva debe se de mas de 8 caracteres");
            }
            
            if (!clave.equals(clave2)) {
                throw new MiException("Las contraseñas no coinciden. Por favor introduzcalas correctamente.");
            }
            
            Usuario usuario = respuesta.get();
            
            
            /// CON ESTE SE CODIFICA ELPASSWORD INGRESADO POR EL USUARIO
             String encodedPassword = usuario.getPassword();
             
              if (bCryptPasswordEncoder.matches(claveActual, encodedPassword)) {
                usuario.setPassword(new BCryptPasswordEncoder().encode(clave));

                usuarioRepositorio.save(usuario);
            } else {
                throw new MiException("La contraseña actual no es válida.");
            }
            
            
            
        }
    }
    
    

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void validar(String email, String password, String password2) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
