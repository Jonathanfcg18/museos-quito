</main>

<footer class="footer" role="contentinfo">
  <div class="footer-content">
    <div class="footer-brand">
      <span aria-hidden="true">🏛️</span>
      <span>Portal de Cultura Quito</span>
    </div>
    <p>Sistema de Gestión y Reserva de Visitas a Museos de Quito</p>
    <p class="footer-sub">Escuela Politécnica Nacional · Calidad de Software · Nexus Core</p>
  </div>
</footer>

<%@ include file="/WEB-INF/includes/settings-modal.jsp" %>

<script>
  function toggleMenuNav() {
    var nav = document.getElementById('nav-menu');
    var btn = document.querySelector('.nav-toggle');
    var isOpen = nav.classList.toggle('open');
    if (btn) btn.setAttribute('aria-expanded', isOpen);
  }
</script>

</body>
</html>
