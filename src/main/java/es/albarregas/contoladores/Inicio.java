package es.albarregas.contoladores;

import es.albarregas.bean.Ave;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "Inicio", urlPatterns = {"/inicio"})
public class Inicio extends HttpServlet {

    DataSource datasource;

    protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conexion = null;
        Statement sentencia = null;
        ResultSet resultado = null;
        Ave ave = null;
        ArrayList<Ave> aves = null;
        String sql = null;
        // Obtenemos parámetro del enlace de inicio
        String peticion = request.getParameter("param");
        String url = null;
        // Comprobamos parámetro del enlace de inicio
        if (peticion.equals("create")) {
            // Url de CREAR
            url = "JSP/create.jsp";
        } else {
            // Ejecutamos la creación del servicio JNDI
            try {
                Context initialContext = new InitialContext();
                datasource = (DataSource) initialContext.lookup("java:comp/env/jdbc/Pool");
            } catch (NamingException ex) {
                System.out.println("Problemas en el acceso al recurso...");
                ex.printStackTrace();
            }

            try {
                // Obtenemos conexión
                conexion = datasource.getConnection();
                // Creamos y ejecutamos la sentencia sql
                sql = "select * from aves";
                sentencia = conexion.createStatement();
                resultado = sentencia.executeQuery(sql);
                aves = new ArrayList();
                // Comprobamos tabla vacía
                if (!resultado.next()) {
                    // Pasamos a la respuesta el error
                    request.setAttribute("error", "No hay avistamientos");
                    url = "JSP/error.jsp";
                } else {
                    // Llenamos array de aves con resultados de la base de datos
                    resultado.previous();
                    while (resultado.next()) {
                        ave = new Ave();
                        ave.setAnilla(resultado.getString("anilla"));
                        ave.setEspecie(resultado.getString("especie"));
                        ave.setLugar(resultado.getString("lugar"));
                        ave.setFecha(resultado.getString("fecha"));
                        aves.add(ave);
                    }
                    // Pasamos a la respuesta el array de aves
                    request.setAttribute("aves", aves);
                    // Pasamos a la respuesta el valor del parámetro del enlace de inicio
                    request.setAttribute("dir", peticion);
                    // Url de LEER ACTUALIZAR Y ELIMINAR
                    url = "JSP/rud.jsp";
                }
            } catch (SQLException ex) {
                System.out.println("Error al crear la conexion");
                ex.printStackTrace();
            } finally {
                try {
                    // Comprobamos conexión
                    if (conexion != null) {
                        System.out.println("Cerrada conexion");
                        // Cerramos conexión
                        conexion.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {
                    // Comprobamos resultado
                    if (resultado != null) {
                        System.out.println("Cerrada resultado");
                        // Cerramos resultado
                        resultado.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        // Dirigimos a url
        request.getRequestDispatcher(url).forward(request, response);
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarPeticion(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        procesarPeticion(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
