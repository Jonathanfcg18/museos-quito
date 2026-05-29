<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle"   value="Inicio – Portal de Cultura Quito"/>
<c:set var="currentPage" value="inicio"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<!-- ===== HERO con carrusel de museos ===== -->
<section class="hero" aria-label="Bienvenida al Portal de Cultura Quito">

  <!-- Carrusel de fondos (imágenes de los 8 museos del DataSeeder) -->
  <div class="hero-carousel" aria-hidden="true">
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Museo_Nacional_del_Ecuador_(MUNA),_ingreso.jpg')"></div>
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Centro_cultural_iberoamericano_%22Capilla_del_Hombre%22_(6267211714).jpg')"></div>
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/(Museo_Casa_del_Alabado)_1st_floor,_courtyard_stone_floor_(pic.a1a).JPG')"></div>
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Exposición_Zoom_un_viajje_en_el_agua.jpg')"></div>
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Capilla_de_Cantuña_(Iglesia_de_San_Francisco,_Quito)_pic._a1_(interior).jpg')"></div>
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Centro_Cultural_Metropolitano_(pic._a028).JPG')"></div>
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Ecuador_SanAntoniodePichincha_MuseoIntiNan.JPG')"></div>
    <div class="hero-slide" style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Museo_de_la_Ciudad,_Quito_(exterior_sign).JPG')"></div>
  </div>

  <div class="hero-overlay" aria-hidden="true"></div>

  <div class="hero-content">
    <span class="hero-eyebrow">Quito, Ecuador</span>
    <h1>Descubre los<br/>Museos de Quito</h1>
    <p>Explora la historia, el arte y la cultura de la capital del Ecuador.<br/>
       Consulta horarios, exposiciones y reserva tu visita en línea.</p>
    <div style="display:flex;gap:1rem;justify-content:center;flex-wrap:wrap;">
      <a href="${pageContext.request.contextPath}/museos" class="btn btn-primary btn-lg">
        Ver Museos Disponibles
      </a>
      <a href="${pageContext.request.contextPath}/registro" class="btn btn-lg"
         style="background:rgba(255,255,255,0.15);color:#fff;border:1px solid rgba(255,255,255,0.4);backdrop-filter:blur(8px);">
        Crear cuenta gratis
      </a>
    </div>
  </div>

  <!-- Indicadores del carrusel -->
  <div class="carousel-dots" role="tablist" aria-label="Museos en el carrusel">
    <button class="carousel-dot active" aria-label="Museo Nacional"></button>
    <button class="carousel-dot" aria-label="Capilla del Hombre"></button>
    <button class="carousel-dot" aria-label="Casa del Alabado"></button>
    <button class="carousel-dot" aria-label="YAKU"></button>
    <button class="carousel-dot" aria-label="San Francisco"></button>
    <button class="carousel-dot" aria-label="CCM"></button>
    <button class="carousel-dot" aria-label="Intiñán"></button>
    <button class="carousel-dot" aria-label="Museo Ciudad"></button>
  </div>
</section>

<!-- ===== Features ===== -->
<section class="features">
  <div class="container">
    <h2 class="section-title">¿Qué puedes hacer aquí?</h2>
    <div class="features-grid">

      <div class="feature-card" style="animation-delay:0.1s">
        <span class="feature-icon">🔍</span>
        <h3>Consultar Museos</h3>
        <p>Explora el catálogo completo de museos de Quito con información detallada y actualizada.</p>
      </div>

      <div class="feature-card" style="animation-delay:0.2s">
        <span class="feature-icon">📅</span>
        <h3>Ver Horarios</h3>
        <p>Conoce los horarios de atención de cada museo y planifica tu visita con anticipación.</p>
      </div>

      <div class="feature-card" style="animation-delay:0.3s">
        <span class="feature-icon">🎨</span>
        <h3>Exposiciones</h3>
        <p>Descubre las exposiciones permanentes y temporales disponibles en cada museo.</p>
      </div>

      <div class="feature-card" style="animation-delay:0.4s">
        <span class="feature-icon">🎫</span>
        <h3>Reservar Visita</h3>
        <p>Asegura tu ingreso sin hacer filas reservando tu visita en línea en segundos.</p>
      </div>

    </div>
  </div>
</section>

<!-- ===== Stats strip ===== -->
<section style="background:linear-gradient(135deg,var(--primary),var(--primary-dark));padding:2.5rem 2rem;color:#fff;">
  <div class="container">
    <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(150px,1fr));gap:2rem;text-align:center;max-width:800px;margin:0 auto;">
      <div>
        <div style="font-family:var(--font-display);font-size:2.5rem;font-weight:900;color:var(--accent);text-shadow:0 0 20px rgba(212,168,67,0.6);">8</div>
        <div style="font-size:0.9rem;opacity:0.85;">Museos disponibles</div>
      </div>
      <div>
        <div style="font-family:var(--font-display);font-size:2.5rem;font-weight:900;color:var(--accent);text-shadow:0 0 20px rgba(212,168,67,0.6);">∞</div>
        <div style="font-size:0.9rem;opacity:0.85;">Horarios por semana</div>
      </div>
      <div>
        <div style="font-family:var(--font-display);font-size:2.5rem;font-weight:900;color:var(--accent);text-shadow:0 0 20px rgba(212,168,67,0.6);">0</div>
        <div style="font-size:0.9rem;opacity:0.85;">Filas en la entrada</div>
      </div>
    </div>
  </div>
</section>

<script>
/* ── Carrusel con dots interactivos ── */
(function () {
  var dots   = document.querySelectorAll('.carousel-dot');
  var slides = document.querySelectorAll('.hero-slide');
  var current = 0;
  var total   = slides.length;
  var interval;

  function goTo(index) {
    dots[current].classList.remove('active');
    current = (index + total) % total;
    dots[current].classList.add('active');
  }

  function next() { goTo(current + 1); }

  function startAuto() {
    interval = setInterval(next, 5000);
  }

  dots.forEach(function (dot, i) {
    dot.addEventListener('click', function () {
      clearInterval(interval);
      goTo(i);
      startAuto();
    });
  });

  startAuto();
})();
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
