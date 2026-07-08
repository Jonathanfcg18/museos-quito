package com.webapp.museosquito.service;

import com.webapp.museosquito.model.Reserva;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio de envío de correos electrónicos mediante SMTP (Jakarta Mail).
 *
 * HU-10: Correo de confirmación de reserva.
 * HU-11: Correo de cancelación de reserva.
 *
 * Configuración SMTP (host, puerto, usuario, password): se lee, en orden de
 * prioridad, desde:
 *   1. Variables de entorno (SMTP_HOST, SMTP_PORT, SMTP_USER, SMTP_PASS,
 *      SMTP_FROM) — usado en producción (Railway), igual que DATABASE_URL
 *      en {@link com.webapp.museosquito.util.HibernateUtil}.
 *   2. Archivo "correo.properties" en el classpath (copiar de
 *      correo.properties.example) — usado en desarrollo local. Este
 *      archivo NO se sube al repositorio con credenciales reales (ver
 *      .gitignore), para no exponer credenciales en el código fuente.
 *
 * IMPORTANTE — envío no bloqueante: el envío real por SMTP se ejecuta en un
 * hilo en segundo plano ({@link #EXECUTOR}). Esto evita que una conexión
 * SMTP lenta, bloqueada por firewall (p. ej. Railway bloquea SMTP saliente
 * en planes Free/Trial/Hobby) o simplemente caída, deje "colgado" el hilo
 * que procesa la petición HTTP del visitante — que es justo lo que exige
 * HU-10 Escenario 3 / HU-11: un fallo de envío no debe afectar ni demorar
 * la respuesta al visitante.
 */
public class CorreoService {

    private static final Logger LOGGER = Logger.getLogger(CorreoService.class.getName());

    // Pool de hilos daemon dedicado al envío de correos: si la app se detiene,
    // estos hilos no impiden el apagado del servidor.
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "correo-service-sender");
        t.setDaemon(true);
        return t;
    });

    private final String host;
    private final int port;
    private final String usuario;
    private final String password;
    private final String remitente;
    private final boolean configurado;

    public CorreoService() {
        Properties props = cargarPropiedadesLocal();

        this.host = valor("SMTP_HOST", props, "correo.smtp.host");
        String portStr = valor("SMTP_PORT", props, "correo.smtp.port");
        this.port = (portStr != null && !portStr.isBlank())
                ? Integer.parseInt(portStr.trim())
                : 587;
        this.usuario = valor("SMTP_USER", props, "correo.smtp.usuario");
        this.password = valor("SMTP_PASS", props, "correo.smtp.password");
        String from = valor("SMTP_FROM", props, "correo.smtp.from");
        this.remitente = (from != null && !from.isBlank()) ? from : usuario;

        this.configurado = host != null && !host.isBlank()
                && usuario != null && !usuario.isBlank()
                && password != null && !password.isBlank();
    }

    private static Properties cargarPropiedadesLocal() {
        Properties props = new Properties();
        try (InputStream in = CorreoService.class.getClassLoader()
                .getResourceAsStream("correo.properties")) {
            if (in != null)
                props.load(in);
        } catch (IOException ignored) {
            // Si el archivo local no existe o no se puede leer, se depende
            // únicamente de las variables de entorno (producción).
        }
        return props;
    }

    private static String valor(String envKey, Properties props, String propKey) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank())
            return env.trim();
        return props.getProperty(propKey);
    }

    private Session crearSesion() {
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", host);
        mailProps.put("mail.smtp.port", String.valueOf(port));
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.starttls.enable", "true");

        // Timeouts explícitos (en ms). Por defecto Jakarta Mail NO tiene
        // timeout (espera indefinidamente), lo que puede colgar un hilo
        // completo si el puerto SMTP está bloqueado o el host no responde.
        mailProps.put("mail.smtp.connectiontimeout", "10000");
        mailProps.put("mail.smtp.timeout", "10000");
        mailProps.put("mail.smtp.writetimeout", "10000");

        return Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, password);
            }
        });
    }

    /**
     * HU-10 Escenario 1: envía el correo de confirmación inmediatamente
     * después de que una reserva queda registrada con estado activo.
     *
     * El envío real se realiza en segundo plano (no bloquea al llamador).
     * Éxito o fallo del envío SMTP se registran en el log del servidor
     * desde el propio hilo en segundo plano.
     *
     * @throws MessagingException solo si el servicio no está configurado
     *                            (validación síncrona e inmediata); nunca
     *                            por fallos de red/SMTP, que ocurren de forma
     *                            asíncrona.
     */
    public void enviarConfirmacionReserva(Reserva reserva) throws MessagingException {
        if (!configurado) {
            throw new MessagingException(
                    "El servicio de correo no está configurado (faltan credenciales SMTP).");
        }
        String codigo = reserva.getCodigoConfirmacion();
        enviarAsync(reserva.getEmailVisitante(),
                "Reserva confirmada — Portal de Cultura Quito",
                construirCuerpoConfirmacion(reserva),
                codigo);
    }

    /**
     * HU-11 Escenario 1: envía el correo de cancelación cuando la reserva
     * fue eliminada y el cupo liberado correctamente.
     *
     * El envío real se realiza en segundo plano (no bloquea al llamador).
     *
     * @throws MessagingException solo si el servicio no está configurado.
     */
    public void enviarCancelacionReserva(Reserva reserva) throws MessagingException {
        if (!configurado) {
            throw new MessagingException(
                    "El servicio de correo no está configurado (faltan credenciales SMTP).");
        }
        String codigo = reserva.getCodigoConfirmacion();
        enviarAsync(reserva.getEmailVisitante(),
                "Reserva cancelada — Portal de Cultura Quito",
                construirCuerpoCancelacion(reserva),
                codigo);
    }

    private void enviarAsync(String destinatario, String asunto, String cuerpoTextoPlano,
            String codigoReserva) {
        EXECUTOR.submit(() -> {
            try {
                enviar(destinatario, asunto, cuerpoTextoPlano);
                LOGGER.info("Correo enviado a " + destinatario
                        + " para la reserva " + codigoReserva);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING,
                        "Error al enviar correo a " + destinatario
                                + " para la reserva " + codigoReserva + ": " + e.getMessage(),
                        e);
            }
        });
    }

    private void enviar(String destinatario, String asunto, String cuerpoTextoPlano)
            throws MessagingException {
        Session session = crearSesion();
        MimeMessage mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(remitente));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject(asunto, "UTF-8");
        mensaje.setText(cuerpoTextoPlano, "UTF-8");

        Transport.send(mensaje);
    }

    private String construirCuerpoConfirmacion(Reserva reserva) {
        double precioTotal = calcularPrecioTotal(reserva);
        return "Hola " + reserva.getNombreVisitante() + ",\n\n" +
                "Tu reserva ha sido confirmada con los siguientes detalles:\n\n" +
                "Museo: " + reserva.getFranja().getMuseo().getNombre() + "\n" +
                "Dirección: " + reserva.getFranja().getMuseo().getUbicacion() + "\n" +
                "Fecha de la visita: " + reserva.getFranja().getFecha() + "\n" +
                "Horario: " + reserva.getFranja().getHoraInicio() + " - "
                + reserva.getFranja().getHoraFin() + "\n" +
                "Cantidad de personas: " + reserva.getCantidadPersonas() + "\n" +
                "Precio total: $" + String.format("%.2f", precioTotal) + "\n" +
                "Código de confirmación: " + reserva.getCodigoConfirmacion() + "\n\n" +
                "Guarda este código, lo necesitarás al llegar al museo.\n\n" +
                "Gracias por reservar con el Portal de Cultura Quito.";
    }

    private String construirCuerpoCancelacion(Reserva reserva) {
        return "Hola " + reserva.getNombreVisitante() + ",\n\n" +
                "Tu reserva ha sido cancelada exitosamente y el cupo fue liberado.\n\n" +
                "Museo: " + reserva.getFranja().getMuseo().getNombre() + "\n" +
                "Fecha: " + reserva.getFranja().getFecha() + "\n" +
                "Horario: " + reserva.getFranja().getHoraInicio() + " - "
                + reserva.getFranja().getHoraFin() + "\n" +
                "Código de confirmación: " + reserva.getCodigoConfirmacion() + "\n\n" +
                "Si tú no solicitaste esta cancelación, contáctanos de inmediato.\n\n" +
                "Portal de Cultura Quito.";
    }

    /**
     * Calcula el precio total de la reserva.
     * Nota: el modelo actual (Reserva/FranjaReserva) no distingue tarifas
     * por edad al momento de reservar, por lo que se usa el precio de
     * adulto del museo como tarifa única. Ajustar aquí si en el futuro se
     * agrega el desglose de tipo de visitante (adulto/niño/estudiante).
     */
    private double calcularPrecioTotal(Reserva reserva) {
        double precioUnitario = reserva.getFranja().getMuseo().getPrecioAdulto();
        return precioUnitario * reserva.getCantidadPersonas();
    }
}

