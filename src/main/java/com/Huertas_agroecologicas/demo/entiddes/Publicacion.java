/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.entiddes;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author User
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class Publicacion {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")

    private String idPublicacion;

    private String titulo;
    private String descripcion;

    @Column(length = 65535, columnDefinition = "text")
    private String cuerpo;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaAlta;

    @OneToOne
    private Imagen imagen;
    private String video;
    private Boolean altaBaja;
    
    
    @ManyToOne
    @JoinColumn(name = "id")
    private Usuario Usuario;
    
     @ManyToOne
    @JoinColumn(name = "idProductor")
    private Productor productor;
     
      @ManyToOne
     @JoinColumn(name = "idConsumidor")
    private Consumidor consumidor;
     
     
    
    @ManyToOne
    @JoinColumn(name = "idHuerta")
    private Huerta huerta;

    @ManyToOne
    @JoinColumn(name = "idCultivo")
    private Cultivo cultivo;

}
