<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="pageTitle" value="${museo.nombre} – Portal de Cultura Quito"/>
<c:set var="currentPage" value="museos"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

    <!-- Breadcrumb -->
    <nav class="breadcrumb">
        <a href="${pageContext.request.contextPath}/">Inicio</a>
        <span>›</span>
        <a href="${pageContext.request.contextPath}/museos">Museos</a>
        <span>›</span>
        <span>${museo.nombre}</span>
    </nav>

    <!-- ===== Detalle del Museo ===== -->
    <div class="detalle-museo">

        <!-- Encabezado -->
        <div class="detalle-header">
            <div class="detalle-header-text">
                <span class="categoria-badge categoria-badge-lg">${museo.categoria}</span>
                <h1>${museo.nombre}</h1>
                <p class="museo-ubicacion museo-ubicacion-lg">
                    <span class="icon">📍</span> ${museo.ubicacion}
                </p>
                <c:if test="${not empty museo.telefono}">
                    <p class="museo-contacto">
                        <span class="icon">📞</span> ${museo.telefono}
                    </p>
                </c:if>
                <c:if test="${not empty museo.sitioWeb}">
                    <p class="museo-contacto">
                        <span class="icon">🌐</span>
                        <a href="${museo.sitioWeb}" target="_blank" rel="noopener">${museo.sitioWeb}</a>
                    </p>
                </c:if>
            </div>

            <!-- Imagen principal -->
            <div class="detalle-imagen">
                <c:choose>
                    <c:when test="${not empty museo.imagenUrl}">
                        <img src="${museo.imagenUrl}" alt="${museo.nombre}"
                             onerror="this.src='${pageContext.request.contextPath}/img/museo-default.svg'"/>
                    </c:when>
                    <c:otherwise>
                        <div class="museo-img-placeholder museo-img-placeholder-lg">🏛️</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Grid de contenido -->
        <div class="detalle-grid">

            <!-- Columna izquierda: Descripción y Exposiciones -->
            <div class="detalle-col-main">

                <!-- Descripción -->
                <div class="detalle-card">
                    <h2>📖 Descripción</h2>
                    <p class="descripcion-completa">${museo.descripcion}</p>
                </div>

                <!-- Exposiciones -->
                <div class="detalle-card">
                    <h2>🎨 Exposiciones</h2>
                    <c:choose>
                        <c:when test="${empty museo.exposiciones}">
                            <p class="text-muted">No hay exposiciones registradas.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="exposiciones-lista">
                                <c:forEach var="exp" items="${museo.exposiciones}">
                                    <div class="exposicion-item">
                                        <div class="exposicion-header">
                                            <h4>${exp.titulo}</h4>
                                            <span class="tipo-badge tipo-${exp.tipo == 'Permanente' ? 'permanente' : 'temporal'}">
                                                ${exp.tipo}
                                            </span>
                                        </div>
                                        <p>${exp.descripcion}</p>
                                        <c:if test="${not empty exp.fechaFin}">
                                            <p class="exposicion-fecha">
                                                📅 Hasta: ${exp.fechaFin}
                                            </p>
                                        </c:if>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Columna derecha: Horarios y Precios -->
            <div class="detalle-col-side">

                <!-- Horarios -->
                <div class="detalle-card">
                    <h2>🕐 Horarios</h2>
                    <c:choose>
                        <c:when test="${empty museo.horarios}">
                            <p class="text-muted">Horarios no disponibles.</p>
                        </c:when>
                        <c:otherwise>
                            <ul class="horarios-lista">
                                <c:forEach var="h" items="${museo.horarios}">
                                    <li class="horario-item ${h.cerrado ? 'horario-cerrado' : ''}">
                                        <c:choose>
                                            <c:when test="${h.cerrado}">
                                                <span class="horario-dia">${h.diaSemana}</span>
                                                <span class="horario-hora horario-cerrado-text">Cerrado</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="horario-dia">${h.diaSemana}</span>
                                                <span class="horario-hora">${h.horaApertura} – ${h.horaCierre}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Precios -->
                <div class="detalle-card">
                    <h2>🎟️ Precios de Entrada</h2>
                    <c:choose>
                        <c:when test="${museo.precioAdulto == 0}">
                            <div class="precio-gratuito precio-gratuito-lg">
                                ¡Entrada Gratuita!
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="precios-tabla">
                                <tbody>
                                    <tr>
                                        <td>Adulto</td>
                                        <td><strong>$<fmt:formatNumber value="${museo.precioAdulto}" pattern="#,##0.00"/></strong></td>
                                    </tr>
                                    <tr>
                                        <td>Niño</td>
                                        <td><strong>$<fmt:formatNumber value="${museo.precioNino}" pattern="#,##0.00"/></strong></td>
                                    </tr>
                                    <tr>
                                        <td>Estudiante / Adulto mayor</td>
                                        <td><strong>$<fmt:formatNumber value="${museo.precioEstudiante}" pattern="#,##0.00"/></strong></td>
                                    </tr>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Reservar (stub para HU2 de compañeros) -->
                <div class="detalle-card reserva-card">
                    <h2>🎫 Reservar Visita</h2>
                    <p>Asegura tu ingreso sin hacer filas reservando en línea.</p>
                    <a href="${pageContext.request.contextPath}/museos/horarios?museoId=${museo.id}"
                       class="btn btn-primary btn-full">
                        Reservar Ahora →
                    </a>
                </div>

                <!-- Volver -->
                <a href="${pageContext.request.contextPath}/museos" class="btn btn-secondary btn-full">
                    ← Volver al catálogo
                </a>
            </div>

        </div>
    </div>
</div>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
