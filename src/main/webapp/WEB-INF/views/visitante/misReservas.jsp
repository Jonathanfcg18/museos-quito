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

  <%-- HU-09: Selector de filtro por estado --%>
  <div class="filtros-bar" style="flex-direction:row;align-items:center;gap:1rem;flex-wrap:wrap;">
    <span style="font-weight:600;font-size:0.9rem;color:var(--text-muted);">Filtrar por estado:</span>
    <div class="filtros-categorias">
      <button class="badge ${estadoFiltro == 'todas'     ? 'badge-active' : ''}"
              onclick="filtrarEstado('todas')">Todas</button>
      <button class="badge ${estadoFiltro == 'activas'   ? 'badge-active' : ''}"
              onclick="filtrarEstado('activas')">Activas</button>
      <button class="badge ${estadoFiltro == 'canceladas' ? 'badge-active' : ''}"
              onclick="filtrarEstado('canceladas')">Canceladas</button>
    </div>
  </div>

  <c:if test="${not empty mensaje}">
    <div class="alert alert-success">${mensaje}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <%-- HU-07 Escenario 2: sin reservas --%>
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

      <%-- Contador dinámico --%>
      <p class="text-muted" style="margin-bottom:1rem;">
        Mostrando <strong id="contadorVisible">${totalReservas}</strong>
        reserva(s) de <strong>${totalReservas}</strong> en total.
      </p>

      <%-- Mensaje vacío al filtrar --%>
      <div id="mensajeFiltroVacio" class="empty-state" style="display:none;">
        <span class="empty-icon">🔍</span>
        <h3>No hay reservas con ese estado</h3>
        <p>Prueba seleccionando otro filtro.</p>
      </div>

      <div class="reservas-lista" id="listaReservas">
        <c:forEach var="reserva" items="${reservas}">
          <%-- data-estado para filtrado JS --%>
          <div class="reserva-card-panel"
               data-estado="${reserva.activa ? 'activas' : 'canceladas'}">

            <div class="reserva-panel-header">
              <div>
                <span class="reserva-codigo">${reserva.codigoConfirmacion}</span>
                <c:choose>
                  <c:when test="${reserva.activa}">
                    <span class="badge badge-active">Activa</span>
                  </c:when>
                  <c:otherwise>
                    <span class="badge"
                          style="background:rgba(198,40,40,0.1);color:#C62828;
                                 border-color:rgba(198,40,40,0.3);font-size:.75rem;">
                      Cancelada
                    </span>
                  </c:otherwise>
                </c:choose>
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
                <%-- HU-08: botón Ver detalle --%>
              <a href="${pageContext.request.contextPath}/reservas/detalle?id=${reserva.id}"
                 class="btn btn-secondary">
                🔍 Ver detalle
              </a>

              <c:if test="${reserva.activa}">
                <a href="${pageContext.request.contextPath}/reservas/modificar?reservaId=${reserva.id}"
                   class="btn btn-secondary">
                  ✏️ Modificar fecha/horario
                </a>
                <form method="post"
                      action="${pageContext.request.contextPath}/reservas/cancelar"
                      onsubmit="return confirmarCancelacion()">
                  <input type="hidden" name="reservaId" value="${reserva.id}"/>
                  <button type="submit" class="btn btn-cancelar">🗑️ Cancelar reserva</button>
                </form>
              </c:if>
            </div>

          </div>
        </c:forEach>
      </div>

    </c:otherwise>
  </c:choose>

</div>

<script>
  /* HU-09: Filtrado por estado en el cliente */
  var estadoActual = '${estadoFiltro}';

  function filtrarEstado(estado) {
    estadoActual = estado;

    // Actualizar badges
    document.querySelectorAll('.filtros-bar .badge').forEach(function(b) {
      b.classList.remove('badge-active');
    });
    event.target.classList.add('badge-active');

    var tarjetas = document.querySelectorAll('.reserva-card-panel');
    var visibles = 0;

    tarjetas.forEach(function(card) {
      var estadoCard = card.getAttribute('data-estado');
      var mostrar = (estado === 'todas') || (estadoCard === estado);
      card.style.display = mostrar ? '' : 'none';
      if (mostrar) visibles++;
    });

    // Actualizar contador
    var contador = document.getElementById('contadorVisible');
    if (contador) contador.textContent = visibles;

    // Mostrar mensaje vacío si aplica
    var msgVacio = document.getElementById('mensajeFiltroVacio');
    if (msgVacio) msgVacio.style.display = visibles === 0 ? 'block' : 'none';
  }

  // Aplicar filtro inicial si viene de servidor
  document.addEventListener('DOMContentLoaded', function() {
    if (estadoActual && estadoActual !== 'todas') {
      var btnActivo = document.querySelector(
              '.filtros-bar .badge[onclick*="' + estadoActual + '"]'
      );
      if (btnActivo) filtrarEstado(estadoActual);
    }
  });

  function confirmarCancelacion() {
    return confirm(
            '¿Estás seguro de que deseas cancelar esta reserva?\n' +
            'Esta acción no se puede deshacer y el cupo será liberado.'
    );
  }
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>