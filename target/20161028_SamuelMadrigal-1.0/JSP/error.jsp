<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Error</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo1.css" type="text/css"/>
    </head>
    <body>
        <div id="divInicial">
            <h1>Error</h1>
            <p id="err"><%=request.getAttribute("error")%></p>
            <p><a href="<%=request.getContextPath()%>">Volver al menu principal</a></p>
        </div>
    </body>
</html>
