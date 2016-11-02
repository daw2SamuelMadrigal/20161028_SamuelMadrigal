<%@page import="java.util.ArrayList"%>
<%@page import="es.albarregas.bean.Ave"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Delete</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo1.css" type="text/css"/>
    </head>
    <body>
        <div id="divInicial">
            <h1>Datos a eliminar</h1>
            <form action="updateDeleteFin" method="post">
                <div id="divInicial2">
                <%
                ArrayList<Ave> aves = (ArrayList<Ave>) request.getAttribute("aves");
                for (Ave a : aves) {
                %>
                    <p class="p">
                        <input type="hidden" id="anilla" name="anilla" value="<%=a.getAnilla()%>"/>
                        <span><%=a.getAnilla()%></span><br/>
                        <%=a.getEspecie()%><br/>
                        <%=a.getLugar()%><br/>
                        <%=a.getFecha()%><br/>
                    </p>
                <%
                }
                %>
                </div>
                <input type="submit" id="eliminar" name="eliminar" value="Aceptar"/>
                <input type="button" id="cancelar" name="cancelar" value="Cancelar" onclick="location.href='<%=request.getContextPath()%>'"/>
            </form>
        </div>
    </body>
</html>
