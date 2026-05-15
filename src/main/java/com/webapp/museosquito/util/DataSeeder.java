package com.webapp.museosquito.util;

import com.webapp.museosquito.model.Exposicion;
import com.webapp.museosquito.model.FranjaReserva;
import com.webapp.museosquito.model.HorarioMuseo;
import com.webapp.museosquito.model.Museo;
import com.webapp.museosquito.repository.RepositorioFranjaReserva;
import com.webapp.museosquito.repository.RepositorioMuseo;
import org.hibernate.Session;

/**
 * Pobla la base de datos con museos reales de Quito, Ecuador.
 * Datos basados en información pública de la Fundación Museos de la Ciudad
 * y el Sistema de Museos y Espacios Culturales de Quito (SMQ).
 */
public class DataSeeder {

    private final RepositorioMuseo repoMuseo = new RepositorioMuseo();

    public void sembrar() {
        try {
            if (repoMuseo.hayDatos()) {
                System.out.println("[DataSeeder] BD ya tiene datos. Omitido.");
                return;
            }
    
            System.out.println("[DataSeeder] Insertando museos reales de Quito...");
    
            Museo museoNacional = guardarMuseo(new Museo(
                    "Museo Nacional del Ecuador",
                    "Av. Patria y Av. 6 de Diciembre, Quito",
                    "El Museo Nacional del Ecuador alberga una de las colecciones " +
                    "patrimoniales más importantes del país. Exhibe distintos bienes " +
                    "culturales que van desde lo arqueológico hasta lo pictórico y " +
                    "escultórico, cubriendo etapas como el período precolombino, " +
                    "barroco y republicano. Cuenta con salas de Arqueología, de Oro, " +
                    "Arte Colonial, Arte de la República y Arte Contemporáneo.",
                    "Historia y Arte",
                    "(02) 222-3258",
                    "https://www.museonacional.gob.ec",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Museo_Nacional_del_Ecuador.jpg/800px-Museo_Nacional_del_Ecuador.jpg",
                    2.00, 1.00, 1.00
            ));
            agregarHorario(museoNacional, "Martes - Viernes", "08:30", "16:30", false);
            agregarHorario(museoNacional, "Sábado, Domingo y Feriados", "10:00", "16:00", false);
            agregarHorario(museoNacional, "Lunes", "", "", true);
            agregarExposicion(museoNacional, "De Quito al Ecuador",
                    "Exposición permanente que desarrolla los hechos sucedidos en el " +
                    "territorio de la Real Audiencia de Quito desde 1730 hasta la " +
                    "conformación de la República del Ecuador.",
                    "Permanente", "2010-01-01", null);
    
            Museo capillaHombre = guardarMuseo(new Museo(
                    "Capilla del Hombre – Fundación Guayasamín",
                    "Mariano Calvache E18-94 y Lorenzo Chávez, Bellavista, Quito",
                    "La Capilla del Hombre es un museo de arte ideado por el pintor " +
                    "Oswaldo Guayasamín, uno de los artistas más importantes de Ecuador " +
                    "y América Latina. Fue declarado Proyecto Prioritario para la Cultura " +
                    "por la UNESCO.",
                    "Arte",
                    "(02) 244-8492",
                    "https://www.capilladelhombre.com",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Capilla_del_Hombre.jpg/800px-Capilla_del_Hombre.jpg",
                    8.00, 4.50, 4.50
            ));
            agregarHorario(capillaHombre, "Martes - Sábado", "09:00", "17:30", false);
            agregarHorario(capillaHombre, "Domingo", "10:00", "13:30", false);
            agregarHorario(capillaHombre, "Lunes", "", "", true);
    
            Museo alabado = guardarMuseo(new Museo(
                    "Casa del Alabado – Museo de Arte Precolombino",
                    "Cuenca N1-41 y Bolívar, Centro Histórico, Quito",
                    "Museo ubicado en el Centro Histórico de Quito en un edificio " +
                    "colonial del siglo XVI. Alberga más de 5.000 piezas de cerámica, " +
                    "piedra, metal, concha, madera y textil de culturas ecuatorianas precolombinas.",
                    "Arte Precolombino",
                    "(02) 228-0940",
                    "https://www.alabado.org",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Museo_Casa_del_Alabado.jpg/800px-Museo_Casa_del_Alabado.jpg",
                    6.00, 3.00, 3.00
            ));
            agregarHorario(alabado, "Miércoles - Domingo", "09:00", "17:00", false);
            agregarHorario(alabado, "Lunes y Martes", "", "", true);
    
            Museo yaku = guardarMuseo(new Museo(
                    "YAKU Parque Museo del Agua",
                    "El Placer Oe7-49 y Antonio Ante, Quito",
                    "Yaku (agua en quichua) es un museo único dedicado al agua, " +
                    "fundado en 2005. Construido sobre los primeros tanques de " +
                    "recolección y purificación de agua de la ciudad (1913).",
                    "Ciencia y Naturaleza",
                    "(02) 257-2022",
                    "https://www.yakumuseo.gob.ec",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Yaku_museo_agua.jpg/800px-Yaku_museo_agua.jpg",
                    3.00, 2.00, 2.00
            ));
            agregarHorario(yaku, "Martes - Viernes", "09:00", "17:30", false);
            agregarHorario(yaku, "Sábado y Domingo", "10:00", "17:30", false);
            agregarHorario(yaku, "Lunes", "", "", true);
    
            Museo sanFrancisco = guardarMuseo(new Museo(
                    "Museo Fray Pedro Gocial – San Francisco",
                    "Plaza de San Francisco, García Moreno y Sucre, Centro Histórico, Quito",
                    "Ubicado en el complejo franciscano más grande de América, este museo " +
                    "exhibe una valiosa colección de arte colonial quiteño de los siglos XVI al XIX.",
                    "Arte Colonial",
                    "(02) 228-1124",
                    "",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/San_Francisco_Church_Quito.jpg/800px-San_Francisco_Church_Quito.jpg",
                    2.50, 0.50, 1.50
            ));
            agregarHorario(sanFrancisco, "Lunes - Sábado", "09:00", "17:00", false);
            agregarHorario(sanFrancisco, "Domingo", "09:00", "12:00", false);
    
            Museo ccm = guardarMuseo(new Museo(
                    "Centro Cultural Metropolitano",
                    "García Moreno 887 y Espejo, frente al Palacio de Gobierno, Quito",
                    "Alberga el Museo Alberto Mena Caamaño (único museo de cera del país) " +
                    "y exposiciones temporales de arte contemporáneo ecuatoriano e internacional.",
                    "Arte Contemporáneo",
                    "(02) 295-0272",
                    "",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Centro_Cultural_Metropolitano_Quito.jpg/800px-Centro_Cultural_Metropolitano_Quito.jpg",
                    0.00, 0.00, 0.00
            ));
            agregarHorario(ccm, "Martes - Viernes", "10:30", "17:30", false);
            agregarHorario(ccm, "Sábado y Domingo", "10:00", "17:00", false);
            agregarHorario(ccm, "Lunes", "", "", true);
    
            Museo intinan = guardarMuseo(new Museo(
                    "Museo de Sitio Intiñán",
                    "Av. Manuel Córdoba Galarza km 4.5, San Antonio de Pichincha, Quito",
                    "El Museo Intiñán está ubicado en lo que se considera la ubicación " +
                    "precisa de la línea ecuatorial. Combina ciencia y cultura con " +
                    "experimentos didácticos sobre fenómenos equinocciales.",
                    "Ciencia y Cultura",
                    "(02) 239-4122",
                    "https://www.museointinan.com.ec",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Inti%C3%B1an_Solar_Museum.jpg/800px-Inti%C3%B1an_Solar_Museum.jpg",
                    5.00, 3.00, 3.00
            ));
            agregarHorario(intinan, "Lunes - Domingo", "09:00", "17:00", false);
    
            Museo museoCiudad = guardarMuseo(new Museo(
                    "Museo de la Ciudad",
                    "García Moreno S1-47 y Rocafuerte, Centro Histórico, Quito",
                    "Ubicado en el edificio público más antiguo de Quito, el primer " +
                    "hospital de la ciudad. Narra la historia de Quito desde la " +
                    "cotidianidad de sus ciudadanos con exhibiciones interactivas.",
                    "Historia",
                    "(02) 228-3882",
                    "https://www.museociudadquito.gob.ec",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Museo_de_la_Ciudad_Quito.jpg/800px-Museo_de_la_Ciudad_Quito.jpg",
                    3.00, 1.50, 1.50
            ));
            agregarHorario(museoCiudad, "Martes - Domingo", "09:30", "17:00", false);
            agregarHorario(museoCiudad, "Lunes", "", "", true);
    
            System.out.println("[DataSeeder] Museos insertados. Sembrando franjas de reserva...");
    
            // ── Franjas de reserva para HU2 ──────────────────────────────────
            RepositorioFranjaReserva repoFranja = new RepositorioFranjaReserva();
            String[] fechas = {
                "2026-06-02","2026-06-03","2026-06-04",
                "2026-06-05","2026-06-06","2026-06-09","2026-06-10"
            };
            String[][] turnos = {
                {"09:00","10:00"}, {"10:00","11:00"}, {"11:00","12:00"},
                {"14:00","15:00"}, {"15:00","16:00"}, {"16:00","17:00"}
            };
            Museo[] todosMuseos = {
                museoNacional, capillaHombre, alabado, yaku,
                sanFrancisco, ccm, intinan, museoCiudad
            };
            for (Museo m : todosMuseos) {
                for (String fecha : fechas) {
                    for (String[] turno : turnos) {
                        repoFranja.guardar(
                            new FranjaReserva(m, fecha, turno[0], turno[1], 20)
                        );
                    }
                }
            }
    
            System.out.println("[DataSeeder] ¡Semilla completada! 8 museos y franjas de reserva insertados.");
    
        } catch (Exception e) {
            System.err.println("[DataSeeder] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Museo guardarMuseo(Museo museo) {
        return repoMuseo.guardar(museo);
    }

    private void agregarHorario(Museo museo, String dia, String apertura,
                                String cierre, boolean cerrado) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            HorarioMuseo h = new HorarioMuseo(museo, dia, apertura, cierre, cerrado);
            s.persist(h);
            s.getTransaction().commit();
        } finally {
            s.close();
        }
    }

    private void agregarExposicion(Museo museo, String titulo, String descripcion,
                                   String tipo, String inicio, String fin) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            s.beginTransaction();
            Exposicion e = new Exposicion(museo, titulo, descripcion, tipo, inicio, fin);
            s.persist(e);
            s.getTransaction().commit();
        } finally {
            s.close();
        }
    }

        

    
}
