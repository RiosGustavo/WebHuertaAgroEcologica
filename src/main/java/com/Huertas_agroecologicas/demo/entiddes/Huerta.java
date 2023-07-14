/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Huertas_agroecologicas.demo.entiddes;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Huerta {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idHuerta;
    private String nombrehuerta;
    private String cuerpo;

    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    private Boolean altaBaja;

    @OneToOne
    protected Imagen imagen;

    @OneToOne
    private Productor productor;
    @OneToMany
    private List<Cosecha> cosechas;
    
    
    @OneToMany
    private List<Estadistica> estadisticas;
    @OneToMany
    private List<Publicacion> publicaciones;
    @OneToMany
    private List<Comentario> comentarios;

}
