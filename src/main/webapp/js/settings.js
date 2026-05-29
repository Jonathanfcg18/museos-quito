(function () {
  var savedTheme = localStorage.getItem('museos-theme') || 'light';
  var savedFont  = localStorage.getItem('museos-font')  || 'md';

  if (savedTheme === 'dark') document.body.classList.add('dark-mode');
  document.documentElement.classList.add('font-' + savedFont);

  window.toggleTheme = function () {
    var isDark = document.body.classList.toggle('dark-mode');
    localStorage.setItem('museos-theme', isDark ? 'dark' : 'light');
    var label  = document.getElementById('themeLabel');
    var toggle = document.getElementById('themeToggle');
    if (label)  label.textContent = isDark ? 'Oscuro' : 'Claro';
    if (toggle) toggle.checked = isDark;
  };

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

  window.openSettings = function () {
    var modal  = document.getElementById('settingsModal');
    var isDark = document.body.classList.contains('dark-mode');
    var toggle = document.getElementById('themeToggle');
    var label  = document.getElementById('themeLabel');
    var saved  = localStorage.getItem('museos-font') || 'md';
    if (modal)  modal.classList.add('open');
    if (toggle) toggle.checked = isDark;
    if (label)  label.textContent = isDark ? 'Oscuro' : 'Claro';
    document.querySelectorAll('.fs-btn').forEach(function (b) {
      b.classList.toggle('active', b.dataset.size === saved);
    });
  };

  window.closeSettings = function () {
    var modal = document.getElementById('settingsModal');
    if (modal) modal.classList.remove('open');
  };

  document.addEventListener('click', function (e) {
    var modal = document.getElementById('settingsModal');
    if (modal && e.target === modal) closeSettings();
  });
})();
