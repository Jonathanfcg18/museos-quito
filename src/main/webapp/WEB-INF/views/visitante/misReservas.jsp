<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle"   value="Mis Reservas – Portal de Cultura Quito"/>
<c:set var="currentPage" value="misreservas"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

  <div class="page-header">
    <h1>📋 Mis Reservas</h1>
    <p>Consulta, filtra, cancela o modifica tus reservas.</p>
  </div>

  <%--
    HAL-06: El filtro se persiste como parámetro GET en la URL.
    Al recargar la página, el servidor lee ?estado= y lo mantiene activo.
  --%>
  <div class="filtros-bar"
       style="flex-direction:row;align-items:center;gap:1rem;flex-wrap:wrap;">
    <span style="font-weight:600;font-size:0.9rem;color:var(--text-muted);">
      Filtrar por estado:
    </span>
    <div class="filtros-categorias">
      <a href="${pageContext.request.contextPath}/reservas/mis-reservas?estado=todas"
         class="badge ${estadoFiltro == 'todas' ? 'badge-active' : ''}">
        Todas
      </a>
      <a href="${pageContext.request.contextPath}/reservas/mis-reservas?estado=activas"
         class="badge ${estadoFiltro == 'activas' ? 'badge-active' : ''}">
        Activas
      </a>
      <a href="${pageContext.request.contextPath}/reservas/mis-reservas?estado=canceladas"
         class="badge ${estadoFiltro == 'canceladas' ? 'badge-active' : ''}">
        Canceladas
      </a>
      <%-- HAL-05: Nuevo filtro "Pasadas" --%>
      <a href="${pageContext.request.contextPath}/reservas/mis-reservas?estado=pasadas"
         class="badge ${estadoFiltro == 'pasadas' ? 'badge-active' : ''}">
        Pasadas
      </a>
    </div>
  </div>

  <c:if test="${not empty mensaje}">
    <div class="alert alert-success">${mensaje}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <c:choose>
    <c:when test="${empty reservas}">
      <div class="empty-state">
        <span class="empty-icon">📭</span>
        <h3>No tienes reservas registradas</h3>
        <p>Aún no has realizado ninguna reserva.</p>
        <a href="${pageContext.request.contextPath}/museos" class="btn btn-primary">
          Explorar museos disponibles →
        </a>
      </div>
    </c:when>
    <c:otherwise>

      <%--
        Conteo por estado para mostrar el total filtrado.
        El filtrado real se hace en el servidor o en JS según el estado GET.
      --%>
      <c:set var="contadorVisible" value="0"/>
      <c:forEach var="r" items="${reservas}">
        <c:choose>
          <c:when test="${estadoFiltro == 'todas'}">
            <c:set var="contadorVisible" value="${contadorVisible + 1}"/>
          </c:when>
          <c:when test="${estadoFiltro == 'activas' and r.activa and not r.pasada}">
            <c:set var="contadorVisible" value="${contadorVisible + 1}"/>
          </c:when>
          <c:when test="${estadoFiltro == 'canceladas' and not r.activa}">
            <c:set var="contadorVisible" value="${contadorVisible + 1}"/>
          </c:when>
          <c:when test="${estadoFiltro == 'pasadas' and r.pasada}">
            <c:set var="contadorVisible" value="${contadorVisible + 1}"/>
          </c:when>
        </c:choose>
      </c:forEach>

      <p class="text-muted" style="margin-bottom:1rem;">
        Mostrando <strong>${contadorVisible}</strong>
        reserva(s) de <strong>${totalReservas}</strong> en total.
      </p>

      <%-- Mensaje cuando el filtro no da resultados --%>
      <c:if test="${contadorVisible == 0}">
        <div class="empty-state">
          <span class="empty-icon">🔍</span>
          <h3>No hay reservas con ese estado</h3>
          <p>Prueba seleccionando otro filtro.</p>
        </div>
      </c:if>

      <div class="reservas-lista">
        <c:forEach var="reserva" items="${reservas}">

          <%-- Determinar si esta tarjeta debe mostrarse según el filtro --%>
          <c:set var="mostrar" value="false"/>
          <c:choose>
            <c:when test="${estadoFiltro == 'todas'}">
              <c:set var="mostrar" value="true"/>
            </c:when>
            <c:when test="${estadoFiltro == 'activas' and reserva.activa and not reserva.pasada}">
              <c:set var="mostrar" value="true"/>
            </c:when>
            <c:when test="${estadoFiltro == 'canceladas' and not reserva.activa}">
              <c:set var="mostrar" value="true"/>
            </c:when>
            <c:when test="${estadoFiltro == 'pasadas' and reserva.pasada}">
              <c:set var="mostrar" value="true"/>
            </c:when>
          </c:choose>

          <c:if test="${mostrar}">
            <div class="reserva-card-panel">

              <div class="reserva-panel-header">
                <div>
                  <span class="reserva-codigo">${reserva.codigoConfirmacion}</span>
                    <%-- HAL-05: badge de estado según fecha y activa --%>
                  <c:choose>
                    <c:when test="${reserva.pasada and reserva.activa}">
                      <span class="badge"
                            style="background:rgba(100,100,100,0.12);color:#555;
                                   border-color:rgba(100,100,100,0.3);font-size:.75rem;">
                        📅 Pasada
                      </span>
                    </c:when>
                    <c:when test="${reserva.activa}">
                      <span class="badge badge-active">✅ Activa</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge"
                            style="background:rgba(198,40,40,0.1);color:#C62828;
                                   border-color:rgba(198,40,40,0.3);font-size:.75rem;">
                        🚫 Cancelada
                      </span>
                    </c:otherwise>
                  </c:choose>
                </div>
                <span class="reserva-fecha-reg">
                  Registrada: ${reserva.fechaRegistro}
                </span>
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
                    <span class="info-valor">
                      ${reserva.franja.horaInicio} – ${reserva.franja.horaFin}
                    </span>
                  </div>
                  <div>
                    <span class="info-label">👥 Personas</span>
                    <span class="info-valor">${reserva.cantidadPersonas}</span>
                  </div>
                </div>
              </div>

              <div class="reserva-panel-footer">
                  <%-- HU-08: Ver detalle siempre disponible --%>
                <a href="${pageContext.request.contextPath}/reservas/detalle?id=${reserva.id}"
                   class="btn btn-secondary">
                  🔍 Ver detalle
                </a>

                  <%-- Solo permitir modificar/cancelar si está activa Y no es pasada --%>
                <c:if test="${reserva.activa and not reserva.pasada}">
                  <a href="${pageContext.request.contextPath}/reservas/modificar?reservaId=${reserva.id}"
                     class="btn btn-secondary">
                    ✏️ Modificar
                  </a>
                  <form method="post"
                        action="${pageContext.request.contextPath}/reservas/cancelar"
                        onsubmit="return confirmarCancelacion()">
                    <input type="hidden" name="reservaId" value="${reserva.id}"/>
                    <button type="submit" class="btn btn-cancelar">
                      🗑️ Cancelar reserva
                    </button>
                  </form>
                </c:if>
              </div>

            </div>
          </c:if>

        </c:forEach>
      </div>

    </c:otherwise>
  </c:choose>

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