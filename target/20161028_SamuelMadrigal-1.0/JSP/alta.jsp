<%@page import="es.albarregas.bean.Ave"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Alta</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo1.css" type="text/css"/>
    </head>
    <body>
        <div id="divInicial">
            <h1>Datos guardados</h1>
            <%Ave ave = (Ave) request.getAttribute("ave");%>
            <p><span id="neg">Anilla: </span><%=ave.getAnilla()%></p>
            <p><span id="neg">Especie: </span><%=ave.getEspecie()%></p>
            <p><span id="neg">Lugar: </span><%=ave.getLugar()%></p>
            <p><span id="neg">Fecha: </span><%=ave.getFecha()%></p>
            <p><a href="<%=request.getContextPath()%>">Volver al menu principal</a></p>
        </div>
    </body>
</html>
