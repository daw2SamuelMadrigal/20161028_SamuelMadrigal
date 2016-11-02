<%@page import="java.util.ArrayList"%>
<%@page import="es.albarregas.bean.Ave"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <%
        if (request.getAttribute("dir").equals("read")) {
        %> 
        <title>CRUD Read</title>
        <%
        } else if (request.getAttribute("dir").equals("update")) {
        %> 
        <title>CRUD Update</title>
        <%
        } else {
        %>
        <title>CRUD Delete</title>
        <%
         }
        %>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo1.css" type="text/css"/>
    </head>
    <body>
        <div id="divInicial">
            <h1>Avistamientos</h1>
            <form action="updateDelete" method="post">
                <div id="divInicial2">
            <%
            ArrayList<Ave> aves = (ArrayList<Ave>) request.getAttribute("aves");
            for (Ave a : aves) {
            %>
                <p class="p">
                    <span><%=a.getAnilla()%></span><br/>
                    <%=a.getEspecie()%><br/>
                    <%
                    if (request.getAttribute("dir").equals("read")) {
                    %> 
                    <%=a.getLugar()%><br/>
                    <%=a.getFecha()%><br/>
                </p>
                    <%
                    } else if (request.getAttribute("dir").equals("update")) {
                    %> 
                    <input type="radio" name="anilla" value="<%=a.getAnilla()%>"/>
                </p>
                    <%
                    } else {
                    %>
                    <input type="checkbox" name="anilla" value="<%=a.getAnilla()%>"/>
                </p>
                    <%
                    }
            }
            if (request.getAttribute("dir").equals("read")) {
            %> 
                </div>
            </form>
            <div class="clear"></div>
            <%
            } else if (request.getAttribute("dir").equals("update")) {
            %> 
                </div>
                <div class="clear"></div>
                <input type="submit" id="actualizar" name="actualizar" value="Actualizar Registro"/>
            </form>
            <%
            } else {
            %>
                </div>
                <div class="clear"></div>
                <input type="submit" id="eliminar" name="eliminar" value="Eliminar Registro/s"/>
            </form>
            <%
            }
            %>
            <p><a href="<%=request.getContextPath()%>">Volver al menu principal</a></p>
        </div>
    </body>
</html>
