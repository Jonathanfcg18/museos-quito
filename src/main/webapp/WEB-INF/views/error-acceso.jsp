<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Acceso denegado – Portal de Cultura Quito"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>
<div class="container">
    <div class="error-page">
        <div class="error-code">403</div>
        <h2>Acceso denegado</h2>
        <p>No tienes permisos para acceder a esta sección.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
            Ir al inicio
        </a>
    </div>
</div>
<%@ include file="/WEB-INF/includes/footer.jsp" %>