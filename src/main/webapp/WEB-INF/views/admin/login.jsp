<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Panel Administrador – Portal de Cultura Quito</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css"/>
</head>
<body>

<header class="navbar">
    <div class="navbar-brand">
        <span class="logo-icon">🏛️</span>
        <div>
            <span class="brand-title">Portal de Cultura Quito</span>
            <span class="brand-sub">Panel de Administración</span>
        </div>
    </div>
</header>

<main class="main-content">
<div class="container">
    <div class="login-box">
        <h1>🔐 Acceso Administrador</h1>
        <p class="text-muted">Ingresa con tus credenciales de administrador del museo.</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/admin/login"
              class="form-login">

            <div class="form-group">
                <label for="email">Correo electrónico</label>
                <input type="email" id="email" name="email"
                       class="form-control" required
                       placeholder="admin@museos.gob.ec"/>
            </div>

            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password"
                       class="form-control" required
                       placeholder="••••••••"/>
            </div>

            <button type="submit" class="btn btn-primary btn-full">
                Ingresar al panel →
            </button>
        </form>

        <div class="login-hint">
            <p><strong>Credenciales de prueba:</strong></p>
            <p>Email: <code>admin.nacional@museos.gob.ec</code></p>
            <p>Password: <code>admin123</code></p>
        </div>

        <a href="${pageContext.request.contextPath}/"
           class="btn btn-secondary btn-full" style="margin-top:1rem;">
            ← Volver al portal
        </a>
    </div>
</div>
</main>

<footer class="footer">
    <div class="footer-content">
        <p>Portal de Cultura Quito – Panel de Administración</p>
    </div>
</footer>

</body>
</html>