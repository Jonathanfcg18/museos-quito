<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle"   value="Inicio – Portal de Cultura Quito"/>
<c:set var="currentPage" value="inicio"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<!-- ===== HERO con carrusel de museos ===== -->
<section class="hero" aria-label="Bienvenida al Portal de Cultura Quito">

  <div class="hero-carousel" aria-hidden="true">
    <div class="hero-slide" id="slide-0" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Museo_Nacional_del_Ecuador_%28MUNA%29%2C_ingreso.jpg/1280px-Museo_Nacional_del_Ecuador_%28MUNA%29%2C_ingreso.jpg')"></div>
    <div class="hero-slide" id="slide-1" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Centro_cultural_iberoamericano_%22Capilla_del_Hombre%22_%286267211714%29.jpg/1280px-Centro_cultural_iberoamericano_%22Capilla_del_Hombre%22_%286267211714%29.jpg')"></div>
    <div class="hero-slide" id="slide-2" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/6/69/%28Museo_Casa_del_Alabado%29_1st_floor%2C_courtyard_stone_floor_%28pic.a1a%29.JPG/1280px-%28Museo_Casa_del_Alabado%29_1st_floor%2C_courtyard_stone_floor_%28pic.a1a%29.JPG')"></div>
    <div class="hero-slide" id="slide-3" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Exposici%C3%B3n_Zoom_un_viajje_en_el_agua.jpg/1280px-Exposici%C3%B3n_Zoom_un_viajje_en_el_agua.jpg')"></div>
    <div class="hero-slide" id="slide-4" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Capilla_de_Cantu%C3%B1a_%28Iglesia_de_San_Francisco%2C_Quito%29_pic._a1_%28interior%29.jpg/1280px-Capilla_de_Cantu%C3%B1a_%28Iglesia_de_San_Francisco%2C_Quito%29_pic._a1_%28interior%29.jpg')"></div>
    <div class="hero-slide" id="slide-5" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Centro_Cultural_Metropolitano_%28pic._a028%29.JPG/1280px-Centro_Cultural_Metropolitano_%28pic._a028%29.JPG')"></div>
    <div class="hero-slide" id="slide-6" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Ecuador_SanAntoniodePichincha_MuseoIntiNan.JPG/1280px-Ecuador_SanAntoniodePichincha_MuseoIntiNan.JPG')"></div>
    <div class="hero-slide" id="slide-7" style="background-image:url('https://upload.wikimedia.org/wikipedia/commons/thumb/0/08/Museo_de_la_Ciudad%2C_Quito_%28exterior_sign%29.JPG/1280px-Museo_de_la_Ciudad%2C_Quito_%28exterior_sign%29.JPG')"></div>
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
      <a href="${pageContext.request.contextPath}/login?tab=registro" class="btn btn-lg"
         style="background:rgba(255,255,255,0.15);color:#fff;border:1px solid rgba(255,255,255,0.4);backdrop-filter:blur(8px);">
        Crear cuenta gratis
      </a>
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

<!-- ===== Features con canvas de partículas ===== -->
<section class="features" aria-labelledby="features-titulo">
  <canvas id="featuresGl" aria-hidden="true"></canvas>
  <div class="container" style="position:relative;z-index:1;">
    <h2 class="section-title" id="features-titulo" style="color:#fff;">¿Qué puedes hacer aquí?</h2>
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
/* ============================================================
   CARRUSEL – control 100% JS, los dots realmente cambian slide
   ============================================================ */
