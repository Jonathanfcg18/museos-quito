<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Página no encontrada – Portal de Cultura Quito"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>
<div class="container">
    <div class="error-page">
        <div class="error-code">404</div>
        <h2>Página no encontrada</h2>
        <p>El recurso que buscas no existe o fue movido.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Ir al inicio</a>
    </div>
</div>
<%@ include file="/WEB-INF/includes/footer.jsp" %>
