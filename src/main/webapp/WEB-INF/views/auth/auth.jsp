<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Acceso – Portal de Cultura Quito</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700;900&family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css"/>
  <script src="${pageContext.request.contextPath}/js/settings.js" defer></script>
</head>
<body>

<!-- ── Navbar mínima ── -->
<header class="navbar">
  <a href="${pageContext.request.contextPath}/" class="navbar-brand" aria-label="Portal de Cultura Quito">
    <span class="logo-icon">🏛️</span>
    <div>
      <span class="brand-title">Portal de Cultura Quito</span>
      <span class="brand-sub">Gestión de Visitas a Museos</span>
    </div>
  </a>
  <div style="display:flex;align-items:center;gap:0.75rem;">
    <a href="${pageContext.request.contextPath}/museos" class="navbar-links" style="display:flex;gap:0.5rem;">
      <span style="color:rgba(255,255,255,0.85);font-size:0.9rem;padding:0.45rem 0.85rem;">← Museos</span>
    </a>
    <button class="settings-btn" onclick="openSettings()" aria-label="Configuración">⚙️</button>
  </div>
</header>

<main class="auth-wrapper">
  <div class="auth-box">

    <!-- Logo -->
    <div class="auth-logo">
      <span class="auth-logo-icon">🏛️</span>
      <span class="auth-logo-title">Portal de Cultura Quito</span>
    </div>

    <!-- Tabs -->
    <div class="auth-tabs" role="tablist" aria-label="Tipo de acceso">
      <button class="auth-tab active" id="tab-login"
              role="tab" aria-selected="true" aria-controls="panel-login"
              onclick="switchTab('login')">
        Iniciar sesión
      </button>
      <button class="auth-tab" id="tab-registro"
              role="tab" aria-selected="false" aria-controls="panel-registro"
              onclick="switchTab('registro')">
        Crear cuenta
      </button>
    </div>

    <!-- ── PANEL LOGIN ── -->
    <div class="auth-panel active" id="panel-login" role="tabpanel" aria-labelledby="tab-login">
      <h2 class="auth-title">Bienvenido de vuelta</h2>
      <p class="auth-subtitle">Accede con tu correo electrónico y contraseña.</p>

      <c:if test="${not empty errorLogin}">
        <div class="alert alert-error" style="margin-bottom:1rem;">${errorLogin}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/login"
            id="formLogin" novalidate>

        <div class="form-group">
          <label for="loginEmail">Correo electrónico</label>
          <input type="email" id="loginEmail" name="email" class="form-control"
                 value="${not empty emailLogin ? emailLogin : ''}"
                 placeholder="correo@ejemplo.com" required autofocus/>
          <span class="campo-error" id="errLoginEmail" style="display:none;">
            El correo electrónico es obligatorio.
          </span>
        </div>

        <div class="form-group">
          <label for="loginPassword">Contraseña</label>
          <div class="password-wrap">
            <input type="password" id="loginPassword" name="password"
                   class="form-control" placeholder="••••••••" required/>
            <button type="button" class="password-toggle"
                    onclick="togglePass('loginPassword', this)" aria-label="Mostrar contraseña">👁</button>
          </div>
          <span class="campo-error" id="errLoginPassword" style="display:none;">
            La contraseña es obligatoria.
          </span>
        </div>

        <button type="submit" class="btn btn-primary btn-full" style="margin-top:0.75rem;">
          Ingresar →
        </button>
      </form>

      <div class="login-hint">
        <strong>Credenciales de prueba:</strong><br/>
        👤 Visitante: <code>visitante@prueba.ec</code> / <code>Visita2026!</code><br/>
        🏛️ Admin museo: <code>admin.nacional@museos.gob.ec</code> / <code>Admin2026!</code>
      </div>
    </div>

    <!-- ── PANEL REGISTRO ── -->
    <div class="auth-panel" id="panel-registro" role="tabpanel" aria-labelledby="tab-registro">
      <h2 class="auth-title">Crea tu cuenta</h2>
      <p class="auth-subtitle">Regístrate para reservar visitas a los museos de Quito.</p>

      <c:if test="${not empty errorRegistro}">
        <div class="alert alert-error" style="margin-bottom:1rem;">${errorRegistro}</div>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/registro"
            id="formRegistro" novalidate>

        <div class="form-group">
          <label for="regNombre">Nombre completo</label>
          <input type="text" id="regNombre" name="nombre" class="form-control"
                 value="${not empty nombreReg ? nombreReg : ''}"
                 placeholder="Ej: Ana López" maxlength="100" required/>
          <span class="campo-error" id="errRegNombre" style="display:none;">
            El nombre completo es obligatorio.
          </span>
        </div>

        <div class="form-group">
          <label for="regEmail">Correo electrónico</label>
          <input type="email" id="regEmail" name="email" class="form-control"
                 value="${not empty emailReg ? emailReg : ''}"
                 placeholder="ejemplo@correo.com" maxlength="150" required/>
          <span class="campo-error" id="errRegEmail" style="display:none;">
            Ingresa un correo electrónico válido.
          </span>
        </div>

        <div class="form-group">
          <label for="regPassword">Contraseña</label>
          <div class="password-wrap">
            <input type="password" id="regPassword" name="password"
                   class="form-control" placeholder="Mínimo 8 caracteres"
                   minlength="8" required/>
            <button type="button" class="password-toggle"
                    onclick="togglePass('regPassword', this)" aria-label="Mostrar contraseña">👁</button>
          </div>
          <span class="campo-error" id="errRegPassword" style="display:none;">
            La contraseña debe tener al menos 8 caracteres.
          </span>
          <small class="form-hint">Mínimo 8 caracteres.</small>
        </div>

        <div class="form-group">
          <label for="regConfirmar">Confirmar contraseña</label>
          <div class="password-wrap">
            <input type="password" id="regConfirmar" name="confirmar"
                   class="form-control" placeholder="Repite tu contraseña" required/>
            <button type="button" class="password-toggle"
                    onclick="togglePass('regConfirmar', this)" aria-label="Mostrar contraseña">👁</button>
          </div>
          <span class="campo-error" id="errRegConfirmar" style="display:none;">
            Las contraseñas no coinciden.
          </span>
        </div>

        <button type="submit" class="btn btn-primary btn-full" style="margin-top:0.75rem;">
          Crear cuenta →
        </button>
      </form>
    </div>

    <!-- Volver -->
    <a href="${pageContext.request.contextPath}/"
       class="btn btn-secondary btn-full" style="margin-top:1rem;font-size:0.88rem;">
      ← Volver al inicio
    </a>
  </div>
