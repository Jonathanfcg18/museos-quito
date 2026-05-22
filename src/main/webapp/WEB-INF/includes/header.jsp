<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${not empty pageTitle ? pageTitle : 'Portal de Cultura Quito'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css"/>
</head>
<body>

<header class="navbar">
    <div class="navbar-brand">
        <span class="logo-icon">🏛️</span>
        <div>
            <span class="brand-title">Portal de Cultura Quito</span>
            <span class="brand-sub">Gestión de Visitas a Museos</span>
        </div>
    </div>
    <nav class="navbar-links">
        <a href="${pageContext.request.contextPath}/"
           class="${currentPage == 'inicio' ? 'active' : ''}">Inicio</a>
        <a href="${pageContext.request.contextPath}/museos"
           class="${currentPage == 'museos' ? 'active' : ''}">Museos</a>

        <c:choose>
            <c:when test="${not empty sessionScope.usuarioSesion}">
                <%-- Usuario con sesión activa --%>
                <c:set var="u" value="${sessionScope.usuarioSesion}"/>

                <c:if test="${u.visitante}">
                    <a href="${pageContext.request.contextPath}/reservas/mis-reservas"
                       class="${currentPage == 'misreservas' ? 'active' : ''}">
                        Mis Reservas
                    </a>
                </c:if>

                <c:if test="${u.adminMuseo}">
                    <a href="${pageContext.request.contextPath}/admin/horarios">
                        Mi Museo
                    </a>
                </c:if>

                <span style="color:#fff;opacity:.85;font-size:.85rem;">
                    👤 ${u.nombre}
                </span>
                <a href="${pageContext.request.contextPath}/logout">
                    Cerrar sesión
                </a>
            </c:when>
            <c:otherwise>
                <%-- Usuario sin sesión --%>
                <a href="${pageContext.request.contextPath}/reservas/mis-reservas"
                   class="${currentPage == 'misreservas' ? 'active' : ''}">
                    Mis Reservas
                </a>
                <a href="${pageContext.request.contextPath}/login"
                   class="${currentPage == 'login' ? 'active' : ''}">
                    Iniciar sesión
                </a>
                <a href="${pageContext.request.contextPath}/registro"
                   class="btn btn-primary"
                   style="padding:.35rem .9rem;font-size:.85rem;">
                    Registrarse
                </a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>

<main class="main-content">
    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>
    <c:if test="${not empty mensaje}">
        <div class="alert alert-success">${mensaje}</div>
    </c:if>