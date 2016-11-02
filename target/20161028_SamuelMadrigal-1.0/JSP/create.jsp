<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>CRUD Create</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/estilo1.css" type="text/css"/>
    </head>
    <body>
        <div id="divInicial">
            <h1>Crear registro de avistamiento</h1>
            <form action="create" method="post">
                <fieldset>
                    <legend> Datos </legend>
                    <label for="anilla">Anilla: </label>
                    <input type="text" id="anilla" name="anilla" value="" size="3" minlength="3" maxlength="3" title="Campo requerido, 3 caracteres" required/>
                    <br/><br/>
                    <label for="especie">Especie: </label>
                    <input type="text" id="especie" name="especie" value="" size="20" maxlength="20" title="Introduzca la especie avistada" required/>
                    <br/><br/>
                    <label for="lugar">Lugar: </label>
                    <input type="text" id="lugar" name="lugar" value="" size="50" maxlength="50" title="Introduzca el lugar de avistamiento" required/>
                    <br/><br/>
                    <label for="dia">Fecha: </label>
                    <input type="text" id="dia" name="dia" value="" size="3" maxlength="2" placeholder="D&iacute;a" pattern="[0-9]*" required/> /
                    <input type="text" id="mes" name="mes" value="" size="3" maxlength="2" placeholder="Mes" pattern="[0-9]*" required/> /
                    <input type="text" id="anio" name="anio" value="" size="4" minlength="4" maxlength="4" placeholder="A&ntilde;o" pattern="[0-9]*" required/>
                    <br/><br/><br/>
                    <input type="submit" id="crear" name="crear" value="Crear Registro"/>
                </fieldset>
            </form>
            <p><a href="<%=request.getContextPath()%>">Volver al menu principal</a></p>
        </div>
    </body>
</html>