(function () {
  var slides  = document.querySelectorAll('.hero-slide');
  var dots    = document.querySelectorAll('.carousel-dot');
  var current = 0;
  var timer;
  var DURATION = 5000;

  /* Mostrar slide activo, ocultar el resto */
  function goTo(index) {
    slides[current].classList.remove('carousel-active');
    dots[current].classList.remove('active');
    current = (index + slides.length) % slides.length;
    slides[current].classList.add('carousel-active');
    dots[current].classList.add('active');
  }

  /* Arrancar auto-play */
  function startAuto() {
    clearInterval(timer);
    timer = setInterval(function () { goTo(current + 1); }, DURATION);
  }

  /* Click en dot → ir a ese slide y reiniciar contador */
  dots.forEach(function (dot) {
    dot.addEventListener('click', function () {
      var idx = parseInt(dot.dataset.index, 10);
      goTo(idx);
      startAuto();
    });
  });

  /* Init */
  goTo(0);
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
    [0.42,0.18,0.03],[0.29,0.12,0.02],
    [0.60,0.30,0.10],[0.68,0.47,0.05],
    [0.80,0.65,0.28],[0.54,0.32,0.15],
    [0.20,0.10,0.04],[0.36,0.16,0.07],
    [0.45,0.28,0.10],[0.62,0.38,0.16],
  ];
  var NUM = 800;
  var bufData = new Float32Array(NUM * 7);
  var particles = [];

  for (var i = 0; i < NUM; i++) {
    var a = Math.random() * Math.PI * 2;
    var c = PALETTE[Math.floor(Math.random() * PALETTE.length)];
    particles.push({
      x: Math.cos(a)*Math.random()*1.2, y: Math.sin(a)*Math.random()*1.2,
      vx:0, vy:0, size:5+Math.random()*20, phase:Math.random()*100,
      r:c[0], g:c[1], b:c[2], alpha:0.12+Math.random()*0.25
    });
  }

  function resize() {
    var rect = section.getBoundingClientRect();
    var w=rect.width, h=rect.height;
    if (!w||!h) return;
    var dpr = Math.min(window.devicePixelRatio||1, 2);
    canvas.width=w*dpr; canvas.height=h*dpr;
    canvas.style.width=w+'px'; canvas.style.height=h+'px';
    gl.viewport(0,0,canvas.width,canvas.height);
  }
  window.addEventListener('resize', resize);
  if (window.ResizeObserver) new ResizeObserver(resize).observe(section);
  resize();

  function mkShader(t,s){var sh=gl.createShader(t);gl.shaderSource(sh,s);gl.compileShader(sh);return sh;}
  var prog=gl.createProgram();
  gl.attachShader(prog,mkShader(gl.VERTEX_SHADER,'attribute vec2 a_position;attribute float a_size;attribute vec4 a_color;varying vec4 v_color;void main(){gl_Position=vec4(a_position,0.,1.);gl_PointSize=a_size;v_color=a_color;}'));
  gl.attachShader(prog,mkShader(gl.FRAGMENT_SHADER,'precision mediump float;varying vec4 v_color;void main(){vec2 c=gl_PointCoord*2.-1.;float d=length(c);if(d>1.)discard;float core=1.-smoothstep(0.,.6,d);float glow=exp(-d*3.)*.5;gl_FragColor=vec4(v_color.rgb,(core+glow)*v_color.a);}'));
  gl.linkProgram(prog); gl.useProgram(prog);
  gl.enable(gl.BLEND); gl.blendFunc(gl.SRC_ALPHA,gl.ONE);
  gl.clearColor(0.08,0.04,0.01,1.0);

  var buf=gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER,buf);
  gl.bufferData(gl.ARRAY_BUFFER,bufData,gl.DYNAMIC_DRAW);
  var stride=28;
  var pl=gl.getAttribLocation(prog,'a_position');
  gl.enableVertexAttribArray(pl);gl.vertexAttribPointer(pl,2,gl.FLOAT,false,stride,0);
  var sl=gl.getAttribLocation(prog,'a_size');
  gl.enableVertexAttribArray(sl);gl.vertexAttribPointer(sl,1,gl.FLOAT,false,stride,8);
  var cl=gl.getAttribLocation(prog,'a_color');
  gl.enableVertexAttribArray(cl);gl.vertexAttribPointer(cl,4,gl.FLOAT,false,stride,12);

  var mx=0,my=0,mA=false;
  section.addEventListener('mousemove',function(e){var r=canvas.getBoundingClientRect();mx=((e.clientX-r.left)/r.width)*2-1;my=-((e.clientY-r.top)/r.height)*2+1;mA=true;});
  section.addEventListener('mouseleave',function(){mA=false;});

  function loop(now){
    var t=now*0.001;
    var repelR=0.25,repelS=0.004;
    for(var i=0;i<NUM;i++){
      var p=particles[i];
      var sp=0.1+p.size*0.008;
      p.vx+=(Math.sin(t*0.25+p.phase)*0.4+Math.sin(t*0.15+p.phase*1.7)*0.3)*0.0003*sp;
      p.vy+=(Math.cos(t*0.20+p.phase*1.3)*0.4+Math.cos(t*0.18+p.phase*2.1)*0.3)*0.0003*sp;
      p.vx*=0.98;p.vy*=0.98;
      if(mA){var dx=mx-p.x,dy=my-p.y;var dist=Math.sqrt(dx*dx+dy*dy);
        if(dist<repelR&&dist>0.001){var f=(repelR-dist)/repelR*repelS/(dist*0.5+0.1);var nd=Math.sqrt(dx*dx+dy*dy);if(nd>0.001){p.vx-=(dx/nd)*f;p.vy-=(dy/nd)*f;}}
      }
      p.x+=p.vx;p.y+=p.vy;
      var m=0.05;
      if(p.x>1+m)p.x=-1-m;if(p.x<-1-m)p.x=1+m;
      if(p.y>1+m)p.y=-1-m;if(p.y<-1-m)p.y=1+m;
      var idx=i*7;
      bufData[idx]=p.x;bufData[idx+1]=p.y;bufData[idx+2]=p.size;
      bufData[idx+3]=p.r;bufData[idx+4]=p.g;bufData[idx+5]=p.b;bufData[idx+6]=p.alpha;
    }
    gl.bindBuffer(gl.ARRAY_BUFFER,buf);gl.bufferSubData(gl.ARRAY_BUFFER,0,bufData);
    gl.clear(gl.COLOR_BUFFER_BIT);gl.drawArrays(gl.POINTS,0,NUM);
    requestAnimationFrame(loop);
  }
  requestAnimationFrame(loop);
})();
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
