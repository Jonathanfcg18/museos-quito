<div class="settings-modal" id="settingsModal" role="dialog" aria-modal="true" aria-labelledby="settings-titulo">
  <div class="settings-panel">
    <div class="settings-header">
      <h3 id="settings-titulo">⚙️ Configuración</h3>
      <button class="settings-close" onclick="closeSettings()" aria-label="Cerrar configuración">✕</button>
    </div>
    <div class="settings-body">
      <div class="settings-option">
        <span id="label-tema">Tema</span>
        <label class="theme-switch" aria-labelledby="label-tema">
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
