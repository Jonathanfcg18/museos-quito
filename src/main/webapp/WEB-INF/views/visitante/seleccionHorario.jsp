<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Reservar visita – ${museo.nombre}"/>
<c:set var="currentPage" value="museos"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

    <nav class="breadcrumb">
        <a href="${pageContext.request.contextPath}/">Inicio</a>
        <span>›</span>
        <a href="${pageContext.request.contextPath}/museos">Museos</a>
        <span>›</span>
        <a href="${pageContext.request.contextPath}/museos/detalle?id=${museo.id}">${museo.nombre}</a>
        <span>›</span>
        <span>Reservar visita</span>
    </nav>

    <div class="page-header">
        <h1>🎫 Reservar visita</h1>
        <p><strong>${museo.nombre}</strong> — ${museo.ubicacion}</p>
    </div>

    <%-- Escenario 2 HU2: mensaje de error cuando no hay cupos --%>
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
                    <p class="text-muted">Los horarios marcados en rojo están agotados y no pueden seleccionarse.</p>

                    <div class="franjas-lista" id="franjaLista">
                        <c:forEach var="franja" items="${franjas}">
                            <div class="franja-item <c:choose><c:when test="${franja.hayCupos()}">franja-disponible</c:when><c:otherwise>franja-agotada</c:otherwise></c:choose>"
                                data-id="${franja.id}"
                                data-fecha="${franja.fecha}"
                                data-inicio="${franja.horaInicio}"
                                data-fin="${franja.horaFin}"
                                data-cupos="${franja.cuposDisponibles}"
                                onclick="<c:if test='${franja.hayCupos()}'>seleccionarFranja(this)</c:if>">
                                <div class="franja-info">
                                    <span class="franja-fecha">📅 ${franja.fecha}</span>
                                    <span class="franja-hora">🕐 ${franja.horaInicio} – ${franja.horaFin}</span>
                                </div>

                                <div class="franja-estado">
                                    <c:choose>
                                        <c:when test="${franja.hayCupos()}">
                                            <span class="cupos-disponibles">
                                                ✅ ${franja.cuposDisponibles} cupo(s) disponible(s)
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <%-- Frontend: horario agotado visualmente no seleccionable --%>
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
                          id="formReserva" style="display:none;">
                        <input type="hidden" name="franjaId"  id="inputFranjaId"/>
                        <input type="hidden" name="museoId"   value="${museo.id}"/>

                        <div class="form-group">
                            <label for="nombre">Nombre completo</label>
                            <input type="text" id="nombre" name="nombre"
                                   class="form-control" placeholder="Ej: Juan Pérez"
                                   required maxlength="100"/>
                        </div>

                        <div class="form-group">
                            <label for="email">Correo electrónico</label>
                            <input type="email" id="email" name="email"
                                   class="form-control" placeholder="ejemplo@correo.com"
                                   required maxlength="150"/>
                        </div>

                        <div class="form-group">
                            <label for="cantidad">Número de personas</label>
                            <input type="number" id="cantidad" name="cantidad"
                                   class="form-control" value="1" min="1"
                                   id="inputCantidad" required/>
                        </div>

                        <button type="submit" class="btn btn-primary btn-full">
                            Confirmar reserva →
                        </button>
                    </form>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
    let franjaSeleccionada = null;

    function seleccionarFranja(el) {
        // Quitar selección anterior
        document.querySelectorAll('.franja-item').forEach(function(f) {
            f.classList.remove('franja-seleccionada');
        });

        // Marcar seleccionada
        el.classList.add('franja-seleccionada');
        franjaSeleccionada = el;

        // Actualizar resumen
        document.getElementById('resumenFecha').textContent   = el.dataset.fecha;
        document.getElementById('resumenHorario').textContent = el.dataset.inicio + ' – ' + el.dataset.fin;
        document.getElementById('resumenCupos').textContent   = el.dataset.cupos;
        document.getElementById('inputFranjaId').value        = el.dataset.id;

        // Mostrar formulario
        document.getElementById('seleccionResumen').style.display = 'block';
        document.getElementById('sinSeleccion').style.display     = 'none';
        document.getElementById('formReserva').style.display      = 'block';

        // Limitar cantidad al máximo de cupos
        document.getElementById('cantidad').max = el.dataset.cupos;
    }
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>