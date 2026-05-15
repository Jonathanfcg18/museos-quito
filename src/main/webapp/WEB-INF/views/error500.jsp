<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Error interno – Portal de Cultura Quito"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>
<div class="container">
    <div class="error-page">
        <div class="error-code">500</div>
        <h2>Error interno del servidor</h2>
        <p>Ocurrió un error inesperado. Por favor intenta de nuevo más tarde.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Ir al inicio</a>
    </div>
</div>
<%@ include file="/WEB-INF/includes/footer.jsp" %>
