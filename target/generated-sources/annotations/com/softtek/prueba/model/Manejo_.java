package com.softtek.prueba.model;

import com.softtek.prueba.model.Conductor;
import com.softtek.prueba.model.ManejoPK;
import com.softtek.prueba.model.Vehiculo;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2023-02-13T14:33:09")
@StaticMetamodel(Manejo.class)
public class Manejo_ { 

    public static volatile SingularAttribute<Manejo, Date> fechaini;
    public static volatile SingularAttribute<Manejo, ManejoPK> manejoPK;
    public static volatile SingularAttribute<Manejo, Conductor> conductor1;
    public static volatile SingularAttribute<Manejo, Vehiculo> vehiculo;
    public static volatile SingularAttribute<Manejo, Date> fechaFin;

}