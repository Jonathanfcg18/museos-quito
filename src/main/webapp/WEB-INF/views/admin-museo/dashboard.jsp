<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Panel Administrador – ${nombreMuseo}</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/estilos.css"/>
</head>
<body>

<header class="navbar">
    <div class="navbar-brand">
        <span class="logo-icon">🏛️</span>
        <div>
            <span class="brand-title">Panel Administrador</span>
            <span class="brand-sub">${nombreMuseo}</span>
        </div>
    </div>
    <nav class="navbar-links">
        <a href="${pageContext.request.contextPath}/admin/horarios">
            Gestión de Horarios
        </a>
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
        <h1>📊 Panel de Administración</h1>
        <p>Bienvenido, <strong>${usuario.nombre}</strong>.
           Aquí puedes gestionar el museo <strong>${nombreMuseo}</strong>.</p>
    </div>

    <%-- Tarjetas de métricas --%>
    <div class="features-grid" style="max-width:800px;margin-bottom:2rem;">

        <div class="feature-card">
            <span class="feature-icon">📅</span>
            <h3 style="font-size:2rem;color:var(--primary);">${totalFranjas}</h3>
            <p>Franjas horarias configuradas</p>
            <a href="${pageContext.request.contextPath}/admin/horarios"
               class="btn btn-primary" style="margin-top:.75rem;">
                Gestionar →
            </a>
        </div>

        <div class="feature-card">
            <span class="feature-icon">👥</span>
            <h3 style="font-size:2rem;color:var(--primary);">${totalReservas}</h3>
            <p>Reservas activas confirmadas</p>
        </div>

        <div class="feature-card">
            <span class="feature-icon">🚫</span>
            <h3 style="font-size:2rem;color:#C62828;">${franjasAgotadas}</h3>
            <p>Franjas con aforo agotado</p>
        </div>

    </div>

    <%-- Accesos rápidos --%>
    <div class="detalle-card">
        <h2>⚡ Accesos rápidos</h2>
        <div style="display:flex;gap:1rem;flex-wrap:wrap;margin-top:1rem;">
            <a href="${pageContext.request.contextPath}/admin/horarios"
               class="btn btn-primary">
                📅 Ver y crear franjas horarias
            </a>
            <a href="${pageContext.request.contextPath}/museos/detalle?id=${museoId}"
               class="btn btn-secondary">
                🏛️ Ver mi museo en el catálogo
            </a>
            <a href="${pageContext.request.contextPath}/logout"
               class="btn btn-secondary">
                🔓 Cerrar sesión
            </a>
        </div>
    </div>

    <%-- Vista previa de franjas recientes --%>
    <c:if test="${not empty franjas}">
        <div class="page-header" style="margin-top:2rem;">
            <h2>📋 Franjas horarias más próximas</h2>
        </div>
        <div class="franjas-admin-lista">
            <c:forEach var="franja" items="${franjas}" end="4">
                <div class="franja-admin-item">
                    <div class="franja-admin-info">
                        <div class="franja-admin-fecha">
                            📅 ${franja.fecha}
                            &nbsp;🕐 ${franja.horaInicio} – ${franja.horaFin}
                        </div>
                        <div class="franja-admin-aforo">
                            <c:choose>
                                <c:when test="${franja.hayCupos()}">
                                    <span class="aforo-disponible">
                                        ✅ ${franja.aforoOcupado}/${franja.aforoMaximo}
                                        reservas — ${franja.cuposDisponibles} cupo(s) libre(s)
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="aforo-agotado">
                                        🚫 AGOTADO —
                                        ${franja.aforoOcupado}/${franja.aforoMaximo}
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/admin/horarios"
                       class="btn btn-secondary" style="font-size:.82rem;">
                        Ver panel →
                    </a>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty franjas}">
        <div class="empty-state" style="margin-top:2rem;">
            <span class="empty-icon">📅</span>
            <h3>No hay franjas configuradas</h3>
            <p>Crea la primera franja horaria para empezar a recibir reservas.</p>
            <a href="${pageContext.request.contextPath}/admin/horarios"
               class="btn btn-primary">Crear primera franja →</a>
        </div>
    </c:if>

</div>
</main>

<footer class="footer">
    <div class="footer-content">
        <p>Portal de Cultura Quito – Panel de Administración del Museo</p>
    </div>
</footer>

</body>
</html>