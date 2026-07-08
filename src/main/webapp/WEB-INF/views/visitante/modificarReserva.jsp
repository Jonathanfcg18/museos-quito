<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Modificar Reserva – Portal de Cultura Quito"/>
<c:set var="currentPage" value="museos"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

    <%--
      HAL: el enlace "Inicio" del menú superior y el "Inicio" de esta miga de
      pan apuntan a la misma URL ("/") con el mismo texto, lo que WAVE cuenta
      como enlace redundante. Se añade un aria-label distinto (sin cambiar el
      texto visible) para que ambos enlaces tengan un nombre accesible propio
      acorde a su contexto (menú de navegación vs. ruta de navegación).
    --%>
    <nav class="breadcrumb" aria-label="Ruta de navegación">
        <a href="${pageContext.request.contextPath}/" aria-label="Volver a la página de inicio">Inicio</a>
        <span>›</span>
        <a href="${pageContext.request.contextPath}/reservas/mis-reservas">
            Mis Reservas
        </a>
        <span>›</span>
        <span>Modificar reserva</span>
    </nav>

    <div class="page-header">
        <h1>✏️ Modificar Reserva</h1>
        <p>Selecciona un nuevo horario para tu visita a
           <strong>${reserva.franja.museo.nombre}</strong>.</p>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <%-- Reserva actual --%>
    <div class="detalle-card" style="margin-bottom:1.5rem;">
        <h2>📋 Reserva actual</h2>
        <div class="reserva-info-grid">
            <div>
                <span class="info-label">Código</span>
                <span class="info-valor codigo-valor">${reserva.codigoConfirmacion}</span>
            </div>
            <div>
                <span class="info-label">📅 Fecha actual</span>
                <span class="info-valor">${reserva.franja.fecha}</span>
            </div>
            <div>
                <span class="info-label">🕐 Horario actual</span>
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

    <%-- Selección de nuevo horario --%>
    <div class="reserva-layout">
        <div class="franjas-panel">
            <h2>Selecciona el nuevo horario</h2>
            <p class="text-muted">
                Los horarios agotados no pueden seleccionarse.
                Tu horario actual aparece deshabilitado.
            </p>

            <div class="franjas-lista">
                <c:forEach var="franja" items="${franjas}">
                    <c:set var="esActual"
                           value="${franja.id == reserva.franja.id}"/>
                    <c:set var="disponible"
                           value="${franja.hayCupos() and not esActual}"/>

                    <%--
                      HAL: antes era un <div onclick="..."> — un div no es
                      focuseable ni operable por teclado de forma nativa, por lo
                      que WAVE marca cada uno como "Device dependent event
                      handler" (41 alertas: 1 por cada franja renderizada).
                      Se cambia a <button type="button">: los botones nativos
                      reciben foco con Tab y el navegador dispara el mismo
                      evento click al presionar Enter/Espacio, sin necesitar
                      handlers adicionales de teclado.
                    --%>
                    <button type="button" class="franja-item
                        <c:choose>
                            <c:when test='${esActual}'>franja-agotada</c:when>
                            <c:when test='${disponible}'>franja-disponible</c:when>
                            <c:otherwise>franja-agotada</c:otherwise>
                        </c:choose>"
                         data-id="${franja.id}"
                         data-fecha="${franja.fecha}"
                         data-inicio="${franja.horaInicio}"
                         data-fin="${franja.horaFin}"
                         data-cupos="${franja.cuposDisponibles}"
                         ${disponible ? '' : 'disabled="disabled" aria-disabled="true"'}
                         onclick="<c:if test='${disponible}'>seleccionarFranja(this)</c:if>">

                        <span class="franja-info">
                            <span class="franja-fecha">📅 ${franja.fecha}</span>
                            <span class="franja-hora">
                                🕐 ${franja.horaInicio} – ${franja.horaFin}
                            </span>
                        </span>
                        <span class="franja-estado">
                            <c:choose>
                                <c:when test="${esActual}">
                                    <span class="tipo-badge tipo-permanente">Horario actual</span>
                                </c:when>
                                <c:when test="${disponible}">
                                    <span class="cupos-disponibles">
                                        ✅ ${franja.cuposDisponibles} cupo(s)
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="cupos-agotados">🚫 Agotado</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </button>
                </c:forEach>
            </div>
        </div>

        <%-- Panel de confirmación --%>
        <div class="form-reserva-panel">
            <h2>Confirmar cambio</h2>

            <div id="seleccionResumen" class="seleccion-resumen" style="display:none;">
                <p>📅 Nueva fecha: <strong id="resumenFecha"></strong></p>
                <p>🕐 Nuevo horario: <strong id="resumenHorario"></strong></p>
                <p>🎟️ Cupos disponibles: <strong id="resumenCupos"></strong></p>
            </div>
            <p id="sinSeleccion" class="text-muted">← Selecciona un nuevo horario.</p>

            <form method="post"
                  action="${pageContext.request.contextPath}/reservas/modificar"
                  id="formModificar" style="display:none;">
                <input type="hidden" name="reservaId"     value="${reserva.id}"/>
                <input type="hidden" name="email"         value="${email}"/>
                <input type="hidden" name="nuevaFranjaId" id="inputNuevaFranja"/>

                <button type="submit" class="btn btn-primary btn-full"
                        onclick="return confirm('¿Confirmas el cambio de horario?')">
                    Confirmar modificación →
                </button>
            </form>

            <a href="${pageContext.request.contextPath}/reservas/mis-reservas"
               class="btn btn-secondary btn-full" style="margin-top:0.75rem;">
                ← Volver a mis reservas
            </a>
        </div>
    </div>
</div>

<script>
    function seleccionarFranja(el) {
        document.querySelectorAll('.franja-item').forEach(function(f) {
            f.classList.remove('franja-seleccionada');
        });
        el.classList.add('franja-seleccionada');

        document.getElementById('resumenFecha').textContent   = el.dataset.fecha;
        document.getElementById('resumenHorario').textContent =
            el.dataset.inicio + ' – ' + el.dataset.fin;
        document.getElementById('resumenCupos').textContent   = el.dataset.cupos;
        document.getElementById('inputNuevaFranja').value     = el.dataset.id;

        document.getElementById('seleccionResumen').style.display = 'block';
        document.getElementById('sinSeleccion').style.display     = 'none';
        document.getElementById('formModificar').style.display    = 'block';
    }
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>