</main>

<!-- Settings modal -->
<div class="settings-modal" id="settingsModal" role="dialog" aria-modal="true" aria-labelledby="settings-titulo">
  <div class="settings-panel">
    <div class="settings-header">
      <h2 id="settings-titulo">⚙️ Configuración</h2>
      <button class="settings-close" onclick="closeSettings()" aria-label="Cerrar configuración">✕</button>
    </div>
    <div class="settings-body">
      <div class="settings-option">
        <span>Tema</span>
        <label class="theme-switch">
          <input type="checkbox" id="themeToggle" onchange="toggleTheme()" role="switch"/>
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
/* ── Switch de tabs login / registro ── */
function switchTab(tab) {
  document.querySelectorAll('.auth-tab').forEach(function (t) {
    t.classList.remove('active');
    t.setAttribute('aria-selected', 'false');
  });
  document.querySelectorAll('.auth-panel').forEach(function (p) {
    p.classList.remove('active');
  });
  document.getElementById('tab-' + tab).classList.add('active');
  document.getElementById('tab-' + tab).setAttribute('aria-selected', 'true');
  document.getElementById('panel-' + tab).classList.add('active');
}

/* ── Toggle visibilidad contraseña ── */
function togglePass(inputId, btn) {
  var inp = document.getElementById(inputId);
  inp.type = inp.type === 'password' ? 'text' : 'password';
  btn.textContent = inp.type === 'password' ? '👁' : '🙈';
}

/* ── Validación login ── */
document.getElementById('formLogin').addEventListener('submit', function (e) {
  var ok = true;
  clearErrors();
  var email = document.getElementById('loginEmail');
  var pass  = document.getElementById('loginPassword');
  if (!email.value.trim()) { showError('errLoginEmail', email); ok = false; }
  if (!pass.value)          { showError('errLoginPassword', pass); ok = false; }
  if (!ok) e.preventDefault();
});

/* ── Validación registro ── */
document.getElementById('formRegistro').addEventListener('submit', function (e) {
  var ok = true;
  clearErrors();
  var nombre    = document.getElementById('regNombre');
  var email     = document.getElementById('regEmail');
  var password  = document.getElementById('regPassword');
  var confirmar = document.getElementById('regConfirmar');
  var rEmail    = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!nombre.value.trim()) { showError('errRegNombre', nombre); ok = false; }
  if (!rEmail.test(email.value.trim())) {
    showError('errRegEmail', email);
    document.getElementById('errRegEmail').textContent =
      email.value.trim() ? 'Ingresa un correo válido.' : 'El correo es obligatorio.';
    ok = false;
  }
  if (password.value.length < 8) {
    showError('errRegPassword', password);
    document.getElementById('errRegPassword').textContent =
      password.value ? 'Mínimo 8 caracteres.' : 'La contraseña es obligatoria.';
    ok = false;
  }
  if (password.value !== confirmar.value || !confirmar.value) {
    showError('errRegConfirmar', confirmar);
    document.getElementById('errRegConfirmar').textContent =
      confirmar.value ? 'Las contraseñas no coinciden.' : 'Debes confirmar tu contraseña.';
    ok = false;
  }
  if (!ok) e.preventDefault();
});

function showError(spanId, field) {
  document.getElementById(spanId).style.display = 'block';
  field.style.borderColor = 'var(--error-color)';
  field.style.boxShadow   = '0 0 0 3px rgba(198,40,40,0.15)';
}

function clearErrors() {
  document.querySelectorAll('.campo-error').forEach(function (el) {
    el.style.display = 'none';
  });
  document.querySelectorAll('.form-control').forEach(function (el) {
    el.style.borderColor = '';
    el.style.boxShadow   = '';
  });
}

/* ── Activar tab según parámetro URL o error de servidor ── */
(function () {
  var params = new URLSearchParams(window.location.search);
  if (params.get('tab') === 'registro') switchTab('registro');
  /* Si hay error de registro, mostrar tab registro */
  var errorReg = document.querySelector('#panel-registro .alert-error');
  if (errorReg) switchTab('registro');
})();
</script>

</body>
</html>
