<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Inicio – Portal de Cultura Quito"/>
<c:set var="currentPage" value="inicio"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<section class="hero">
    <div class="hero-content">
        <h1>Descubre los Museos de Quito</h1>
        <p>Explora la historia, el arte y la cultura de la capital del Ecuador.<br/>
           Consulta información, horarios y exposiciones de los museos de Quito.</p>
        <a href="${pageContext.request.contextPath}/museos" class="btn btn-primary btn-lg">
            Ver Museos Disponibles
        </a>
    </div>
</section>

<section class="features">
    <div class="container">
        <h2 class="section-title">¿Qué puedes hacer aquí?</h2>
        <div class="features-grid">
            <div class="feature-card">
                <span class="feature-icon">🔍</span>
                <h3>Consultar Museos</h3>
                <p>Explora el catálogo completo de museos disponibles en Quito con su información detallada.</p>
            </div>
            <div class="feature-card">
                <span class="feature-icon">📅</span>
                <h3>Ver Horarios</h3>
                <p>Conoce los horarios de atención de cada museo para planificar tu visita.</p>
            </div>
            <div class="feature-card">
                <span class="feature-icon">🎨</span>
                <h3>Exposiciones</h3>
                <p>Descubre las exposiciones permanentes y temporales disponibles en cada museo.</p>
            </div>
            <div class="feature-card">
                <span class="feature-icon">🎫</span>
                <h3>Reservar Visita</h3>
                <p>Reserva tu visita en línea para asegurar tu ingreso sin hacer filas.</p>
            </div>
        </div>
    </div>
</section>

<%@ include file="/WEB-INF/includes/footer.jsp" %>
