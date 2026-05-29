<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle"   value="Mis Reservas – Portal de Cultura Quito"/>
<c:set var="currentPage" value="misreservas"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

  <div class="page-header">
    <h1>📋 Mis Reservas</h1>
    <p>Consulta, cancela o modifica tus reservas activas.</p>
  </div>

  <!-- Buscador por email -->
  <div class="filtros-bar">
    <form method="get" action="${pageContext.request.contextPath}/reservas/mis-reservas">
      <div class="input-group">
        <input type="email" name="email"
               value="${not empty email ? email : ''}"
               placeholder="Ingresa tu correo electrónico..."
               class="input-busqueda" required/>
        <button type="submit" class="btn btn-primary">🔍 Buscar mis reservas</button>
      </div>
    </form>
  </div>

  <c:if test="${not empty mensaje}">
    <div class="alert alert-success">${mensaje}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <!-- Resultados -->
  <c:if test="${not empty email}">
    <c:choose>
      <c:when test="${empty reservas}">
        <div class="empty-state">
          <span class="empty-icon">📭</span>
          <h3>No tienes reservas activas</h3>
          <p>No encontramos reservas activas para <strong>${email}</strong>.</p>
          <a href="${pageContext.request.contextPath}/museos" class="btn btn-primary">
            Ver museos disponibles
          </a>
        </div>
      </c:when>
      <c:otherwise>
        <p class="text-muted" style="margin-bottom:1.5rem;">
          Se encontraron <strong>${totalReservas}</strong> reserva(s) activa(s)
          para <strong>${email}</strong>.
        </p>

        <div class="reservas-lista">
          <c:forEach var="reserva" items="${reservas}">
            <div class="reserva-card-panel">

              <div class="reserva-panel-header">
                <div>
                  <span class="reserva-codigo">${reserva.codigoConfirmacion}</span>
                  <span class="badge badge-active">Activa</span>
                </div>
                <span class="reserva-fecha-reg">Registrada: ${reserva.fechaRegistro}</span>
              </div>

              <div class="reserva-panel-body">
                <div class="reserva-info-grid">
                  <div>
                    <span class="info-label">🏛️ Museo</span>
                    <span class="info-valor">${reserva.franja.museo.nombre}</span>
                  </div>
                  <div>
                    <span class="info-label">📅 Fecha</span>
                    <span class="info-valor">${reserva.franja.fecha}</span>
                  </div>
                  <div>
                    <span class="info-label">🕐 Horario</span>
                    <span class="info-valor">${reserva.franja.horaInicio} – ${reserva.franja.horaFin}</span>
                  </div>
                  <div>
                    <span class="info-label">👥 Personas</span>
                    <span class="info-valor">${reserva.cantidadPersonas}</span>
                  </div>
                </div>
              </div>

              <div class="reserva-panel-footer">
                <a href="${pageContext.request.contextPath}/reservas/modificar?reservaId=${reserva.id}&email=${email}"
                   class="btn btn-secondary">
                  ✏️ Modificar fecha/horario
                </a>
                <form method="post"
                      action="${pageContext.request.contextPath}/reservas/cancelar"
                      onsubmit="return confirmarCancelacion()">
                  <input type="hidden" name="reservaId" value="${reserva.id}"/>
                  <input type="hidden" name="email"     value="${email}"/>
                  <button type="submit" class="btn btn-cancelar">🗑️ Cancelar reserva</button>
                </form>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </c:if>

  <!-- Estado inicial si no se ha buscado -->
  <c:if test="${empty email}">
    <div class="empty-state">
      <span class="empty-icon">✉️</span>
      <h3>Ingresa tu correo</h3>
      <p>Escribe tu correo electrónico para consultar tus reservas activas.</p>
    </div>
  </c:if>

</div>

<script>
function confirmarCancelacion() {
  return confirm(
    '¿Estás seguro de que deseas cancelar esta reserva?\n' +
    'Esta acción no se puede deshacer y el cupo será liberado.'
  );
}
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
