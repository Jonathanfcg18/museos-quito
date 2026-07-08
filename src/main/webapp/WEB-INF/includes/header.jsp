<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>${not empty pageTitle ? pageTitle : 'Portal de Cultura Quito'}</title>
      <link rel="preconnect" href="https://fonts.googleapis.com">
      <link
        href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700;900&family=Outfit:wght@300;400;500;600;700&display=swap"
        rel="stylesheet">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css" />
      <script src="${pageContext.request.contextPath}/js/settings.js" defer></script>
    </head>

    <body>

      <a href="#contenido-principal" class="skip-link">Saltar al contenido principal</a>

      <header class="navbar" role="banner">
        <%--
          HAL: el logo (navbar-brand) y el enlace "Inicio" del menú apuntan a la
          misma URL ("/"), lo que WAVE reporta como "Redundant link". Se oculta
          el logo de la tecnología asistiva (aria-hidden + tabindex="-1"),
          dejando el enlace "Inicio" del menú como único punto de acceso
          equivalente por teclado/lector de pantalla. El logo sigue siendo
          clicable con el mouse para usuarios videntes.
        --%>
        <a href="${pageContext.request.contextPath}/" class="navbar-brand"
          aria-hidden="true" tabindex="-1">
          <span class="logo-icon" aria-hidden="true">🏛️</span>
          <div>
            <span class="brand-title">Portal de Cultura Quito</span>
            <span class="brand-sub">Gestión de Visitas a Museos</span>
          </div>
        </a>

        <nav class="navbar-links" id="nav-menu" aria-label="Navegación principal">
          <a href="${pageContext.request.contextPath}/" class="${currentPage == 'inicio' ? 'active' : ''}">Inicio</a>

          <c:choose>
            <c:when test="${not empty sessionScope.usuarioSesion}">
              <c:set var="u" value="${sessionScope.usuarioSesion}" />

              <%-- Museos: visible solo con sesión --%>
                <a href="${pageContext.request.contextPath}/museos"
                  class="${currentPage == 'museos' ? 'active' : ''}">Museos</a>

                <c:if test="${u.visitante}">
                  <a href="${pageContext.request.contextPath}/reservas/mis-reservas"
                    class="${currentPage == 'misreservas' ? 'active' : ''}">Mis Reservas</a>
                </c:if>

                <c:if test="${u.adminMuseo}">
                  <a href="${pageContext.request.contextPath}/admin-museo/dashboard">Mi Museo</a>
                </c:if>

                <c:if test="${u.adminSistema}">
                  <a href="${pageContext.request.contextPath}/admin-sistema/dashboard">Panel Sistema</a>
                </c:if>

                <span style="color:rgba(255,255,255,0.8);font-size:0.85rem;padding:0.45rem 0.5rem;">
                  👤 ${u.nombre}
                </span>
                <a href="${pageContext.request.contextPath}/logout">Cerrar sesión</a>
            </c:when>
            <c:otherwise>
              <%-- Sin sesión: solo mostrar login y registro --%>
                <a href="${pageContext.request.contextPath}/login"
                  class="${currentPage == 'login' ? 'active' : ''}">Iniciar sesión</a>
                <a href="${pageContext.request.contextPath}/login?tab=registro" class="btn btn-primary"
                  style="padding:0.38rem 0.9rem;font-size:0.85rem;">
                  Registrarse
                </a>
            </c:otherwise>
          </c:choose>

          <button class="settings-btn" onclick="openSettings()" aria-label="Abrir configuración">⚙️</button>
        </nav>

        <button class="nav-toggle" onclick="toggleMenu()" aria-expanded="false" aria-controls="nav-menu"
          aria-label="Abrir menú">☰</button>
      </header>

      <main class="main-content" id="contenido-principal">
        <c:if test="${not empty error}">
          <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty mensaje}">
          <div class="alert alert-success">${mensaje}</div>
        </c:if>