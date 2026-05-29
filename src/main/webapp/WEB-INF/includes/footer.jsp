</main>

<footer class="footer" role="contentinfo">
  <div class="footer-content">
    <div class="footer-brand">
      <span class="logo-icon">🏛️</span>
      <span>Portal de Cultura Quito</span>
    </div>
    <p>Sistema de Gestión y Reserva de Visitas a Museos de Quito – Nexus Core</p>
    <p class="footer-sub">Escuela Politécnica Nacional · Calidad de Software</p>
  </div>
</footer>

<!-- ================================================================
     SETTINGS MODAL – incluido aquí para que esté disponible en TODAS
     las páginas que usen footer.jsp (index, museos, detalle, reservas…)
     ================================================================ -->
<div class="settings-modal" id="settingsModal" role="dialog"
     aria-modal="true" aria-labelledby="settings-titulo">
  <div class="settings-panel">
    <div class="settings-header">
      <h2 id="settings-titulo">⚙️ Configuración</h2>
      <button class="settings-close" onclick="closeSettings()"
              aria-label="Cerrar configuración">✕</button>
    </div>
    <div class="settings-body">
      <div class="settings-option">
        <span>Tema</span>
        <label class="theme-switch" aria-label="Cambiar tema">
          <input type="checkbox" id="themeToggle" onchange="toggleTheme()" role="switch"/>
          <span class="slider"></span>
        </label>
        <span id="themeLabel" aria-live="polite">Claro</span>
      </div>
      <div class="settings-option">
        <span id="label-fuente">Tamaño de letra</span>
        <div class="font-size-options" role="group" aria-labelledby="label-fuente">
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

</body>
</html>
