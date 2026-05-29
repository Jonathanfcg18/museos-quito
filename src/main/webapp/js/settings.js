(function () {
  /* ── Restaurar preferencias guardadas ── */
  var savedTheme = localStorage.getItem('museos-theme') || 'light';
  var savedFont  = localStorage.getItem('museos-font')  || 'md';

  if (savedTheme === 'dark') document.body.classList.add('dark-mode');
  document.documentElement.classList.add('font-' + savedFont);

  /* ── Toggle de tema ── */
  window.toggleTheme = function () {
    var isDark = document.body.classList.toggle('dark-mode');
    localStorage.setItem('museos-theme', isDark ? 'dark' : 'light');
    var label  = document.getElementById('themeLabel');
    var toggle = document.getElementById('themeToggle');
    if (label)  label.textContent = isDark ? 'Oscuro' : 'Claro';
    if (toggle) toggle.checked    = isDark;
  };

  /* ── Tamaño de fuente ── */
  window.setFontSize = function (level) {
    var html = document.documentElement;
    ['xs', 'sm', 'md', 'lg', 'xl'].forEach(function (s) {
      html.classList.remove('font-' + s);
    });
    html.classList.add('font-' + level);
    localStorage.setItem('museos-font', level);
    document.querySelectorAll('.fs-btn').forEach(function (b) {
      b.classList.toggle('active', b.dataset.size === level);
    });
  };

  /* ── Abrir settings ── */
  window.openSettings = function () {
    var modal = document.getElementById('settingsModal');
    if (!modal) return;
    modal.classList.add('open');

    /* Sincronizar estado del toggle */
    var isDark = document.body.classList.contains('dark-mode');
    var toggle = document.getElementById('themeToggle');
    var label  = document.getElementById('themeLabel');
    if (toggle) toggle.checked     = isDark;
    if (label)  label.textContent  = isDark ? 'Oscuro' : 'Claro';

    /* Marcar botón de fuente activo */
    var saved = localStorage.getItem('museos-font') || 'md';
    document.querySelectorAll('.fs-btn').forEach(function (b) {
      b.classList.toggle('active', b.dataset.size === saved);
    });

    /* Trampa de foco accesible */
    var firstFocusable = modal.querySelector('button, [tabindex]');
    if (firstFocusable) firstFocusable.focus();
  };

  /* ── Cerrar settings ── */
  window.closeSettings = function () {
    var modal = document.getElementById('settingsModal');
    if (modal) modal.classList.remove('open');
  };

  /* ── Cerrar al hacer clic fuera del panel ── */
  document.addEventListener('click', function (e) {
    var modal = document.getElementById('settingsModal');
    if (modal && e.target === modal) closeSettings();
  });

  /* ── Cerrar con Escape ── */
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') closeSettings();
  });

  /* ── Menú móvil ── */
  window.toggleMenu = function () {
    var nav = document.querySelector('.navbar-links');
    var btn = document.querySelector('.nav-toggle');
    if (!nav) return;
    var isOpen = nav.classList.toggle('open');
    if (btn) btn.setAttribute('aria-expanded', isOpen);
  };

  /* ── Cerrar menú móvil al hacer clic en un enlace ── */
  document.addEventListener('click', function (e) {
    if (e.target.closest && e.target.closest('.navbar-links a')) {
      var nav = document.querySelector('.navbar-links');
      if (nav) nav.classList.remove('open');
    }
  });
})();