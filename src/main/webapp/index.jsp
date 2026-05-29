<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <c:set var="pageTitle" value="Inicio – Portal de Cultura Quito" />
    <c:set var="currentPage" value="inicio" />
    <%@ include file="/WEB-INF/includes/header.jsp" %>

      <!-- ===== HERO con carrusel de museos ===== -->
      <section class="hero" aria-label="Bienvenida al Portal de Cultura Quito">

        <div class="hero-carousel" aria-hidden="true">
          <div class="hero-slide" data-index="0"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Museo_Nacional_del_Ecuador_(MUNA),_ingreso.jpg')">
          </div>
          <div class="hero-slide" data-index="1"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Centro_cultural_iberoamericano_%22Capilla_del_Hombre%22_(6267211714).jpg')">
          </div>
          <div class="hero-slide" data-index="2"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/(Museo_Casa_del_Alabado)_1st_floor,_courtyard_stone_floor_(pic.a1a).JPG')">
          </div>
          <div class="hero-slide" data-index="3"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Exposición_Zoom_un_viajje_en_el_agua.jpg')">
          </div>
          <div class="hero-slide" data-index="4"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Capilla_de_Cantuña_(Iglesia_de_San_Francisco,_Quito)_pic._a1_(interior).jpg')">
          </div>
          <div class="hero-slide" data-index="5"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Centro_Cultural_Metropolitano_(pic._a028).JPG')">
          </div>
          <div class="hero-slide" data-index="6"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Ecuador_SanAntoniodePichincha_MuseoIntiNan.JPG')">
          </div>
          <div class="hero-slide" data-index="7"
            style="background-image:url('https://commons.wikimedia.org/wiki/Special:FilePath/Museo_de_la_Ciudad,_Quito_(exterior_sign).JPG')">
          </div>
        </div>

        <div class="hero-overlay" aria-hidden="true"></div>

        <div class="hero-content">
          <span class="hero-eyebrow">Quito, Ecuador</span>
          <h1>Descubre los<br />Museos de Quito</h1>
          <p>Explora la historia, el arte y la cultura de la capital del Ecuador.<br />
            Consulta horarios, exposiciones y reserva tu visita en línea.</p>

          <%-- Botones: cambian según si hay sesión activa o no --%>
            <div style="display:flex;gap:1rem;justify-content:center;flex-wrap:wrap;">
              <c:choose>
                <c:when test="${not empty sessionScope.usuarioSesion}">
                  <%-- Usuario autenticado: acceso directo a museos --%>
                    <a href="${pageContext.request.contextPath}/museos" class="btn btn-primary btn-lg">
                      Ver Museos Disponibles
                    </a>
                </c:when>
                <c:otherwise>
                  <%-- Sin sesión: redirigir al login --%>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">
                      Ver Museos Disponibles
                    </a>
                    <a href="${pageContext.request.contextPath}/login?tab=registro" class="btn btn-lg"
                      style="background:rgba(255,255,255,0.15);color:#fff;border:1px solid rgba(255,255,255,0.4);backdrop-filter:blur(8px);">
                      Crear cuenta gratis
                    </a>
                </c:otherwise>
              </c:choose>
            </div>
        </div>

        <div class="carousel-dots" role="tablist" aria-label="Museos en el carrusel">
          <button class="carousel-dot active" data-index="0" aria-label="Museo Nacional"></button>
          <button class="carousel-dot" data-index="1" aria-label="Capilla del Hombre"></button>
          <button class="carousel-dot" data-index="2" aria-label="Casa del Alabado"></button>
          <button class="carousel-dot" data-index="3" aria-label="YAKU"></button>
          <button class="carousel-dot" data-index="4" aria-label="San Francisco"></button>
          <button class="carousel-dot" data-index="5" aria-label="CCM"></button>
          <button class="carousel-dot" data-index="6" aria-label="Intiñán"></button>
          <button class="carousel-dot" data-index="7" aria-label="Museo Ciudad"></button>
        </div>
      </section>

      <!-- ===== Features con partículas WebGL ===== -->
      <section class="features" aria-labelledby="features-titulo">
        <canvas id="featuresGl" aria-hidden="true"></canvas>
        <div class="container">
          <h2 class="section-title" id="features-titulo">¿Qué puedes hacer aquí?</h2>
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
      <section style="background:linear-gradient(135deg,#3d1c06,#2C1810);padding:2.5rem 2rem;color:#fff;">
        <div class="container">
          <div
            style="display:grid;grid-template-columns:repeat(auto-fit,minmax(150px,1fr));gap:2rem;text-align:center;max-width:800px;margin:0 auto;">
            <div>
              <div
                style="font-family:var(--font-display);font-size:2.5rem;font-weight:900;color:#D4A843;text-shadow:0 0 20px rgba(212,168,67,0.6);">
                8</div>
              <div style="font-size:0.9rem;opacity:0.85;">Museos en el catálogo</div>
            </div>
            <div>
              <div
                style="font-family:var(--font-display);font-size:2.5rem;font-weight:900;color:#D4A843;text-shadow:0 0 20px rgba(212,168,67,0.6);">
                ∞</div>
              <div style="font-size:0.9rem;opacity:0.85;">Horarios por semana</div>
            </div>
            <div>
              <div
                style="font-family:var(--font-display);font-size:2.5rem;font-weight:900;color:#D4A843;text-shadow:0 0 20px rgba(212,168,67,0.6);">
                0</div>
              <div style="font-size:0.9rem;opacity:0.85;">Filas en la entrada</div>
            </div>
          </div>
        </div>
      </section>

      <script>
        /* ============================================================
           CARRUSEL – 100% JS
           ============================================================ */
        (function () {
          var slides = document.querySelectorAll('.hero-slide');
          var dots = document.querySelectorAll('.carousel-dot');
          var current = 0;
          var timer = null;
          var DELAY = 5000;

          function showSlide(index) {
            slides[current].classList.remove('carousel-active');
            dots[current].classList.remove('active');
            current = (index + slides.length) % slides.length;
            slides[current].classList.add('carousel-active');
            dots[current].classList.add('active');
          }

          function startAuto() {
            clearInterval(timer);
            timer = setInterval(function () { showSlide(current + 1); }, DELAY);
          }

          dots.forEach(function (dot) {
            dot.addEventListener('click', function () {
              showSlide(parseInt(dot.dataset.index, 10));
              startAuto();
            });
          });

          showSlide(0);
          startAuto();
        })();

        /* ============================================================
           PARTÍCULAS WebGL – sección features
           ============================================================ */
        (function () {
          var canvas = document.getElementById('featuresGl');
          if (!canvas) return;
          var section = canvas.closest('.features');
          if (!section) return;
          var gl = canvas.getContext('webgl');
          if (!gl) return;

          var PALETTE = [
            [0.42, 0.18, 0.03], [0.29, 0.12, 0.02],
            [0.60, 0.30, 0.10], [0.68, 0.47, 0.05],
            [0.80, 0.65, 0.28], [0.54, 0.32, 0.15],
            [0.20, 0.10, 0.04], [0.36, 0.16, 0.07],
            [0.45, 0.28, 0.10], [0.62, 0.38, 0.16]
          ];
          var NUM = 800;
          var bufData = new Float32Array(NUM * 7);
          var ptcls = [];

          for (var i = 0; i < NUM; i++) {
            var a = Math.random() * Math.PI * 2;
            var c = PALETTE[Math.floor(Math.random() * PALETTE.length)];
            ptcls.push({
              x: Math.cos(a) * Math.random() * 1.2,
              y: Math.sin(a) * Math.random() * 1.2,
              vx: 0, vy: 0,
              size: 5 + Math.random() * 20,
              phase: Math.random() * 100,
              r: c[0], g: c[1], b: c[2],
              alpha: 0.12 + Math.random() * 0.25
            });
          }

          function resize() {
            var rect = section.getBoundingClientRect();
            var w = rect.width, h = rect.height;
            if (!w || !h) return;
            var dpr = Math.min(window.devicePixelRatio || 1, 2);
            canvas.width = w * dpr; canvas.height = h * dpr;
            canvas.style.width = w + 'px';
            canvas.style.height = h + 'px';
            gl.viewport(0, 0, canvas.width, canvas.height);
          }
          window.addEventListener('resize', resize);
          if (window.ResizeObserver) new ResizeObserver(resize).observe(section);
          resize();

          function mkShader(type, src) {
            var sh = gl.createShader(type);
            gl.shaderSource(sh, src); gl.compileShader(sh);
            return sh;
          }
          var prog = gl.createProgram();
          gl.attachShader(prog, mkShader(gl.VERTEX_SHADER,
            'attribute vec2 a_pos;attribute float a_sz;attribute vec4 a_col;' +
            'varying vec4 v_col;void main(){gl_Position=vec4(a_pos,0.,1.);' +
            'gl_PointSize=a_sz;v_col=a_col;}'));
          gl.attachShader(prog, mkShader(gl.FRAGMENT_SHADER,
            'precision mediump float;varying vec4 v_col;' +
            'void main(){vec2 c=gl_PointCoord*2.-1.;float d=length(c);' +
            'if(d>1.)discard;float core=1.-smoothstep(0.,.6,d);' +
            'float glow=exp(-d*3.)*.5;gl_FragColor=vec4(v_col.rgb,(core+glow)*v_col.a);}'));
          gl.linkProgram(prog); gl.useProgram(prog);
          gl.enable(gl.BLEND); gl.blendFunc(gl.SRC_ALPHA, gl.ONE);
          gl.clearColor(0.08, 0.04, 0.01, 1.0);

          var buf = gl.createBuffer();
          gl.bindBuffer(gl.ARRAY_BUFFER, buf);
          gl.bufferData(gl.ARRAY_BUFFER, bufData, gl.DYNAMIC_DRAW);
          var stride = 28;
          var pl = gl.getAttribLocation(prog, 'a_pos');
          gl.enableVertexAttribArray(pl); gl.vertexAttribPointer(pl, 2, gl.FLOAT, false, stride, 0);
          var sl = gl.getAttribLocation(prog, 'a_sz');
          gl.enableVertexAttribArray(sl); gl.vertexAttribPointer(sl, 1, gl.FLOAT, false, stride, 8);
          var cl = gl.getAttribLocation(prog, 'a_col');
          gl.enableVertexAttribArray(cl); gl.vertexAttribPointer(cl, 4, gl.FLOAT, false, stride, 12);

          var mx = 0, my = 0, mActive = false;
          section.addEventListener('mousemove', function (e) {
            var r = canvas.getBoundingClientRect();
            mx = ((e.clientX - r.left) / r.width) * 2 - 1;
            my = -((e.clientY - r.top) / r.height) * 2 + 1;
            mActive = true;
          });
          section.addEventListener('mouseleave', function () { mActive = false; });

          function loop(now) {
            var t = now * 0.001;
            var rR = 0.25, rS = 0.004;
            for (var i = 0; i < NUM; i++) {
              var p = ptcls[i];
              var sp = 0.1 + p.size * 0.008;
              p.vx += (Math.sin(t * 0.25 + p.phase) * 0.4 + Math.sin(t * 0.15 + p.phase * 1.7) * 0.3) * 0.0003 * sp;
              p.vy += (Math.cos(t * 0.20 + p.phase * 1.3) * 0.4 + Math.cos(t * 0.18 + p.phase * 2.1) * 0.3) * 0.0003 * sp;
              p.vx *= 0.98; p.vy *= 0.98;
              if (mActive) {
                var dx = mx - p.x, dy = my - p.y;
                var dist = Math.sqrt(dx * dx + dy * dy);
                if (dist < rR && dist > 0.001) {
                  var f = (rR - dist) / rR * rS / (dist * 0.5 + 0.1);
                  var nd = Math.sqrt(dx * dx + dy * dy);
                  if (nd > 0.001) { p.vx -= (dx / nd) * f; p.vy -= (dy / nd) * f; }
                }
              }
              p.x += p.vx; p.y += p.vy;
              var m = 0.05;
              if (p.x > 1 + m) p.x = -1 - m;
              if (p.x < -1 - m) p.x = 1 + m;
              if (p.y > 1 + m) p.y = -1 - m;
              if (p.y < -1 - m) p.y = 1 + m;
              var idx = i * 7;
              bufData[idx] = p.x; bufData[idx + 1] = p.y; bufData[idx + 2] = p.size;
              bufData[idx + 3] = p.r; bufData[idx + 4] = p.g; bufData[idx + 5] = p.b; bufData[idx + 6] = p.alpha;
            }
            gl.bindBuffer(gl.ARRAY_BUFFER, buf);
            gl.bufferSubData(gl.ARRAY_BUFFER, 0, bufData);
            gl.clear(gl.COLOR_BUFFER_BIT);
            gl.drawArrays(gl.POINTS, 0, NUM);
            requestAnimationFrame(loop);
          }
          requestAnimationFrame(loop);
        })();
      </script>

      <%@ include file="/WEB-INF/includes/footer.jsp" %>