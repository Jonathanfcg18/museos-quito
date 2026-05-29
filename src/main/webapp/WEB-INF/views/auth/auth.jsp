<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Acceso – Portal de Cultura Quito</title>
      <link rel="preconnect" href="https://fonts.googleapis.com">
      <link
        href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700;900&family=Outfit:wght@300;400;500;600;700&display=swap"
        rel="stylesheet">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css" />
      <script src="${pageContext.request.contextPath}/js/settings.js" defer></script>
      <style>
        /* ── Auth page: fondo propio ── */
        body {
          background: linear-gradient(135deg, #e8ddd0 0%, #f5ede0 50%, #ddd0be 100%);
        }

        body.dark-mode {
          background: linear-gradient(135deg, #0f0805 0%, #1a0f0a 50%, #0a0503 100%);
        }

        .auth-page-wrap {
          min-height: 100vh;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          padding: 2rem 1rem;
        }

        /* Marca arriba */
        .portal-brand {
          display: flex;
          align-items: center;
          gap: 0.6rem;
          margin-bottom: 1.5rem;
          color: #5a2b0b;
        }

        body.dark-mode .portal-brand {
          color: var(--accent);
        }

        .portal-brand-icon {
          font-size: 1.75rem;
        }

        .portal-brand-title {
          font-family: 'Playfair Display', serif;
          font-size: 1rem;
          font-weight: 700;
          color: #5a2b0b;
          display: block;
        }

        body.dark-mode .portal-brand-title {
          color: var(--accent);
        }

        .portal-brand-sub {
          font-size: 0.7rem;
          color: #9a7060;
          display: block;
        }

        /* ── Contenedor principal con efecto split ── */
        .auth-container {
          background: #fff;
          border-radius: 24px;
          box-shadow: 0 8px 40px rgba(90, 43, 11, 0.18);
          position: relative;
          overflow: hidden;
          width: 780px;
          max-width: 100%;
          min-height: 520px;
        }

        body.dark-mode .auth-container {
          background: #1a0f0a;
          box-shadow: 0 8px 40px rgba(0, 0, 0, 0.5), 0 0 40px rgba(212, 168, 67, 0.1);
        }

        /* ── Formularios (mitad izquierda) ── */
        .form-container {
          position: absolute;
          top: 0;
          height: 100%;
          transition: all 0.6s ease-in-out;
        }

        .sign-in {
          left: 0;
          width: 50%;
          z-index: 2;
        }

        .auth-container.active .sign-in {
          transform: translateX(100%);
        }

        .sign-up {
          left: 0;
          width: 50%;
          opacity: 0;
          z-index: 1;
        }

        .auth-container.active .sign-up {
          transform: translateX(100%);
          opacity: 1;
          z-index: 5;
          animation: aparecer 0.6s;
        }

        @keyframes aparecer {

          0%,
          49.99% {
            opacity: 0;
            z-index: 1;
          }

          50%,
          100% {
            opacity: 1;
            z-index: 5;
          }
        }

        /* Formulario interno */
        .form-container form {
          background: #fff;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-direction: column;
          padding: 2.5rem 2.25rem;
          height: 100%;
          gap: 0;
        }

        body.dark-mode .form-container form {
          background: #1a0f0a;
        }

        .form-container h1 {
          font-family: 'Playfair Display', serif;
          font-size: 1.5rem;
          color: #5a2b0b;
          margin-bottom: 0.4rem;
        }

        body.dark-mode .form-container h1 {
          color: var(--accent);
        }

        .form-sub {
          font-size: 0.78rem;
          color: #9a7060;
          margin-bottom: 1.25rem;
          text-align: center;
        }

        /* Inputs del auth */
        .auth-input {
          background: #F5EDE3;
          border: 1.5px solid transparent;
          margin: 5px 0;
          padding: 0.65rem 1rem;
          font-size: 0.85rem;
          font-family: 'Outfit', sans-serif;
          border-radius: 8px;
          width: 100%;
          outline: none;
          color: #271510;
          transition: border-color 0.2s, background 0.2s;
        }

        body.dark-mode .auth-input {
          background: #2a1810;
          color: #f5edd8;
        }

        .auth-input::placeholder {
          color: #b09080;
        }

        .auth-input:focus {
          border-color: #7B3A10;
          background: #fff;
        }

        body.dark-mode .auth-input:focus {
          background: #1a0f0a;
        }

        /* Password wrap en auth */
        .auth-pass-wrap {
          position: relative;
          width: 100%;
          margin: 5px 0;
        }

        .auth-pass-wrap .auth-input {
          margin: 0;
          padding-right: 2.8rem;
        }

        .auth-pass-toggle {
          position: absolute;
          right: 0.7rem;
          top: 50%;
          transform: translateY(-50%);
          background: none;
          border: none;
          cursor: pointer;
          font-size: 1rem;
          color: #9a7060;
        }

        /* Botón principal auth */
        .btn-auth-main {
          background: #7B3A10;
          color: #fff;
          font-family: 'Outfit', sans-serif;
          font-size: 0.82rem;
          font-weight: 700;
          letter-spacing: 0.08em;
          text-transform: uppercase;
          padding: 0.65rem 2.5rem;
          border: none;
          border-radius: 8px;
          margin-top: 1rem;
          cursor: pointer;
          transition: background 0.2s, transform 0.15s, box-shadow 0.2s;
          width: 100%;
        }

        .btn-auth-main:hover {
          background: #5a2b0b;
          transform: translateY(-1px);
          box-shadow: 0 4px 16px rgba(123, 58, 16, 0.4);
        }

        /* Alerta */
        .auth-alert {
          width: 100%;
          padding: 0.55rem 0.85rem;
          border-radius: 6px;
          font-size: 0.8rem;
          font-weight: 500;
          margin-bottom: 0.35rem;
        }

        .auth-alert.err {
          background: #FFF0F0;
          color: #B71C1C;
          border-left: 3px solid #B71C1C;
        }

        .auth-alert.ok {
          background: #EBF5EB;
          color: #276127;
          border-left: 3px solid #276127;
        }

        /* Medidor de contraseña */
        .strength-wrap {
          width: 100%;
          margin: 2px 0 4px;
          display: none;
        }

        .strength-wrap.visible {
          display: block;
        }

        .strength-bars {
          display: flex;
          gap: 3px;
          margin-bottom: 2px;
        }

        .sbar {
          height: 3px;
          flex: 1;
          border-radius: 2px;
          background: #e0d0c0;
          transition: background 0.3s;
        }

        .sbar.weak {
          background: #EF5350;
        }

        .sbar.fair {
          background: #FFA726;
        }

        .sbar.good {
          background: #66BB6A;
        }

        .sbar.strong {
          background: #2E7D32;
        }

        .strength-label {
          font-size: 0.7rem;
          color: #9a7060;
        }

        /* Hint credenciales */
        .auth-hint {
          width: 100%;
          margin-top: 0.75rem;
          padding: 0.6rem 0.85rem;
          background: rgba(212, 168, 67, 0.1);
          border: 1px solid rgba(212, 168, 67, 0.3);
          border-radius: 8px;
          font-size: 0.75rem;
          color: #7B3A10;
          line-height: 1.8;
        }

        body.dark-mode .auth-hint {
          color: var(--accent);
        }

        .auth-hint code {
          background: rgba(123, 58, 16, 0.12);
          padding: 0.1rem 0.35rem;
          border-radius: 4px;
          font-size: 0.78rem;
          font-weight: 600;
        }

        /* ── Panel deslizante (toggle) ── */
        .toggle-container {
          position: absolute;
          top: 0;
          left: 50%;
          width: 50%;
          height: 100%;
          overflow: hidden;
          transition: all 0.6s ease-in-out;
          border-radius: 120px 0 0 90px;
          z-index: 1000;
        }

        .auth-container.active .toggle-container {
          transform: translateX(-100%);
          border-radius: 0 120px 90px 0;
        }

        .toggle {
          background: linear-gradient(135deg, #7B3A10 0%, #5a2b0b 50%, #3d1a06 100%);
          color: #fff;
          position: relative;
          left: -100%;
          height: 100%;
          width: 200%;
          transform: translateX(0);
          transition: all 0.6s ease-in-out;
        }

        .toggle::before {
          content: '';
          position: absolute;
          inset: 0;
          background:
            radial-gradient(circle at 75% 25%, rgba(200, 151, 26, 0.25) 0%, transparent 55%),
            radial-gradient(circle at 25% 75%, rgba(200, 151, 26, 0.12) 0%, transparent 45%);
          pointer-events: none;
        }

        .auth-container.active .toggle {
          transform: translateX(50%);
        }

        .toggle-panel {
          position: absolute;
          width: 50%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-direction: column;
          padding: 2rem 2.5rem;
          text-align: center;
          top: 0;
          transition: all 0.6s ease-in-out;
          z-index: 1;
        }

        .toggle-panel .panel-icon {
          font-size: 2.8rem;
          margin-bottom: 1rem;
          display: block;
        }

        .toggle-panel h1 {
          font-family: 'Playfair Display', serif;
          font-size: 1.6rem;
          color: #fff;
          margin-bottom: 0.75rem;
          line-height: 1.2;
        }

        .toggle-panel h1 em {
          font-style: italic;
          color: #e8c55a;
        }

        .toggle-panel p {
          font-size: 0.82rem;
          color: rgba(255, 255, 255, 0.78);
          margin-bottom: 1.5rem;
          font-weight: 300;
          line-height: 1.6;
        }

        .btn-toggle {
          background: transparent;
          border: 2px solid rgba(255, 255, 255, 0.7);
          color: #fff;
          font-family: 'Outfit', sans-serif;
          font-size: 0.78rem;
          font-weight: 700;
          letter-spacing: 0.08em;
          text-transform: uppercase;
          padding: 0.55rem 2rem;
          border-radius: 8px;
          cursor: pointer;
          transition: background 0.2s, border-color 0.2s;
        }

        .btn-toggle:hover {
          background: rgba(255, 255, 255, 0.15);
          border-color: #fff;
        }

        .toggle-left {
          transform: translateX(-200%);
        }

        .auth-container.active .toggle-left {
          transform: translateX(0);
        }

        .toggle-right {
          right: 0;
          transform: translateX(0);
        }

        .auth-container.active .toggle-right {
          transform: translateX(200%);
        }

        /* ── Responsive: móvil ── */
        .mobile-toggle-btns {
          display: none;
        }

        @media (max-width: 640px) {
          .auth-container {
            min-height: auto;
            border-radius: 16px;
          }

          .toggle-container {
            display: none;
          }

          .sign-in,
          .sign-up {
            width: 100%;
            position: relative;
          }

          .auth-container.active .sign-in {
            transform: none;
            display: none;
          }

          .auth-container.active .sign-up {
            transform: none;
            opacity: 1;
            position: relative;
          }

          .sign-up {
            display: none;
          }

          .auth-container.active .sign-up {
            display: flex;
          }

          .mobile-toggle-btns {
            display: flex !important;
            gap: 0.5rem;
            margin-bottom: 1.25rem;
            width: 100%;
          }

          .mobile-toggle-btns button {
            flex: 1;
            padding: 0.5rem;
            border-radius: 8px;
            font-size: 0.82rem;
            font-weight: 700;
            font-family: 'Outfit', sans-serif;
            cursor: pointer;
            border: 2px solid #7B3A10;
            background: transparent;
            color: #7B3A10;
            transition: background 0.2s;
          }

          .mobile-toggle-btns button.active-tab {
            background: #7B3A10;
            color: #fff;
          }
        }

        .back-link {
          margin-top: 1.5rem;
          color: #7B3A10;
          font-size: 0.85rem;
          font-weight: 600;
          text-decoration: none;
          display: inline-flex;
          align-items: center;
          gap: 0.3rem;
        }

        body.dark-mode .back-link {
          color: var(--accent);
        }

        .back-link:hover {
          text-decoration: underline;
        }

        /* settings-btn en esta página */
        .auth-top-bar {
          position: fixed;
          top: 1rem;
          right: 1rem;
          z-index: 200;
        }

        .auth-top-bar .settings-btn {
          background: rgba(123, 58, 16, 0.15);
          border: 1px solid rgba(123, 58, 16, 0.3);
          color: #7B3A10;
        }

        body.dark-mode .auth-top-bar .settings-btn {
          background: rgba(212, 168, 67, 0.15);
          border-color: rgba(212, 168, 67, 0.3);
          color: var(--accent);
        }
      </style>
    </head>

    <body>

      <!-- Botón settings flotante -->
      <div class="auth-top-bar">
        <button class="settings-btn" onclick="openSettings()" aria-label="Configuración">⚙️</button>
      </div>

      <div class="auth-page-wrap">

        <!-- Marca -->
        <div class="portal-brand">
          <span class="portal-brand-icon">🏛️</span>
          <div>
            <span class="portal-brand-title">Portal de Cultura Quito</span>
            <span class="portal-brand-sub">Gestión de Visitas a Museos</span>
          </div>
        </div>

        <!-- Contenedor principal con split animado -->
        <div class="auth-container" id="authContainer">

          <!-- ── FORMULARIO REGISTRO (sign-up, izquierda) ── -->
          <div class="form-container sign-up">
            <form method="post" action="${pageContext.request.contextPath}/registro" id="formRegistro"
              onsubmit="return validarRegistro()">

              <div class="mobile-toggle-btns">
                <button type="button" onclick="irALogin()" id="mobLoginBtn">Ingresar</button>
                <button type="button" class="active-tab" id="mobRegBtn">Registro</button>
              </div>

              <h1>Crear cuenta</h1>
              <p class="form-sub">Regístrate para reservar visitas a los museos</p>

              <c:if test="${not empty errorRegistro}">
                <div class="auth-alert err">${errorRegistro}</div>
              </c:if>
              <div class="auth-alert err" id="regError" style="display:none;"></div>

              <input type="text" name="nombre" id="regNombre" class="auth-input" placeholder="Nombre completo"
                value="${not empty nombreReg ? nombreReg : ''}" />

              <input type="email" name="email" id="regEmail" class="auth-input" placeholder="Correo electrónico"
                value="${not empty emailReg ? emailReg : ''}" />

              <div class="auth-pass-wrap">
                <input type="password" name="password" id="regPass" class="auth-input"
                  placeholder="Contraseña (mín. 8 caracteres)" oninput="evaluarFuerza()" />
                <button type="button" class="auth-pass-toggle" onclick="toggleAuthPass('regPass',this)">👁</button>
              </div>

              <div class="strength-wrap" id="strengthWrap">
                <div class="strength-bars">
                  <div class="sbar" id="sb1"></div>
                  <div class="sbar" id="sb2"></div>
                  <div class="sbar" id="sb3"></div>
                  <div class="sbar" id="sb4"></div>
                </div>
                <span class="strength-label" id="slabel">—</span>
              </div>

              <div class="auth-pass-wrap">
                <input type="password" name="confirmar" id="regPassConf" class="auth-input"
                  placeholder="Confirmar contraseña" />
                <button type="button" class="auth-pass-toggle" onclick="toggleAuthPass('regPassConf',this)">👁</button>
              </div>

              <button type="submit" class="btn-auth-main">Crear cuenta</button>

            </form>
          </div>

          <!-- ── FORMULARIO LOGIN (sign-in, izquierda) ── -->
          <div class="form-container sign-in">
            <form method="post" action="${pageContext.request.contextPath}/login" id="formLogin"
              onsubmit="return validarLogin()">

              <div class="mobile-toggle-btns">
                <button type="button" class="active-tab" id="mobLoginBtn2">Ingresar</button>
                <button type="button" onclick="irARegistro()" id="mobRegBtn2">Registro</button>
              </div>

              <h1>Bienvenido</h1>
              <p class="form-sub">Ingresa para gestionar tus reservas</p>

              <c:if test="${not empty errorLogin}">
                <div class="auth-alert err">${errorLogin}</div>
              </c:if>
              <div class="auth-alert err" id="loginError" style="display:none;"></div>

              <input type="email" name="email" id="loginEmail" class="auth-input" placeholder="Correo electrónico"
                value="${not empty emailLogin ? emailLogin : ''}" />

              <div class="auth-pass-wrap">
                <input type="password" name="password" id="loginPass" class="auth-input" placeholder="Contraseña" />
                <button type="button" class="auth-pass-toggle" onclick="toggleAuthPass('loginPass',this)">👁</button>
              </div>

              <button type="submit" class="btn-auth-main">Ingresar</button>

              <div class="auth-hint">
                <strong>Credenciales de prueba:</strong><br />
                👤 Visitante: <code>visitante@prueba.ec</code> / <code>Visita2026!</code><br />
                🏛️ Admin museo: <code>admin.nacional@museos.gob.ec</code> / <code>Admin2026!</code><br />
                🖥️ Admin sistema: <code>admin.sistema@portalcultura.ec</code> / <code>Sistema2026!</code><br />
                🔴 Cuenta suspendida: <code>suspendido@prueba.ec</code> / <code>Suspendido2026!</code>
              </div>
            </form>
          </div>

          <!-- ── PANEL DESLIZANTE MARRÓN ── -->
          <div class="toggle-container">
            <div class="toggle">

              <!-- Izquierdo: visible cuando estás en registro → va a login -->
              <div class="toggle-panel toggle-left">
                <span class="panel-icon">🔑</span>
                <h1>¡Bienvenido<br />de <em>vuelta</em>!</h1>
                <p>Si ya tienes cuenta, ingresa con tus datos para gestionar tus reservas</p>
                <button type="button" class="btn-toggle" onclick="irALogin()">Iniciar sesión</button>
              </div>

              <!-- Derecho: visible cuando estás en login → va a registro -->
              <div class="toggle-panel toggle-right">
                <span class="panel-icon">🏛️</span>
                <h1>¡Hola,<br /><em>explorador</em>!</h1>
                <p>¿Aún no tienes cuenta? Regístrate gratis y reserva visitas a los museos de Quito</p>
                <button type="button" class="btn-toggle" onclick="irARegistro()">Crear cuenta</button>
              </div>

            </div>
          </div>

        </div><!-- fin .auth-container -->

        <a href="${pageContext.request.contextPath}/" class="back-link">← Volver al portal</a>

      </div><!-- fin .auth-page-wrap -->

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
        var authContainer = document.getElementById('authContainer');

        function irARegistro() {
          authContainer.classList.add('active');
        }
        function irALogin() {
          authContainer.classList.remove('active');
        }

        /* Toggle password */
        function toggleAuthPass(id, btn) {
          var inp = document.getElementById(id);
          inp.type = inp.type === 'password' ? 'text' : 'password';
          btn.textContent = inp.type === 'password' ? '👁' : '🙈';
        }

        /* Validación login */
        function validarLogin() {
          var email = document.getElementById('loginEmail').value.trim();
          var pass = document.getElementById('loginPass').value;
          var err = document.getElementById('loginError');
          if (!email || !pass) {
            err.textContent = 'El correo y la contraseña son obligatorios.';
            err.style.display = 'block';
            return false;
          }
          err.style.display = 'none';
          return true;
        }

        /* Validación registro */
        function validarRegistro() {
          var nombre = document.getElementById('regNombre').value.trim();
          var email = document.getElementById('regEmail').value.trim();
          var pass = document.getElementById('regPass').value;
          var conf = document.getElementById('regPassConf').value;
          var err = document.getElementById('regError');
          var rEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

          if (!nombre) {
            mostrarRegErr('El nombre completo es obligatorio.', err); return false;
          }
          if (!rEmail.test(email)) {
            mostrarRegErr(email ? 'Ingresa un correo válido.' : 'El correo es obligatorio.', err); return false;
          }
          if (pass.length < 8) {
            mostrarRegErr(pass ? 'La contraseña debe tener al menos 8 caracteres.' : 'La contraseña es obligatoria.', err); return false;
          }
          if (pass !== conf) {
            mostrarRegErr('Las contraseñas no coinciden.', err); return false;
          }
          err.style.display = 'none';
          return true;
        }
        function mostrarRegErr(msg, el) {
          el.textContent = msg;
          el.style.display = 'block';
        }

        /* Medidor de fortaleza de contraseña */
        function evaluarFuerza() {
          var pass = document.getElementById('regPass').value;
          var wrap = document.getElementById('strengthWrap');
          var bars = [document.getElementById('sb1'), document.getElementById('sb2'),
          document.getElementById('sb3'), document.getElementById('sb4')];
          var label = document.getElementById('slabel');

          if (!pass) { wrap.classList.remove('visible'); return; }
          wrap.classList.add('visible');

          var score = 0;
          if (pass.length >= 8) score++;
          if (/[A-Z]/.test(pass)) score++;
          if (/[0-9]/.test(pass)) score++;
          if (/[^A-Za-z0-9]/.test(pass)) score++;

          var levels = ['', 'weak', 'fair', 'good', 'strong'];
          var labels = ['', 'Débil', 'Regular', 'Buena', 'Fuerte'];
          bars.forEach(function (b, i) {
            b.className = 'sbar ' + (i < score ? levels[score] : '');
          });
          label.textContent = labels[score] || '—';
        }

        /* Activar panel según ?tab=registro o errores de servidor */
        (function () {
          var params = new URLSearchParams(window.location.search);
          if (params.get('tab') === 'registro') irARegistro();
          /* Si hay error de registro en el DOM, mostrar ese panel */
          var errReg = document.querySelector('.sign-up .auth-alert.err');
          if (errReg && errReg.textContent.trim()) irARegistro();
        })();
      </script>

    </body>

    </html>