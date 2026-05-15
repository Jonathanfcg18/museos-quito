<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Reserva confirmada – Portal de Cultura Quito"/>
<c:set var="currentPage" value="museos"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">
    <div class="confirmacion-box">
        <div class="confirmacion-icono">✅</div>
        <h1>¡Reserva confirmada!</h1>
        <p class="confirmacion-sub">Tu visita ha sido registrada exitosamente.</p>

        <div class="confirmacion-detalle">
            <div class="conf-row">
                <span class="conf-label">Código de reserva</span>
                <span class="conf-valor codigo-valor">${reserva.codigoConfirmacion}</span>
            </div>
            <div class="conf-row">
                <span class="conf-label">Museo</span>
                <span class="conf-valor">${reserva.franja.museo.nombre}</span>
            </div>
            <div class="conf-row">
                <span class="conf-label">Fecha</span>
                <span class="conf-valor">${reserva.franja.fecha}</span>
            </div>
            <div class="conf-row">
                <span class="conf-label">Horario</span>
                <span class="conf-valor">${reserva.franja.horaInicio} – ${reserva.franja.horaFin}</span>
            </div>
            <div class="conf-row">
                <span class="conf-label">Visitante</span>
                <span class="conf-valor">${reserva.nombreVisitante}</span>
            </div>
            <div class="conf-row">
                <span class="conf-label">Email</span>
                <span class="conf-valor">${reserva.emailVisitante}</span>
            </div>
            <div class="conf-row">
                <span class="conf-label">Personas</span>
                <span class="conf-valor">${reserva.cantidadPersonas}</span>
            </div>
        </div>

        <p class="confirmacion-nota">
            Guarda tu código de reserva. Lo necesitarás al llegar al museo.
        </p>

        <div class="confirmacion-acciones">
            <a href="${pageContext.request.contextPath}/museos" class="btn btn-secondary">
                Ver más museos
            </a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                Ir al inicio
            </a>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/includes/footer.jsp" %>