<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Reservar visita – ${museo.nombre}"/>
<c:set var="currentPage" value="museos"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

    <nav class="breadcrumb" aria-label="Ruta de navegación">
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

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <%-- Mensaje orientativo de disponibilidad según observación Sprint 1 --%>
    <c:set var="hayAgotadas" value="false"/>
    <c:forEach var="f" items="${franjas}">
        <c:if test="${!f.hayCupos()}">
            <c:set var="hayAgotadas" value="true"/>
        </c:if>
    </c:forEach>
    <c:if test="${hayAgotadas}">
        <div class="alert" style="background:rgba(212,168,67,0.12);color:#7B3A10;
         border-left:4px solid #D4A843;margin-bottom:0.5rem;">
            ℹ️ Algunos horarios están agotados (marcados en rojo).
            Por favor selecciona un horario con disponibilidad de cupos para continuar.
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty franjas}">
            <div class="empty-state">
                <span class="empty-icon">📅</span>
                <h3>No hay horarios disponibles</h3>
                <p>Este museo no tiene franjas horarias habilitadas por el momento.</p>
                <a href="${pageContext.request.contextPath}/museos/detalle?id=${museo.id}" class="btn btn-secondary">←
                    Volver al museo</a>
            </div>
        </c:when>
        <c:otherwise>

            <div class="reserva-layout">

                <!-- Lista de franjas -->
                <div class="franjas-panel">
                    <h2>Selecciona fecha y horario</h2>
                    <p class="text-muted" style="margin-bottom:1rem;">
                        Los horarios marcados en rojo están agotados y no pueden seleccionarse.
                    </p>

                    <!-- Filtro de fechas dinámico -->
                    <div class="fecha-filtro">
                        <label>Filtrar por fecha:</label>
                        <div class="fecha-tabs" id="fechaTabs">
                            <button class="fecha-tab active" onclick="filtrarFecha('todas', this)">Todas</button>
                                <%-- Las fechas únicas se generan con JS al cargar --%>
                        </div>
                    </div>

                    <div class="franjas-lista" id="franjaLista">
                        <c:forEach var="franja" items="${franjas}">
                            <%-- HAL-04: determinar si la franja es de fecha pasada --%>
                            <c:set var="hoy" value="<%=java.time.LocalDate.now().toString()%>"/>
                            <c:set var="esPasada" value="${franja.fecha lt hoy}"/>

                            <div class="franja-item
    <c:choose>
      <c:when test='${esPasada}'>franja-agotada</c:when>
      <c:when test='${franja.hayCupos()}'>franja-disponible</c:when>
      <c:otherwise>franja-agotada</c:otherwise>
    </c:choose>"
                                 data-fecha="${franja.fecha}"
                                 data-id="${franja.id}"
                                 data-inicio="${franja.horaInicio}"
                                 data-fin="${franja.horaFin}"
                                 data-cupos="${franja.cuposDisponibles}"
                                 onclick="<c:if
                                         test='${franja.hayCupos() and not esPasada}'>seleccionarFranja(this)</c:if>">

                                <div class="franja-info">
                                    <span class="franja-fecha">📅 ${franja.fecha}</span>
                                    <span class="franja-hora">
        🕐 ${franja.horaInicio} – ${franja.horaFin}
      </span>
                                </div>
                                <div class="franja-estado">
                                    <c:choose>
                                        <c:when test="${esPasada}">
                                            <span class="cupos-agotados">⏰ Fecha pasada</span>
                                        </c:when>
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

                <!-- Formulario -->
                <div class="form-reserva-panel">
                    <h2>Tus datos</h2>

                    <div id="seleccionResumen" class="seleccion-resumen" style="display:none;">
                        <p>📅 Fecha: <strong id="resumenFecha"></strong></p>
                        <p>🕐 Horario: <strong id="resumenHorario"></strong></p>
                        <p>🎟️ Cupos disponibles: <strong id="resumenCupos"></strong></p>
                    </div>
                    <p id="sinSeleccion" class="text-muted">← Selecciona un horario disponible.</p>

                    <form method="post" action="${pageContext.request.contextPath}/reservas" id="formReserva"
                          style="display:none;">
                        <input type="hidden" name="franjaId" id="inputFranjaId"/>
                        <input type="hidden" name="museoId" value="${museo.id}"/>

                        <div class="form-group">
                            <label for="nombre">Nombre completo</label>
                            <input type="text" id="nombre" name="nombre" class="form-control"
                                   placeholder="Ej: Juan Pérez"
                                   required maxlength="100"
                                   value="${not empty sessionScope.usuarioSesion ? sessionScope.usuarioSesion.nombre : ''}"/>
                        </div>
                        <div class="form-group">
                            <label for="emailInput">Correo electrónico</label>
                            <input type="email" id="emailInput" name="email" class="form-control"
                                   placeholder="ejemplo@correo.com" required maxlength="150"
                                   value="${not empty sessionScope.usuarioSesion ? sessionScope.usuarioSesion.email : ''}"/>
                            <c:if test="${not empty sessionScope.usuarioSesion}">
                                <small class="form-hint">✅ Datos tomados de tu cuenta. Puedes modificarlos si lo
                                    deseas.</small>
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label for="cantidad">Número de personas</label>
                            <input type="number" id="cantidad" name="cantidad" class="form-control" value="1" min="1"
                                   required/>
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
    /* ── Generar tabs de fechas únicas ── */
    (function () {
        var items = document.querySelectorAll('.franja-item[data-fecha]');
        var fechas = [];
        items.forEach(function (item) {
            if (fechas.indexOf(item.dataset.fecha) === -1) fechas.push(item.dataset.fecha);
        });
        var tabs = document.getElementById('fechaTabs');
        fechas.forEach(function (f) {
            var btn = document.createElement('button');
            btn.className = 'fecha-tab';
            btn.textContent = f.slice(8) + '/' + f.slice(5, 7); // DD/MM
            btn.onclick = function () {
                filtrarFecha(f, btn);
            };
            tabs.appendChild(btn);
        });
    })();

    function filtrarFecha(fecha, btn) {
        document.querySelectorAll('.fecha-tab').forEach(function (t) {
            t.classList.remove('active');
        });
        btn.classList.add('active');
        document.querySelectorAll('.franja-item').forEach(function (item) {
            item.style.display = (fecha === 'todas' || item.dataset.fecha === fecha) ? '' : 'none';
        });
    }

    function seleccionarFranja(el) {
        document.querySelectorAll('.franja-item').forEach(function (f) {
            f.classList.remove('franja-seleccionada');
        });
        el.classList.add('franja-seleccionada');

        document.getElementById('resumenFecha').textContent = el.dataset.fecha;
        document.getElementById('resumenHorario').textContent = el.dataset.inicio + ' – ' + el.dataset.fin;
        document.getElementById('resumenCupos').textContent = el.dataset.cupos;
        document.getElementById('inputFranjaId').value = el.dataset.id;
        document.getElementById('cantidad').max = el.dataset.cupos;

        document.getElementById('seleccionResumen').style.display = 'block';
        document.getElementById('sinSeleccion').style.display = 'none';
        document.getElementById('formReserva').style.display = 'block';
    }
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>