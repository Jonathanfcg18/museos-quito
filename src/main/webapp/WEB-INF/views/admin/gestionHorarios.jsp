<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Gestión de Horarios – ${admin.museo.nombre}</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@400;700;900&family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css"/>
  <script src="${pageContext.request.contextPath}/js/settings.js" defer></script>
</head>
<body>

<header class="navbar">
  <div class="navbar-brand">
    <span class="logo-icon">🏛️</span>
    <div>
      <span class="brand-title">Panel Administrador</span>
      <span class="brand-sub">${admin.museo.nombre}</span>
    </div>
  </div>
  <nav class="navbar-links">
    <span style="color:rgba(255,255,255,0.8);font-size:0.85rem;">👤 ${admin.nombre}</span>
    <a href="${pageContext.request.contextPath}/admin/logout">Cerrar sesión</a>
    <button class="settings-btn" onclick="openSettings()" aria-label="Configuración">⚙️</button>
  </nav>
</header>

<main class="main-content">
<div class="container">

  <div class="page-header">
    <h1>📅 Gestión de Horarios y Aforo</h1>
    <p>Crea, modifica y controla las franjas horarias de <strong>${admin.museo.nombre}</strong>.</p>
  </div>

  <c:if test="${not empty mensaje}">
    <div class="alert alert-success">${mensaje}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <div class="admin-layout">

    <!-- Formulario crear -->
    <div class="detalle-card">
      <h2>➕ Nueva franja horaria</h2>
      <p class="text-muted" style="font-size:0.85rem;margin-bottom:1rem;">
        Todos los campos son obligatorios. El aforo máximo es requerido.
      </p>

      <form method="post" action="${pageContext.request.contextPath}/admin/horarios">
        <input type="hidden" name="accion" value="crear"/>

        <div class="form-group">
          <label for="fecha">Fecha</label>
          <input type="date" id="fecha" name="fecha" class="form-control" required/>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="horaInicio">Hora inicio</label>
            <input type="time" id="horaInicio" name="horaInicio" class="form-control" required/>
          </div>
          <div class="form-group">
            <label for="horaFin">Hora fin</label>
            <input type="time" id="horaFin" name="horaFin" class="form-control" required/>
          </div>
        </div>

        <div class="form-group">
          <label for="aforoMaximo">
            Aforo máximo de visitantes
            <span class="campo-requerido">* obligatorio</span>
          </label>
          <input type="number" id="aforoMaximo" name="aforoMaximo"
                 class="form-control" min="1" required placeholder="Ej: 20"/>
          <small class="form-hint">
            Número máximo de personas. Sin este valor no se puede guardar.
          </small>
        </div>

        <button type="submit" class="btn btn-primary btn-full">
          Guardar franja horaria
        </button>
      </form>
    </div>

    <!-- Lista franjas -->
    <div>
      <div style="display:flex;align-items:center;justify-content:space-between;
                  margin-bottom:1rem;flex-wrap:wrap;gap:0.5rem;">
        <h2 style="color:var(--primary);margin:0;font-family:var(--font-display);">
          📋 Franjas horarias registradas
          <span class="text-muted" style="font-size:0.82rem;font-weight:400;">
            (${franjas.size()} total)
          </span>
        </h2>
        <div class="admin-filtro-fecha">
          <label style="font-size:0.82rem;color:var(--text-muted);">Filtrar:</label>
          <input type="date" id="filtroFecha" class="form-control"
                 style="width:auto;padding:0.4rem 0.7rem;font-size:0.82rem;"
                 onchange="filtrarFranjas()"/>
          <button class="btn btn-secondary" style="padding:0.4rem 0.8rem;font-size:0.82rem;"
                  onclick="limpiarFiltro()">✕</button>
        </div>
      </div>

      <c:choose>
        <c:when test="${empty franjas}">
          <div class="empty-state">
            <span class="empty-icon">📅</span>
            <h3>No hay franjas registradas</h3>
            <p>Crea la primera franja horaria con el formulario.</p>
          </div>
        </c:when>
        <c:otherwise>
          <div class="franjas-admin-lista" id="franjasList">
            <c:forEach var="franja" items="${franjas}">
              <div class="franja-admin-item" data-fecha="${franja.fecha}">
                <div class="franja-admin-info">
                  <div class="franja-admin-fecha">
                    📅 ${franja.fecha} &nbsp;🕐 ${franja.horaInicio} – ${franja.horaFin}
                  </div>
                  <div>
                    <c:choose>
                      <c:when test="${franja.hayCupos()}">
                        <span class="aforo-disponible">
                          ✅ ${franja.aforoOcupado}/${franja.aforoMaximo} reservas
                          — ${franja.cuposDisponibles} cupo(s) libre(s)
                        </span>
                      </c:when>
                      <c:otherwise>
                        <span class="aforo-agotado">
                          🚫 AGOTADO — ${franja.aforoOcupado}/${franja.aforoMaximo} reservas
                        </span>
                      </c:otherwise>
                    </c:choose>
                  </div>
                </div>

                <div class="franja-admin-acciones">
                  <button class="btn btn-secondary"
                          onclick="abrirEdicion('${franja.id}','${franja.fecha}',
                                   '${franja.horaInicio}','${franja.horaFin}',
                                   '${franja.aforoMaximo}')">
                    ✏️ Editar
                  </button>
                  <c:if test="${franja.aforoOcupado == 0}">
                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/horarios"
                          onsubmit="return confirm('¿Eliminar esta franja?')">
                      <input type="hidden" name="accion"   value="eliminar"/>
                      <input type="hidden" name="franjaId" value="${franja.id}"/>
                      <button type="submit" class="btn btn-cancelar">🗑️ Eliminar</button>
                    </form>
                  </c:if>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</div>
