package com.softtek.prueba.model;

import com.softtek.prueba.model.Vehiculo;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-13T14:33:09")
@StaticMetamodel(Proveedor.class)
public class Proveedor_ { 

    public static volatile SingularAttribute<Proveedor, String> correo;
    public static volatile SingularAttribute<Proveedor, String> direccion;
    public static volatile SingularAttribute<Proveedor, String> nombre;
    public static volatile ListAttribute<Proveedor, Vehiculo> vehiculoList;
    public static volatile SingularAttribute<Proveedor, Long> numiden;

}