<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Panel Administrador – Portal de Cultura Quito</title>
      <link rel="preconnect" href="https://fonts.googleapis.com">
      <link
        href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700;900&family=Outfit:wght@300;400;500;600;700&display=swap"
        rel="stylesheet">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css" />
      <script src="${pageContext.request.contextPath}/js/settings.js" defer></script>
    </head>

    <body>

      <header class="navbar">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand">
          <span class="logo-icon">🏛️</span>
          <div>
            <span class="brand-title">Portal de Cultura Quito</span>
            <span class="brand-sub">Panel de Administración</span>
          </div>
        </a>
        <button class="settings-btn" onclick="openSettings()" aria-label="Configuración">⚙️</button>
      </header>

      <main class="auth-wrapper">
        <div class="auth-box">

          <div class="auth-logo">
            <span class="auth-logo-icon">🔐</span>
            <span class="auth-logo-title">Acceso Administrador</span>
          </div>

          <p class="auth-subtitle" style="text-align:center;margin-bottom:1.5rem;">
            Ingresa con tus credenciales de administrador del museo.
          </p>

          <c:if test="${not empty error}">
            <div class="alert alert-error" style="margin-bottom:1rem;">${error}</div>
          </c:if>

          <form method="post" action="${pageContext.request.contextPath}/admin/login" class="form-login">

            <div class="form-group">
              <label for="email">Correo electrónico</label>
              <input type="email" id="email" name="email" class="form-control" placeholder="admin@museos.gob.ec"
                required autofocus />
            </div>

            <div class="form-group">
              <label for="password">Contraseña</label>
              <div class="password-wrap">
                <input type="password" id="password" name="password" class="form-control" placeholder="••••••••"
                  required />
                <button type="button" class="password-toggle" onclick="togglePass('password',this)"
                  aria-label="Mostrar contraseña">👁</button>
              </div>
            </div>

            <button type="submit" class="btn btn-primary btn-full" style="margin-top:0.5rem;">
              Ingresar al panel →
            </button>
          </form>

          <div class="login-hint">
            <strong>Credenciales de prueba (panel legacy HU4):</strong><br />
            Email: <code>admin.nacional@museos.gob.ec</code><br />
            Password: <code>admin123</code>
            <br /><br />
            <strong>Login unificado (Sprint 2 — redirige al dashboard):</strong><br />
            Email: <code>admin.nacional@museos.gob.ec</code><br />
            Password: <code>Admin2026!</code>
          </div>

          <a href="${pageContext.request.contextPath}/" class="btn btn-secondary btn-full" style="margin-top:1rem;">
            ← Volver al portal
          </a>
        </div>
      </main>

      <!-- Settings modal -->
      <div class="settings-modal" id="settingsModal" role="dialog" aria-modal="true">
        <div class="settings-panel">
          <div class="settings-header">
            <h2>⚙️ Configuración</h2>
            <button class="settings-close" onclick="closeSettings()">✕</button>
          </div>
          <div class="settings-body">
            <div class="settings-option">
              <span>Tema</span>
              <label class="theme-switch">
                <input type="checkbox" id="themeToggle" onchange="toggleTheme()" role="switch" />
                <span class="slider"></span>
              </label>
              <span id="themeLabel">Claro</span>
            </div>
            <div class="settings-option">
              <span>Tamaño de letra</span>
              <div class="font-size-options">
                <button class="fs-btn" data-size="xs" onclick="setFontSize('xs')">XS</button>
                <button class="fs-btn" data-size="sm" onclick="setFontSize('sm')">S</button>
                <button class="fs-btn active" data-size="md" onclick="setFontSize('md')">M</button>
                <button class="fs-btn" data-size="lg" onclick="setFontSize('lg')">L</button>
                <button class="fs-btn" data-size="xl" onclick="setFontSize('xl')">XL</button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <script>
        function togglePass(id, btn) {
          var inp = document.getElementById(id);
          inp.type = inp.type === 'password' ? 'text' : 'password';
          btn.textContent = inp.type === 'password' ? '👁' : '🙈';
        }
        document.addEventListener('keydown', function (e) {
          if (e.key === 'Enter') document.querySelector('form').submit();
        });
      </script>

    </body>

    </html>