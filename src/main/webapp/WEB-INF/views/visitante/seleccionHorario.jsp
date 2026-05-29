<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fn" prefix="fn" %>
<c:set var="pageTitle" value="Reservar visita – ${museo.nombre}"/>
<c:set var="currentPage" value="museos"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

  <nav class="breadcrumb" aria-label="Ruta de navegación">
    <a href="${pageContext.request.contextPath}/">Inicio</a>
    <span aria-hidden="true">›</span>
    <a href="${pageContext.request.contextPath}/museos">Museos</a>
    <span aria-hidden="true">›</span>
    <a href="${pageContext.request.contextPath}/museos/detalle?id=${museo.id}">${museo.nombre}</a>
    <span aria-hidden="true">›</span>
    <span aria-current="page">Reservar visita</span>
  </nav>

  <div class="page-header">
    <h1>🎫 Reservar visita</h1>
    <p><strong>${museo.nombre}</strong> — ${museo.ubicacion}</p>
  </div>

  <%-- Escenario 2 HU2: sin cupos --%>
  <c:if test="${not empty param.error}">
    <div class="alert alert-error">${param.error}</div>
  </c:if>

  <c:choose>
    <c:when test="${empty franjas}">
      <div class="empty-state">
        <span class="empty-icon">📅</span>
        <h3>No hay horarios disponibles</h3>
        <p>Este museo no tiene franjas horarias habilitadas por el momento.</p>
        <a href="${pageContext.request.contextPath}/museos/detalle?id=${museo.id}"
           class="btn btn-secondary">← Volver al museo</a>
      </div>
    </c:when>
    <c:otherwise>
      <div class="reserva-layout">

        <%-- Lista de franjas horarias --%>
        <div class="franjas-panel">
          <h2>Selecciona fecha y horario</h2>
          <p class="text-muted">Los horarios marcados como agotados no pueden seleccionarse.</p>

          <%-- Filtro de fechas dinámico --%>
          <div class="fecha-filtro">
            <label>Filtrar por fecha:</label>
            <div class="fecha-tabs" id="fechaTabs">
              <button class="fecha-tab active"
                      onclick="filtrarFecha('todas', this)">Todas</button>
              <c:set var="ultimaFecha" value=""/>
              <c:forEach var="franja" items="${franjas}">
                <c:if test="${franja.fecha != ultimaFecha}">
                  <button class="fecha-tab"
                          onclick="filtrarFecha('${franja.fecha}', this)">
                    ${fn:substring(franja.fecha,8,10)}/${fn:substring(franja.fecha,5,7)}
                  </button>
                  <c:set var="ultimaFecha" value="${franja.fecha}"/>
                </c:if>
              </c:forEach>
            </div>
          </div>

          <div class="franjas-lista" id="franjaLista">
            <c:forEach var="franja" items="${franjas}">
              <div class="franja-item ${franja.hayCupos() ? 'franja-disponible' : 'franja-agotada'}"
                   data-fecha="${franja.fecha}"
                   data-id="${franja.id}"
                   data-inicio="${franja.horaInicio}"
                   data-fin="${franja.horaFin}"
                   data-cupos="${franja.cuposDisponibles}"
                   <c:if test="${franja.hayCupos()}">onclick="seleccionarFranja(this)"</c:if>>
                <div class="franja-info">
                  <span class="franja-fecha">📅 ${franja.fecha}</span>
                  <span class="franja-hora">🕐 ${franja.horaInicio} – ${franja.horaFin}</span>
                </div>
                <div class="franja-estado">
                  <c:choose>
                    <c:when test="${franja.hayCupos()}">
                      <span class="cupos-disponibles">✅ ${franja.cuposDisponibles} cupo(s)</span>
                    </c:when>
                    <c:otherwise>
                      <span class="cupos-agotados">🚫 Agotado</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </c:forEach>
          </div>
        </div>

        <%-- Formulario de confirmación --%>
        <div class="form-reserva-panel">
          <h2>Tus datos</h2>

          <div id="seleccionResumen" class="seleccion-resumen" style="display:none;">
            <p>📅 Fecha: <strong id="resumenFecha"></strong></p>
            <p>🕐 Horario: <strong id="resumenHorario"></strong></p>
            <p>🎟️ Cupos disponibles: <strong id="resumenCupos"></strong></p>
          </div>
          <p id="sinSeleccion" class="text-muted">← Selecciona un horario disponible.</p>

          <form method="post" action="${pageContext.request.contextPath}/reservas"
                id="formReserva" style="display:none;" novalidate>
            <input type="hidden" name="franjaId" id="inputFranjaId"/>
            <input type="hidden" name="museoId"  value="${museo.id}"/>

            <div class="form-group">
              <label for="nombre">Nombre completo</label>
              <input type="text" id="nombre" name="nombre" class="form-control"
                     placeholder="Ej: Juan Pérez" required maxlength="100"
                     value="${not empty sessionScope.usuarioSesion ? sessionScope.usuarioSesion.nombre : ''}"/>
            </div>

            <div class="form-group">
              <label for="email">Correo electrónico</label>
              <input type="email" id="email" name="email" class="form-control"
                     placeholder="ejemplo@correo.com" required maxlength="150"
                     value="${not empty sessionScope.usuarioSesion ? sessionScope.usuarioSesion.email : ''}"/>
            </div>

            <div class="form-group">
              <label for="cantidad">Número de personas</label>
              <input type="number" id="cantidad" name="cantidad"
                     class="form-control" value="1" min="1" required/>
            </div>

            <button type="submit" class="btn btn-primary btn-full">
              Confirmar reserva →
            </button>
          </form>

          <div id="errorCupos" class="alert alert-error" style="display:none;margin-top:1rem;">
            🚫 El horario seleccionado ya no tiene cupos. Elige otra opción.
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<script>
  function filtrarFecha(fecha, btn) {
    document.querySelectorAll('.fecha-tab').forEach(function(b) {
      b.classList.remove('active');
    });
    btn.classList.add('active');
    document.querySelectorAll('#franjaLista .franja-item').forEach(function(item) {
      item.style.display =
        (fecha === 'todas' || item.dataset.fecha === fecha) ? '' : 'none';
    });
    // Si la franja seleccionada quedó oculta, limpiar selección
    var sel = document.querySelector('.franja-seleccionada');
    if (sel && sel.style.display === 'none') {
      sel.classList.remove('franja-seleccionada');
      document.getElementById('seleccionResumen').style.display = 'none';
      document.getElementById('sinSeleccion').style.display     = 'block';
      document.getElementById('formReserva').style.display      = 'none';
    }
  }

  function seleccionarFranja(el) {
    document.querySelectorAll('.franja-item').forEach(function(f) {
      f.classList.remove('franja-seleccionada');
    });
    el.classList.add('franja-seleccionada');

    document.getElementById('resumenFecha').textContent   = el.dataset.fecha;
    document.getElementById('resumenHorario').textContent =
      el.dataset.inicio + ' – ' + el.dataset.fin;
    document.getElementById('resumenCupos').textContent   = el.dataset.cupos;
    document.getElementById('inputFranjaId').value        = el.dataset.id;
    document.getElementById('cantidad').max               = el.dataset.cupos;

    document.getElementById('seleccionResumen').style.display = 'block';
    document.getElementById('sinSeleccion').style.display     = 'none';
    document.getElementById('formReserva').style.display      = 'block';
    document.getElementById('errorCupos').style.display       = 'none';
  }
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
