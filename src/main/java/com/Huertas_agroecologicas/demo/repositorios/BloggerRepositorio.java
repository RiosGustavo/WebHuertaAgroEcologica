/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Blogger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public interface BloggerRepositorio extends JpaRepository<Blogger, String>{
    @Query("SELECT blog FROM Blogger blog WHERE (:termino IS NULL OR CONCAT(blog.nombreApellido,blog.email ,blog.direccion, blog.dni, blog.id) LIKE %:termino%) "
            + "AND (:estado IS NULL OR blog.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE blog.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN blog.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN blog.fechaAlta END DESC")
    public List<Blogger> search(@Param("termino") String termino, @Param("estado") String estado, @Param("orden") String orden);

    @Query("SELECT blog FROM Blogger blog WHERE (:estado IS NULL OR blog.altaBaja = CASE WHEN :estado = 'true' THEN 1 WHEN :estado = 'false' THEN 0 ELSE blog.altaBaja END) "
            + "ORDER BY CASE WHEN :orden = 'asc' THEN blog.fechaAlta END ASC, CASE WHEN :orden = 'desc' THEN blog.fechaAlta END DESC")
    public List<Blogger> search2(@Param("estado") String estado, @Param("orden") String orden);
    
}
