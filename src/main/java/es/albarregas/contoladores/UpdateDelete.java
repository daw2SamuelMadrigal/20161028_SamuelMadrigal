package es.albarregas.contoladores;

import es.albarregas.bean.Ave;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

@WebServlet(name = "UpdateDelete", urlPatterns = {"/updateDelete"})
public class UpdateDelete extends HttpServlet {

    DataSource datasource;

    // Método init en el que ejecutamos la creación del servicio JNDI
    @Override
    public void init() {
        try {
            Context initialContext = new InitialContext();
            datasource = (DataSource) initialContext.lookup("java:comp/env/jdbc/Pool");
        } catch (NamingException ex) {
            System.out.println("Problemas en el acceso al recurso...");
            ex.printStackTrace();
        }
    }

    protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conexion = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        Ave ave = null;
        ArrayList<Ave> aves = null;
        String[] anillas = null;
        String anilla = null;
        String sql = null;
        String url = null;
        try {
            // Obtenemos conexión
            conexion = datasource.getConnection();
            // Comprobamos parámetro de botones actualizar o eliminar
            if (request.getParameter("actualizar") != null) {
                // Obtenemos parámetro anilla
                anilla = request.getParameter("anilla");
                // Creamos, preparamos y ejecutamos la sentencia sql
                sql = "select * from aves where anilla=?";
                preparada = conexion.prepareStatement(sql);
                preparada.setString(1, anilla);
                try {
                    resultado = preparada.executeQuery();
                    resultado.next();
                    // Creamos Ave con resultados de la base de datos
                    ave = new Ave();
                    ave.setAnilla(resultado.getString("anilla"));
                    ave.setEspecie(resultado.getString("especie"));
                    ave.setLugar(resultado.getString("lugar"));
                    ave.setFecha(resultado.getString("fecha"));
                    // Pasamos a la respuesta el Ave a actualizar
                    request.setAttribute("ave", ave);
                    // Url de actualizar
                    url = "JSP/update.jsp";
                } catch (SQLException e) {
                    // Pasamos a la respuesta error de anilla sin seleccionar
                    request.setAttribute("error", "No has seleccionado anilla");
                    e.getStackTrace();
                    // Url de error
                    url = "JSP/error.jsp";
                }
            } else { // Venimos de eliminar
                // Obtenemos parámetro anillas
                anillas = request.getParameterValues("anilla");
                // Comprobamos que se hayan marcado anillas
                if (anillas == null) {
                    // Pasamos a la respuesta error de anilla sin seleccionar
                    request.setAttribute("error", "No has seleccionado anilla");
                    // Url de error
                    url = "JSP/error.jsp";
                } else {
                    // Creamos, preparamos y ejecutamos la sentencia sql
// SERIA CONVENIENTE QUE LA VARIABLE sql FUESE UN StringBuilder
                    sql = "select * from aves where anilla in(?";
                    for (int i = 1; i < anillas.length; i++) {
                        sql = sql.concat(",?");
                    }
                    sql = sql.concat(")");
                    preparada = conexion.prepareStatement(sql);
                    for (int i = 1; i <= anillas.length; i++) {
                        preparada.setString(i, anillas[i - 1]);
                    }
                    resultado = preparada.executeQuery();
                    // Creamos y llenamos array de aves con resultados de la base de datos
                    aves = new ArrayList();
                    while (resultado.next()) {
                        ave = new Ave();
                        ave.setAnilla(resultado.getString("anilla"));
                        ave.setEspecie(resultado.getString("especie"));
                        ave.setLugar(resultado.getString("lugar"));
                        ave.setFecha(resultado.getString("fecha"));
                        aves.add(ave);
                    }
                    // Pasamos a la respuesta el array de aves a eliminar
                    request.setAttribute("aves", aves);
                    // Url de eliminar
                    url = "JSP/delete.jsp";
                }
            }
            // Dirigimos a url
            request.getRequestDispatcher(url).forward(request, response);
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
                // Comprobamos preparada
                if (preparada != null) {
                    System.out.println("Cerrada preparada");
                    // Cerramos preparada
                    preparada.close();
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
