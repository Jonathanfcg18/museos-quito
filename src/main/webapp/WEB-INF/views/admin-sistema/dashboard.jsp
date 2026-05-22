<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Panel Administrador del Sistema – Portal de Cultura Quito</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css"/>
</head>
<body>

<header class="navbar">
    <div class="navbar-brand">
        <span class="logo-icon">🏛️</span>
        <div>
            <span class="brand-title">Portal de Cultura Quito</span>
            <span class="brand-sub">Panel Administrador del Sistema</span>
        </div>
    </div>
    <nav class="navbar-links">
        <span style="color:#fff;opacity:.85;font-size:.85rem;">
            👤 ${usuario.nombre}
        </span>
        <a href="${pageContext.request.contextPath}/logout">
            Cerrar sesión
        </a>
    </nav>
</header>

<main class="main-content">
<div class="container">

    <div class="page-header">
        <h1>🖥️ Panel del Sistema</h1>
        <p>Bienvenido, <strong>${usuario.nombre}</strong>.
           Visión global de la plataforma.</p>
    </div>

    <%-- Métricas globales --%>
    <div class="features-grid" style="max-width:600px;margin-bottom:2rem;">

        <div class="feature-card">
            <span class="feature-icon">🏛️</span>
            <h3 style="font-size:2rem;color:var(--primary);">${totalMuseos}</h3>
            <p>Museos registrados en la plataforma</p>
        </div>

    </div>

    <%-- Lista de museos registrados --%>
    <div class="detalle-card">
        <h2>🏛️ Museos en el sistema</h2>
        <c:choose>
            <c:when test="${empty museos}">
                <p class="text-muted">No hay museos registrados.</p>
            </c:when>
            <c:otherwise>
                <div style="margin-top:1rem;display:flex;
                            flex-direction:column;gap:.5rem;">
                    <c:forEach var="museo" items="${museos}">
                        <div style="display:flex;justify-content:space-between;
                                    align-items:center;padding:.75rem 1rem;
                                    background:var(--bg);border-radius:8px;
                                    border:1px solid var(--border);">
                            <div>
                                <strong>${museo.nombre}</strong>
                                <span style="margin-left:.75rem;
                                             font-size:.82rem;
                                             color:var(--text-muted);">
                                    ${museo.categoria}
                                </span>
                            </div>
                            <div style="display:flex;gap:.5rem;
                                        align-items:center;">
                                <c:choose>
                                    <c:when test="${museo.activo}">
                                        <span class="badge badge-active"
                                              style="font-size:.75rem;">
                                            ✅ Activo
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge"
                                              style="font-size:.75rem;
                                                     background:#FFEBEE;
                                                     color:#C62828;
                                                     border-color:#FFCDD2;">
                                            🔴 Inactivo
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/museos/detalle?id=${museo.id}"
                                   class="btn btn-secondary"
                                   style="padding:.3rem .7rem;font-size:.8rem;">
                                    Ver →
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Accesos rápidos --%>
    <div class="detalle-card" style="margin-top:1.5rem;">
        <h2>⚡ Accesos rápidos</h2>
        <div style="display:flex;gap:1rem;flex-wrap:wrap;margin-top:1rem;">
            <a href="${pageContext.request.contextPath}/museos"
               class="btn btn-secondary">
                🔍 Ver catálogo público
            </a>
            <a href="${pageContext.request.contextPath}/logout"
               class="btn btn-secondary">
                🔓 Cerrar sesión
            </a>
        </div>
    </div>

</div>
</main>

<footer class="footer">
    <div class="footer-content">
        <p>Portal de Cultura Quito – Panel de Administración del Sistema</p>
    </div>
</footer>

</body>
</html>