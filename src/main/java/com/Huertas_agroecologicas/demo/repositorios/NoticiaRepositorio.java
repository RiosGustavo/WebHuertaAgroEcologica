/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Huertas_agroecologicas.demo.repositorios;

import com.Huertas_agroecologicas.demo.entiddes.Noticia;
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
public interface NoticiaRepositorio extends JpaRepository<Noticia, String> {

    @Query("SELECT no FROM Noticia no WHERE no.altaBaja = true")
    public List<Noticia> listadoNoticiasActivas();

    @Query(value = "select * from Noticia order by fecha_alta desc", nativeQuery = true)
    List<Noticia> findAllOrderByfecha_altaDesc();

    @Query("SELECT no FROM Noticia no WHERE no.titulo LIKE %:titulo%")
    public List<Noticia> buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT no FROM Noticia no WHERE no.bloger.id = :id")
    public List<Noticia> noticiasPorBlogger(@Param("id") String id);

}
