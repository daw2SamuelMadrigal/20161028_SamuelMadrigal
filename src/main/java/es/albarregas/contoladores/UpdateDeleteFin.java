package es.albarregas.contoladores;

import es.albarregas.bean.Ave;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "UpdateDeleteFin", urlPatterns = {"/updateDeleteFin"})
public class UpdateDeleteFin extends HttpServlet {

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
        Ave ave = null;
        String sql = null;
        String url = null;
        String anilla = null;
        String especie = null;
        String lugar = null;
        String fecha = null;
        String[] anillas = null;
        try {
            // Obtenemos conexión
            conexion = datasource.getConnection();
            // Comprobamos parámetro de botones actualizar o eliminar
            if (request.getParameter("eliminar") != null) {
                try {
                    // Obtenemos anillas del formulario
                    anillas = request.getParameterValues("anilla");
                    // Creamos y ejecutamos la sentencia sql preparada
                    sql = "delete from aves where anilla in(?";
                    for (int i = 1; i < anillas.length; i++) {
                        sql = sql.concat(",?");
                    }
                    sql = sql.concat(")");
                    preparada = conexion.prepareStatement(sql);
                    for (int i = 1; i <= anillas.length; i++) {
                        preparada.setString(i, anillas[i - 1]);
                    }
                    preparada.executeUpdate();
                    // Url de index
                    url = "index.html";
                } catch (SQLException e) {
                    // Pasamos a la respuesta error desconocido
                    request.setAttribute("error", "Error desconocido en la eliminaci&oacute;n");
                    e.getStackTrace();
                    // Url de error
                    url = "JSP/error.jsp";
                }
            } else { // Venimos de actualizar
                try {
                    // Obtenemos campos del formulario
                    anilla = request.getParameter("anilla");
                    // Comprobación de longitud del campo anilla en servidor, para probar deshabilitar validación en "update.jsp"
                    if (anilla.length() != 3) {
                        // Lanzamos excepción con mensaje
                        throw new Exception("El valor del campo anilla debe tener 3 caracteres");
                    }
                    especie = request.getParameter("especie");
                    // Comprobación de longitud del campo especie en servidor, para probar deshabilitar validación en "update.jsp"
                    if (especie.equals("") || especie.length() > 20) {
                        // Lanzamos excepción con mensaje
                        throw new Exception("El valor del campo especie debe tener entre 1 y 20 caracteres");
                    }
                    lugar = request.getParameter("lugar");
                    // Comprobación de longitud del campo lugar en servidor, para probar deshabilitar validación en "update.jsp"
                    if (lugar.equals("") || lugar.length() > 50) {
                        // Lanzamos excepción con mensaje
                        throw new Exception("El valor del campo lugar debe tener entre 1 y 50 caracteres");
                    }
                    fecha = request.getParameter("dia") + "-" + request.getParameter("mes") + "-" + request.getParameter("anio");
                    // Comprobación de fecha válida
                    if (Integer.parseInt(request.getParameter("mes")) > 12 || !fechaCorrecta(request.getParameter("dia"), request.getParameter("mes"), request.getParameter("anio"))) {
                        // Lanzamos excepción con mensaje
                        throw new Exception("La fecha no es correcta");
                    }
                    // Creamos y ejecutamos la sentencia sql preparada
// POR QUÉ ACTUALIZAS TODO SI ES POSIBLE QUE NO HAYAMOS CAMBIADO NADA
                    sql = "update aves set especie=?, lugar=?,fecha=? where anilla=?";
                    preparada = conexion.prepareStatement(sql);
                    preparada.setString(1, especie);
                    preparada.setString(2, lugar);
                    preparada.setString(3, fecha);
                    preparada.setString(4, anilla);
                    preparada.executeUpdate();
                    // Url de index
                    url = "index.html";
                } catch (SQLException e) {
                    // Pasamos a la respuesta error desconocido
                    request.setAttribute("error", "Error desconocido en la actualizaci&oacute;n");
                    e.getStackTrace();
                    // Url de error
                    url = "JSP/error.jsp";
                } catch (Exception ex) {
                    // Pasamos a la respuesta el error de fecha
                    request.setAttribute("error", ex.getMessage());
                    ex.getStackTrace();
                    // Url de error
                    url = "JSP/error.jsp";
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
        }
    }

    /**
     * método para fecha correcta
     *
     */
    private boolean fechaCorrecta(String d, String m, String a) {
        boolean correcto = true;
        int dia = Integer.parseInt(d);
        int mes = Integer.parseInt(m);
        int anio = Integer.parseInt(a);
        int mesDias = 0;
        int bisiesto = 0;
        int[] numDiasMes = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (anioBisiesto(anio) && mes == 2) {
            bisiesto = 1;
        }
        mesDias = numDiasMes[mes - 1] + bisiesto;
        if (dia > mesDias) {
            correcto = false;
        }

        return correcto;
    }

    /**
     * método para año bisiesto
     *
     */
    private boolean anioBisiesto(int anio) {
        boolean anioBisiesto = false;
        if ((anio % 100 != 0 || anio % 400 == 0) && anio % 4 == 0) {
            anioBisiesto = true;
        }
        return anioBisiesto;
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
