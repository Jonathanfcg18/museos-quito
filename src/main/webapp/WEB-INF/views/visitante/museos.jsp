<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<c:set var="pageTitle" value="Museos de Quito – Portal de Cultura Quito"/>
<c:set var="currentPage" value="museos"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">

    <!-- ===== Encabezado de sección ===== -->
    <div class="page-header">
        <h1>Museos de Quito</h1>
        <p>Explora el catálogo completo de museos disponibles en la ciudad.
           <c:if test="${not empty totalMuseos}">
               Se encontraron <strong>${totalMuseos}</strong> museo(s).
           </c:if>
        </p>
    </div>

    <!-- ===== Buscador y Filtros ===== -->
    <div class="filtros-bar">
        <form method="get" action="${pageContext.request.contextPath}/museos" class="form-busqueda">
            <div class="input-group">
                <input type="text" name="busqueda" placeholder="Buscar museo por nombre..."
                       value="${not empty busqueda ? busqueda : ''}"
                       class="input-busqueda"/>
                <button type="submit" class="btn btn-primary">🔍 Buscar</button>
            </div>
        </form>

        <div class="filtros-categorias">
            <span class="filtros-label">Filtrar por categoría:</span>
            <a href="${pageContext.request.contextPath}/museos"
               class="badge ${empty categoriaSeleccionada ? 'badge-active' : ''}">Todos</a>
            <a href="${pageContext.request.contextPath}/museos?categoria=Arte"
               class="badge ${categoriaSeleccionada == 'Arte' ? 'badge-active' : ''}">Arte</a>
            <a href="${pageContext.request.contextPath}/museos?categoria=Historia"
               class="badge ${categoriaSeleccionada == 'Historia' ? 'badge-active' : ''}">Historia</a>
            <a href="${pageContext.request.contextPath}/museos?categoria=Ciencia+y+Naturaleza"
               class="badge ${categoriaSeleccionada == 'Ciencia y Naturaleza' ? 'badge-active' : ''}">Ciencia</a>
            <a href="${pageContext.request.contextPath}/museos?categoria=Arte+Colonial"
               class="badge ${categoriaSeleccionada == 'Arte Colonial' ? 'badge-active' : ''}">Arte Colonial</a>
            <a href="${pageContext.request.contextPath}/museos?categoria=Arte+Precolombino"
               class="badge ${categoriaSeleccionada == 'Arte Precolombino' ? 'badge-active' : ''}">Precolombino</a>
            <a href="${pageContext.request.contextPath}/museos?categoria=Arte+Contempor%C3%A1neo"
               class="badge ${categoriaSeleccionada == 'Arte Contemporáneo' ? 'badge-active' : ''}">Contemporáneo</a>
            <a href="${pageContext.request.contextPath}/museos?categoria=Ciencia+y+Cultura"
               class="badge ${categoriaSeleccionada == 'Ciencia y Cultura' ? 'badge-active' : ''}">Cultura</a>
        </div>
    </div>

    <!-- ===== Lista de Museos (Tarjetas) ===== -->
    <c:choose>
        <c:when test="${empty museos}">
            <div class="empty-state">
                <span class="empty-icon">🏛️</span>
                <h3>No se encontraron museos</h3>
                <p>Intenta con otra búsqueda o categoría.</p>
                <a href="${pageContext.request.contextPath}/museos" class="btn btn-secondary">Ver todos</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="museos-grid">
                <c:forEach var="museo" items="${museos}">
                    <div class="museo-card">
                        <!-- Imagen -->
                        <div class="museo-card-img">
                            <c:choose>
                                <c:when test="${not empty museo.imagenUrl}">
                                    <img src="${museo.imagenUrl}" alt="${museo.nombre}"
                                         onerror="this.src='${pageContext.request.contextPath}/img/museo-default.svg'"/>
                                </c:when>
                                <c:otherwise>
                                    <div class="museo-img-placeholder">🏛️</div>
                                </c:otherwise>
                            </c:choose>
                            <span class="categoria-badge">${museo.categoria}</span>
                        </div>

                        <!-- Contenido -->
                        <div class="museo-card-body">
                            <h3 class="museo-nombre">${museo.nombre}</h3>
                            <p class="museo-ubicacion">
                                <span class="icon">📍</span> ${museo.ubicacion}
                            </p>
                            <p class="museo-descripcion">
                                ${fn:substring(museo.descripcion, 0, 160)}...
                            </p>

                            <!-- Precios -->
                            <div class="museo-precios">
                                <c:choose>
                                    <c:when test="${museo.precioAdulto == 0}">
                                        <span class="precio-gratuito">🎟️ Entrada gratuita</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="precio-item">
                                            Adulto: <strong>$<fmt:formatNumber value="${museo.precioAdulto}" pattern="#,##0.00"/></strong>
                                        </span>
                                        <span class="precio-item">
                                            Niño: <strong>$<fmt:formatNumber value="${museo.precioNino}" pattern="#,##0.00"/></strong>
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <!-- Acción -->
                        <div class="museo-card-footer">
                            <a href="${pageContext.request.contextPath}/museos/detalle?id=${museo.id}"
                               class="btn btn-primary btn-full">
                                Ver más detalles →
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
