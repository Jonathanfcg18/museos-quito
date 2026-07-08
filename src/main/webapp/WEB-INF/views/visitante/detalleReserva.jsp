<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Detalle de Reserva – Portal de Cultura Quito"/>
<c:set var="currentPage" value="misreservas"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

    <nav class="breadcrumb">
        <a href="${pageContext.request.contextPath}/">Inicio</a>
        <span>›</span>
        <a href="${pageContext.request.contextPath}/reservas/mis-reservas">Mis Reservas</a>
        <span>›</span>
        <span>Detalle de reserva</span>
    </nav>

    <div class="page-header">
        <h1>🔍 Detalle de Reserva</h1>
        <p>Información completa de tu reserva.</p>
    </div>

    <div class="confirmacion-box" style="max-width:640px;">

        <%-- Código y estado --%>
        <div style="display:flex;justify-content:space-between;align-items:center;
                margin-bottom:1.5rem;flex-wrap:wrap;gap:0.5rem;">
            <div>
        <span style="font-size:0.82rem;color:var(--text-muted);font-weight:600;
                     text-transform:uppercase;letter-spacing:0.05em;">
          Código de reserva
        </span>
                <div class="codigo-valor" style="margin-top:0.25rem;font-size:1.1rem;">
                    ${reserva.codigoConfirmacion}
                </div>
            </div>
            <c:choose>
                <c:when test="${reserva.activa}">
          <span class="badge badge-active" style="font-size:0.85rem;padding:0.4rem 1rem;">
            ✅ Activa
          </span>
                </c:when>
                <c:otherwise>
          <span class="badge"
                style="background:rgba(198,40,40,0.1);color:#C62828;
                       border-color:rgba(198,40,40,0.3);
                       font-size:0.85rem;padding:0.4rem 1rem;">
            🚫 Cancelada
          </span>
                </c:otherwise>
            </c:choose>
        </div>

        <%-- Tabla de detalle --%>
        <div class="confirmacion-detalle">

            <div class="conf-row">
                <span class="conf-label">🏛️ Museo</span>
                <span class="conf-valor">${reserva.franja.museo.nombre}</span>
            </div>

            <div class="conf-row">
                <span class="conf-label">📍 Dirección</span>
                <span class="conf-valor">${reserva.franja.museo.ubicacion}</span>
            </div>

            <div class="conf-row">
                <span class="conf-label">📅 Fecha de visita</span>
                <span class="conf-valor">${reserva.franja.fecha}</span>
            </div>

            <div class="conf-row">
                <span class="conf-label">🕐 Horario</span>
                <span class="conf-valor">
          ${reserva.franja.horaInicio} – ${reserva.franja.horaFin}
        </span>
            </div>

            <div class="conf-row">
                <span class="conf-label">👥 Cantidad de personas</span>
                <span class="conf-valor">${reserva.cantidadPersonas}</span>
            </div>

            <%-- HAL-07: Precio siempre con dos decimales usando fmt:formatNumber --%>
            <div class="conf-row">
                <span class="conf-label">💰 Precio total</span>
                <span class="conf-valor">
                <c:choose>
                    <c:when test="${reserva.franja.museo.precioAdulto == 0}">
                        Entrada gratuita
                    </c:when>
                    <c:otherwise>
                        $<fmt:formatNumber
                            value="${reserva.franja.museo.precioAdulto * reserva.cantidadPersonas}"
                            pattern="#,##0.00"/>
                    </c:otherwise>
                </c:choose>
              </span>
            </div>


            <div class="conf-row conf-row-highlight">
                <span class="conf-label">🎫 Código de confirmación</span>
                <span class="conf-valor">
          <span class="codigo-valor">${reserva.codigoConfirmacion}</span>
        </span>
            </div>

            <div class="conf-row">
                <span class="conf-label">📆 Fecha de registro</span>
                <span class="conf-valor">${reserva.fechaRegistro}</span>
            </div>

        </div>

        <%-- Nota --%>
        <c:if test="${reserva.activa}">
            <div class="confirmacion-nota" style="margin-top:1.25rem;">
                <span>📌</span>
                <span>Presenta tu código <strong>${reserva.codigoConfirmacion}</strong>
          al llegar al museo. Llega con al menos 10 minutos de anticipación.</span>
            </div>
        </c:if>

        <%-- Acciones --%>
        <div class="confirmacion-acciones" style="margin-top:1.5rem;">
            <a href="${pageContext.request.contextPath}/reservas/mis-reservas"
               class="btn btn-secondary">
                ← Volver a mis reservas
            </a>

            <c:if test="${reserva.activa}">
                <a href="${pageContext.request.contextPath}/reservas/modificar?reservaId=${reserva.id}"
                   class="btn btn-secondary">
                    ✏️ Modificar
                </a>
                <form method="post"
                      action="${pageContext.request.contextPath}/reservas/cancelar"
                      onsubmit="return confirm('¿Cancelar esta reserva? El cupo será liberado.')">
                    <input type="hidden" name="reservaId" value="${reserva.id}"/>
                    <button type="submit" class="btn btn-cancelar">🗑️ Cancelar reserva</button>
                </form>
            </c:if>
        </div>

    </div>
</div>

<%@ include file="/WEB-INF/includes/footer.jsp" %>