</main>

<!-- Modal edición -->
<div id="modalEdicion" class="modal-overlay" style="display:none;">
  <div class="modal-box">
    <h2>✏️ Modificar franja horaria</h2>

    <form method="post" action="${pageContext.request.contextPath}/admin/horarios">
      <input type="hidden" name="accion"   value="modificar"/>
      <input type="hidden" name="franjaId" id="editFranjaId"/>

      <div class="form-group">
        <label for="editFecha">Fecha</label>
        <input type="date" id="editFecha" name="fecha" class="form-control" required/>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label for="editHoraInicio">Hora inicio</label>
          <input type="time" id="editHoraInicio" name="horaInicio" class="form-control" required/>
        </div>
        <div class="form-group">
          <label for="editHoraFin">Hora fin</label>
          <input type="time" id="editHoraFin" name="horaFin" class="form-control" required/>
        </div>
      </div>
      <div class="form-group">
        <label for="editAforo">
          Aforo máximo <span class="campo-requerido">* obligatorio</span>
        </label>
        <input type="number" id="editAforo" name="aforoMaximo"
               class="form-control" min="1" required/>
        <small class="form-hint">No puede ser menor al número de reservas actuales.</small>
      </div>

      <div style="display:flex;gap:0.75rem;margin-top:1.25rem;">
        <button type="submit" class="btn btn-primary" style="flex:1;">Guardar cambios</button>
        <button type="button" class="btn btn-secondary" onclick="cerrarEdicion()" style="flex:1;">Cancelar</button>
      </div>
    </form>
  </div>
</div>

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

<footer class="footer">
  <div class="footer-content">
    <p>Portal de Cultura Quito – Panel de Administración</p>
  </div>
</footer>

<script>
function abrirEdicion(id, fecha, inicio, fin, aforo) {
  document.getElementById('editFranjaId').value   = id;
  document.getElementById('editFecha').value       = fecha;
  document.getElementById('editHoraInicio').value  = inicio;
  document.getElementById('editHoraFin').value     = fin;
  document.getElementById('editAforo').value       = aforo;
  document.getElementById('modalEdicion').style.display = 'flex';
}
function cerrarEdicion() {
  document.getElementById('modalEdicion').style.display = 'none';
}
function filtrarFranjas() {
  var fecha = document.getElementById('filtroFecha').value;
  document.querySelectorAll('.franja-admin-item').forEach(function (item) {
    item.style.display = (!fecha || item.dataset.fecha === fecha) ? '' : 'none';
  });
}
function limpiarFiltro() {
  document.getElementById('filtroFecha').value = '';
  document.querySelectorAll('.franja-admin-item').forEach(function (item) {
    item.style.display = '';
  });
}
document.getElementById('modalEdicion').addEventListener('click', function (e) {
  if (e.target === this) cerrarEdicion();
});
</script>
</body>
</html>
