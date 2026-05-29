<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Iniciar sesión – Portal de Cultura Quito"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">
    <div class="login-box">

        <h1>🔐 Iniciar sesión</h1>
        <p class="text-muted">Accede con tu correo electrónico y contraseña.</p>

        <%--
            Mensajes de error del servidor:
            Escenario 2 → "Correo o contraseña incorrectos."
            Escenario 3 → "Tu cuenta ha sido suspendida. Contacta al administrador."
        --%>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <%-- T-02.1: Formulario de login con correo y contraseña. --%>
        <form method="post"
              action="${pageContext.request.contextPath}/login"
              id="formLogin"
              novalidate>

            <%-- Campo: correo electrónico --%>
            <div class="form-group">
                <label for="email">Correo electrónico</label>
                <input type="email"
                       id="email"
                       name="email"
                       class="form-control"
                       value="${not empty email ? email : ''}"
                       placeholder="correo@ejemplo.com"
                       required
                       autofocus/>
                <span class="campo-error" id="errEmail" style="display:none;
                      color:var(--error-color);font-size:.82rem;">
                    El correo electrónico es obligatorio.
                </span>
            </div>

            <%-- Campo: contraseña --%>
            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password"
                       id="password"
                       name="password"
                       class="form-control"
                       placeholder="••••••••"
                       required/>
                <span class="campo-error" id="errPassword" style="display:none;
                      color:var(--error-color);font-size:.82rem;">
                    La contraseña es obligatoria.
                </span>
            </div>

            <button type="submit" class="btn btn-primary btn-full"
                    style="margin-top:.75rem;">
                Ingresar →
            </button>
        </form>

        <p style="text-align:center;margin-top:1.25rem;font-size:.9rem;
                  color:var(--text-muted);">
            ¿No tienes cuenta?
            <a href="${pageContext.request.contextPath}/registro">Regístrate aquí</a>
        </p>

        <div class="login-hint" style="margin-top:1.25rem;">
            <p><strong>Credenciales de prueba:</strong></p>
            <p>👤 Visitante: <code>visitante@prueba.ec</code> /
               <code>Visita2026!</code></p>
            <p>🏛️ Admin museo: <code>admin.nacional@museos.gob.ec</code> /
               <code>Admin2026!</code></p>
        </div>

        <a href="${pageContext.request.contextPath}/"
           class="btn btn-secondary btn-full" style="margin-top:.75rem;">
            ← Volver al inicio
        </a>
    </div>
</div>

<%-- Validación del lado del cliente: campos obligatorios --%>
<script>
    document.getElementById("formLogin").addEventListener("submit", function(e) {
        let valido = true;

        document.querySelectorAll(".campo-error").forEach(function(el) {
            el.style.display = "none";
        });
        document.querySelectorAll(".form-control").forEach(function(el) {
            el.style.borderColor = "";
        });

        const email    = document.getElementById("email");
        const password = document.getElementById("password");

        if (email.value.trim() === "") {
            document.getElementById("errEmail").style.display = "block";
            email.style.borderColor = "var(--error-color)";
            valido = false;
        }

        if (password.value === "") {
            document.getElementById("errPassword").style.display = "block";
            password.style.borderColor = "var(--error-color)";
            valido = false;
        }

        if (!valido) e.preventDefault();
    });
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>