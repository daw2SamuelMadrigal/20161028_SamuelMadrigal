<%@page import="es.albarregas.bean.Ave"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Update</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo1.css" type="text/css"/>
    </head>
    <body>
        <div id="divInicial">
            <h1>Datos a modificar</h1>
            <form action="updateDeleteFin" method="post">
                <%Ave ave = (Ave) request.getAttribute("ave");%>
                <p>
                    <label id="neg" for="anilla">Anilla: </label>
                    <input type="text" id="anilla" name="anilla" value="<%=ave.getAnilla()%>" size="3" readonly/>
                </p>
                <p>
                    <label id="neg" for="especie">Especie: </label>
                    <input type="text" id="especie" name="especie" value="<%=ave.getEspecie()%>" size="20" maxlength="20" title="Introduzca la especie avistada" required/>
                </p>
                <p>
                    <label id="neg" for="lugar">Lugar: </label>
                    <input type="text" id="lugar" name="lugar" value="<%=ave.getLugar()%>" size="50" maxlength="50" title="Introduzca el lugar de avistamiento" required/>
                </p>
                <%
                String[] fecha=ave.getFecha().split("-");
                String dia=fecha[0];
                String mes=fecha[1];
                String anio=fecha[2];
                %>
                <p>
                    <label id="neg" for="dia">Fecha: </label>
                    <input type="text" id="dia" name="dia" value="<%=dia%>" size="3" maxlength="2" placeholder="D&iacute;a" pattern="[0-9]*" required/> / 
                    <input type="text" id="mes" name="mes" value="<%=mes%>" size="3" maxlength="2" placeholder="Mes" pattern="[0-9]*" required/> / 
                    <input type="text" id="anio" name="anio" value="<%=anio%>" size="4" minlength="4" maxlength="4" placeholder="A&ntilde;o" pattern="[0-9]*" required/>
                </p>
                <input type="submit" id="actualizar" name="actualizar" value="Aceptar"/>
                <input type="button" id="cancelar" name="cancelar" value="Cancelar" onclick="location.href='<%=request.getContextPath()%>'"/>
            </form>
        </div>
    </body>
</html>
