/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.servicios;

import com.Huertas_agroecologicas.demo.entiddes.Imagen;
import com.Huertas_agroecologicas.demo.entiddes.Usuario;
import com.Huertas_agroecologicas.demo.enumeraciones.Rol;
import com.Huertas_agroecologicas.demo.repositorios.UsuarioRepositorio;
import java.util.Date;
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
public class UsuarioServicio implements UserDetailsService{
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private ImagenServicio imagenServicio;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
     @Transactional
    public void crearUsuario(MultipartFile archivo, String email, String password, String password2){
        
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
