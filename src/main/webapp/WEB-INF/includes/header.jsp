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
        <a href="${pageContext.request.contextPath}/reservas/mis-reservas"
           class="${currentPage == 'misreservas' ? 'active' : ''}">Mis Reservas</a>
        <a href="${pageContext.request.contextPath}/admin/login"
           class="${currentPage == 'admin' ? 'active' : ''}">Admin</a>
    </nav>
</header>

<main class="main-content">
    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>
    <c:if test="${not empty mensaje}">
        <div class="alert alert-success">${mensaje}</div>
    </c:if>
