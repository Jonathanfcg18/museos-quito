package com.webapp.museosquito.util;

import com.webapp.museosquito.model.Exposicion;
import com.webapp.museosquito.model.HorarioMuseo;
import com.webapp.museosquito.model.Museo;
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

            // ── 1. Museo Nacional del Ecuador ──────────────────────────────────
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
                    "conformación de la República del Ecuador. Cuenta con 44 figuras de cera.",
                    "Permanente", "2010-01-01", null);
            agregarExposicion(museoNacional, "Arte Precolombino del Ecuador",
                    "Colección de cerámica, orfebrería y objetos rituales de las " +
                    "culturas prehispánicas del Ecuador.",
                    "Permanente", "2010-01-01", null);

            // ── 2. Capilla del Hombre (Fundación Guayasamín) ──────────────────
            Museo capillaHombre = guardarMuseo(new Museo(
                    "Capilla del Hombre – Fundación Guayasamín",
                    "Mariano Calvache E18-94 y Lorenzo Chávez, Bellavista, Quito",
                    "La Capilla del Hombre es un museo de arte ideado por el pintor " +
                    "Oswaldo Guayasamín, uno de los artistas más importantes de Ecuador " +
                    "y América Latina. Fue declarado Proyecto Prioritario para la Cultura " +
                    "por la UNESCO. El recinto celebra al ser humano y la identidad de los " +
                    "pueblos latinoamericanos a través del arte. Incluye la Casa Museo " +
                    "donde Guayasamín vivió y trabajó, conservada tal como la dejó.",
                    "Arte",
                    "(02) 244-8492",
                    "https://www.capilladelhombre.com",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Capilla_del_Hombre.jpg/800px-Capilla_del_Hombre.jpg",
                    8.00, 4.50, 4.50
            ));
            agregarHorario(capillaHombre, "Martes - Sábado", "09:00", "17:30", false);
            agregarHorario(capillaHombre, "Domingo", "10:00", "13:30", false);
            agregarHorario(capillaHombre, "Lunes", "", "", true);
            agregarExposicion(capillaHombre, "Obras de Oswaldo Guayasamín",
                    "Colección permanente de obras del maestro quiteño, incluyendo sus " +
                    "series más emblemáticas: La Edad de la Ira, La Edad de la Ternura y " +
                    "El Camino del Llanto.",
                    "Permanente", "2002-01-01", null);

            // ── 3. Casa del Alabado – Museo de Arte Precolombino ──────────────
            Museo alabado = guardarMuseo(new Museo(
                    "Casa del Alabado – Museo de Arte Precolombino",
                    "Cuenca N1-41 y Bolívar, Centro Histórico, Quito",
                    "Museo ubicado en el Centro Histórico de Quito en un edificio " +
                    "colonial del siglo XVI. Alberga más de 5.000 piezas de cerámica, " +
                    "piedra, metal, concha, madera y textil, pertenecientes a diferentes " +
                    "culturas ecuatorianas precolombinas. Muchas piezas tenían uso ritual. " +
                    "Destaca la Figura Alada, cerámica que representa a un chamán, y " +
                    "urnas funerarias napo.",
                    "Arte Precolombino",
                    "(02) 228-0940",
                    "https://www.alabado.org",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Museo_Casa_del_Alabado.jpg/800px-Museo_Casa_del_Alabado.jpg",
                    6.00, 3.00, 3.00
            ));
            agregarHorario(alabado, "Miércoles - Domingo", "09:00", "17:00", false);
            agregarHorario(alabado, "Lunes y Martes", "", "", true);
            agregarExposicion(alabado, "Arte Precolombino del Ecuador",
                    "Más de 5.000 piezas de culturas prehispánicas ecuatorianas. " +
                    "Ingreso gratuito el último sábado de cada mes.",
                    "Permanente", "2010-01-01", null);

            // ── 4. YAKU Parque Museo del Agua ─────────────────────────────────
            Museo yaku = guardarMuseo(new Museo(
                    "YAKU Parque Museo del Agua",
                    "El Placer Oe7-49 y Antonio Ante, Quito",
                    "Yaku (agua en quichua) es un museo único dedicado al agua, " +
                    "fundado en 2005. Construido sobre los primeros tanques de " +
                    "recolección y purificación de agua de la ciudad (1913), ubicados " +
                    "en el histórico barrio El Placer. Cuenta con exposiciones permanentes " +
                    "y temporales relacionadas con la conservación del agua, cambio " +
                    "climático y bienestar ambiental. Ideal para familias.",
                    "Ciencia y Naturaleza",
                    "(02) 257-2022",
                    "https://www.yakumuseo.gob.ec",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/Yaku_museo_agua.jpg/800px-Yaku_museo_agua.jpg",
                    3.00, 2.00, 2.00
            ));
            agregarHorario(yaku, "Martes - Viernes", "09:00", "17:30", false);
            agregarHorario(yaku, "Sábado y Domingo", "10:00", "17:30", false);
            agregarHorario(yaku, "Lunes", "", "", true);
            agregarExposicion(yaku, "El Ciclo del Agua",
                    "Exposición interactiva sobre el ciclo hidrológico, la conservación " +
                    "del recurso hídrico y el cambio climático.",
                    "Permanente", "2005-01-01", null);
            agregarExposicion(yaku, "Agua y Vida en los Andes",
                    "Exposición temporal sobre el manejo ancestral del agua en las " +
                    "culturas andinas del Ecuador.",
                    "Temporal", "2025-01-01", "2025-12-31");

            // ── 5. Museo Fray Pedro Gocial (San Francisco) ────────────────────
            Museo sanFrancisco = guardarMuseo(new Museo(
                    "Museo Fray Pedro Gocial – San Francisco",
                    "Plaza de San Francisco, García Moreno y Sucre, Centro Histórico, Quito",
                    "Ubicado en el complejo franciscano más grande de América, este museo " +
                    "exhibe una valiosa colección de arte colonial quiteño: pinturas, " +
                    "esculturas, telas y objetos litúrgicos de los siglos XVI al XIX. " +
                    "El convento fue fundado en 1534 y es considerado Patrimonio de la " +
                    "Humanidad por la UNESCO junto al Centro Histórico de Quito.",
                    "Arte Colonial",
                    "(02) 228-1124",
                    "",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/0/06/San_Francisco_Church_Quito.jpg/800px-San_Francisco_Church_Quito.jpg",
                    2.50, 0.50, 1.50
            ));
            agregarHorario(sanFrancisco, "Lunes - Sábado", "09:00", "17:00", false);
            agregarHorario(sanFrancisco, "Domingo", "09:00", "12:00", false);
            agregarExposicion(sanFrancisco, "Arte Quiteño Colonial",
                    "Colección de pinturas, esculturas policromadas y objetos de orfebrería " +
                    "de la Escuela Quiteña, considerada una de las más importantes de " +
                    "América Latina.",
                    "Permanente", "1970-01-01", null);

            // ── 6. Centro Cultural Metropolitano ─────────────────────────────
            Museo ccm = guardarMuseo(new Museo(
                    "Centro Cultural Metropolitano",
                    "García Moreno 887 y Espejo, frente al Palacio de Gobierno, Quito",
                    "Ubicado frente al Palacio de Gobierno (Casa de Carondelet), el " +
                    "Centro Cultural Metropolitano alberga el Museo Alberto Mena Caamaño " +
                    "(único museo de cera del país) y exposiciones temporales de arte " +
                    "contemporáneo ecuatoriano e internacional. El edificio original es " +
                    "de arquitectura colonial del siglo XVIII.",
                    "Arte Contemporáneo",
                    "(02) 295-0272",
                    "",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/Centro_Cultural_Metropolitano_Quito.jpg/800px-Centro_Cultural_Metropolitano_Quito.jpg",
                    0.00, 0.00, 0.00
            ));
            agregarHorario(ccm, "Martes - Viernes", "10:30", "17:30", false);
            agregarHorario(ccm, "Sábado y Domingo", "10:00", "17:00", false);
            agregarHorario(ccm, "Lunes", "", "", true);
            agregarExposicion(ccm, "Museo de Cera Alberto Mena Caamaño",
                    "El único museo de cera del Ecuador, inaugurado en 1959. " +
                    "Recreación de escenas históricas de la independencia ecuatoriana " +
                    "con figuras de cera.",
                    "Permanente", "1959-01-01", null);

            // ── 7. Museo de Sitio Intiñán ─────────────────────────────────────
            Museo intinan = guardarMuseo(new Museo(
                    "Museo de Sitio Intiñán",
                    "Av. Manuel Córdoba Galarza km 4.5, San Antonio de Pichincha, Quito",
                    "El Museo Intiñán está ubicado en lo que se considera la ubicación " +
                    "precisa de la línea ecuatorial. Combina ciencia y cultura con " +
                    "experimentos didácticos sobre fenómenos que se manifiestan en la " +
                    "zona equinoccial: el efecto Coriolis, equilibrio de huevos en la " +
                    "línea, y más. Incluye reproducción de un poblado indígena y " +
                    "demostración de cosmovisión ancestral.",
                    "Ciencia y Cultura",
                    "(02) 239-4122",
                    "https://www.museointinan.com.ec",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Inti%C3%B1an_Solar_Museum.jpg/800px-Inti%C3%B1an_Solar_Museum.jpg",
                    5.00, 3.00, 3.00
            ));
            agregarHorario(intinan, "Lunes - Domingo", "09:00", "17:00", false);
            agregarExposicion(intinan, "La Línea Ecuatorial",
                    "Experimentos y demostraciones científicas sobre los fenómenos " +
                    "físicos únicos que ocurren exactamente en el ecuador geográfico.",
                    "Permanente", "1980-01-01", null);
            agregarExposicion(intinan, "Culturas Ancestrales del Ecuador",
                    "Reproducción de viviendas y objetos de las culturas indígenas " +
                    "ecuatorianas: Tsáchila, Kichwa, Shuar y más.",
                    "Permanente", "1980-01-01", null);

            // ── 8. Museo de la Ciudad ─────────────────────────────────────────
            Museo museoCiudad = guardarMuseo(new Museo(
                    "Museo de la Ciudad",
                    "García Moreno S1-47 y Rocafuerte, Centro Histórico, Quito",
                    "Ubicado en el edificio público más antiguo de Quito, el primer " +
                    "hospital de la ciudad (Hospital San Juan de Dios, siglo XVI). " +
                    "Narra la historia de Quito desde la cotidianidad de sus ciudadanos: " +
                    "desde los primeros habitantes hasta la actualidad. " +
                    "¿Quiénes fueron los primeros habitantes? ¿Qué pasó con la llegada " +
                    "de los Incas y la conquista española? El museo responde estas " +
                    "preguntas con exhibiciones interactivas.",
                    "Historia",
                    "(02) 228-3882",
                    "https://www.museociudadquito.gob.ec",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Museo_de_la_Ciudad_Quito.jpg/800px-Museo_de_la_Ciudad_Quito.jpg",
                    3.00, 1.50, 1.50
            ));
            agregarHorario(museoCiudad, "Martes - Domingo", "09:30", "17:00", false);
            agregarHorario(museoCiudad, "Lunes", "", "", true);
            agregarExposicion(museoCiudad, "Historia de Quito",
                    "Recorrido por la historia de la capital: período precolombino, " +
                    "Inca, colonial, independencia y república.",
                    "Permanente", "2000-01-01", null);

            System.out.println("[DataSeeder] ¡Semilla completada! 8 museos de Quito insertados.");
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
