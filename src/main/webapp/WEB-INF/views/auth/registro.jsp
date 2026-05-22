<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Crear cuenta – Portal de Cultura Quito"/>
<%@ include file="/WEB-INF/includes/header.jsp" %>

<div class="container">
    <div class="login-box">

        <h1>📋 Crear cuenta</h1>
        <p class="text-muted">Regístrate para reservar visitas a los museos de Quito.</p>

        <%-- Mensaje de error del servidor (Escenarios 2, 3 y 4) --%>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <%--
            T-01.1: Formulario de registro con los cuatro campos obligatorios.
            T-01.2: Validaciones del cliente: campos obligatorios, coincidencia
                    de contraseñas y formato de correo.
        --%>
        <form method="post"
              action="${pageContext.request.contextPath}/registro"
              id="formRegistro"
              novalidate>

            <%-- Campo: nombre completo --%>
            <div class="form-group">
                <label for="nombre">Nombre completo</label>
                <input type="text"
                       id="nombre"
                       name="nombre"
                       class="form-control"
                       value="${not empty nombre ? nombre : ''}"
                       placeholder="Ej: Ana López"
                       maxlength="100"
                       required/>
                <span class="campo-error" id="errNombre" style="display:none;
                      color:var(--error-color);font-size:.82rem;">
                    El nombre completo es obligatorio.
                </span>
            </div>

            <%-- Campo: correo electrónico --%>
            <div class="form-group">
                <label for="email">Correo electrónico</label>
                <input type="email"
                       id="email"
                       name="email"
                       class="form-control"
                       value="${not empty email ? email : ''}"
                       placeholder="ejemplo@correo.com"
                       maxlength="150"
                       required/>
                <span class="campo-error" id="errEmail" style="display:none;
                      color:var(--error-color);font-size:.82rem;">
                    Ingresa un correo electrónico válido.
                </span>
            </div>

            <%-- Campo: contraseña --%>
            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password"
                       id="password"
                       name="password"
                       class="form-control"
                       placeholder="Mínimo 8 caracteres"
                       minlength="8"
                       required/>
                <span class="campo-error" id="errPassword" style="display:none;
                      color:var(--error-color);font-size:.82rem;">
                    La contraseña debe tener al menos 8 caracteres.
                </span>
                <small class="form-hint">Mínimo 8 caracteres.</small>
            </div>

            <%-- Campo: confirmar contraseña --%>
            <div class="form-group">
                <label for="confirmar">Confirmar contraseña</label>
                <input type="password"
                       id="confirmar"
                       name="confirmar"
                       class="form-control"
                       placeholder="Repite tu contraseña"
                       required/>
                <span class="campo-error" id="errConfirmar" style="display:none;
                      color:var(--error-color);font-size:.82rem;">
                    Las contraseñas no coinciden.
                </span>
            </div>

            <button type="submit" class="btn btn-primary btn-full"
                    style="margin-top:.75rem;">
                Crear cuenta →
            </button>
        </form>

        <p style="text-align:center;margin-top:1.25rem;font-size:.9rem;
                  color:var(--text-muted);">
            ¿Ya tienes cuenta?
            <a href="${pageContext.request.contextPath}/login">Inicia sesión aquí</a>
        </p>

        <a href="${pageContext.request.contextPath}/"
           class="btn btn-secondary btn-full" style="margin-top:.75rem;">
            ← Volver al inicio
        </a>
    </div>
</div>

<%--
    T-01.2: Validaciones del lado del cliente.
    Se ejecutan antes de enviar el formulario al servidor.
    Cubren: campos vacíos, formato de correo y coincidencia de contraseñas.
--%>
<script>
    document.getElementById("formRegistro").addEventListener("submit", function(e) {
        let valido = true;

        // Limpiar errores anteriores
        document.querySelectorAll(".campo-error").forEach(function(el) {
            el.style.display = "none";
        });
        document.querySelectorAll(".form-control").forEach(function(el) {
            el.style.borderColor = "";
        });

        const nombre    = document.getElementById("nombre");
        const email     = document.getElementById("email");
        const password  = document.getElementById("password");
        const confirmar = document.getElementById("confirmar");

        // Validar nombre obligatorio
        if (nombre.value.trim() === "") {
            mostrarError("errNombre", nombre);
            valido = false;
        }

        // Validar email obligatorio y formato
        const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email.value.trim() === "") {
            mostrarError("errEmail", email);
            document.getElementById("errEmail").textContent =
                "El correo electrónico es obligatorio.";
            valido = false;
        } else if (!regexEmail.test(email.value.trim())) {
            mostrarError("errEmail", email);
            document.getElementById("errEmail").textContent =
                "Ingresa un correo electrónico válido.";
            valido = false;
        }

        // Validar contraseña: obligatoria y mínimo 8 caracteres
        if (password.value === "") {
            mostrarError("errPassword", password);
            document.getElementById("errPassword").textContent =
                "La contraseña es obligatoria.";
            valido = false;
        } else if (password.value.length < 8) {
            mostrarError("errPassword", password);
            document.getElementById("errPassword").textContent =
                "La contraseña debe tener al menos 8 caracteres.";
            valido = false;
        }

        // Validar confirmación y coincidencia
        if (confirmar.value === "") {
            mostrarError("errConfirmar", confirmar);
            document.getElementById("errConfirmar").textContent =
                "Debes confirmar tu contraseña.";
            valido = false;
        } else if (password.value !== confirmar.value) {
            // Escenario 3: contraseñas no coinciden (validación cliente)
            mostrarError("errConfirmar", confirmar);
            document.getElementById("errConfirmar").textContent =
                "Las contraseñas no coinciden.";
            valido = false;
        }

        if (!valido) e.preventDefault();
    });

    function mostrarError(idSpan, campo) {
        document.getElementById(idSpan).style.display = "block";
        campo.style.borderColor = "var(--error-color)";
    }
</script>

<%@ include file="/WEB-INF/includes/footer.jsp" %>