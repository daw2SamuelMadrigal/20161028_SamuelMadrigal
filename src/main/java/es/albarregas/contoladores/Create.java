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

@WebServlet(name = "Create", urlPatterns = {"/create"})
public class Create extends HttpServlet {

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
        try {
            // Obtenemos conexión
            conexion = datasource.getConnection();

            try {
                // Obtenemos campos del formulario
                anilla = request.getParameter("anilla");
                // Comprobación de longitud del campo anilla en servidor, para probar deshabilitar validación en "create.jsp"
                if (anilla.length() != 3) {
                    // Lanzamos excepción con mensaje
                    throw new Exception("El valor del campo anilla debe tener 3 caracteres");
                }
                especie = request.getParameter("especie");
                // Comprobación de longitud del campo especie en servidor, para probar deshabilitar validación en "create.jsp"
                if (especie.equals("") || especie.length() > 20) {
                    // Lanzamos excepción con mensaje
                    throw new Exception("El valor del campo especie debe tener entre 1 y 20 caracteres");
                }
                lugar = request.getParameter("lugar");
                // Comprobación de longitud del campo lugar en servidor, para probar deshabilitar validación en "create.jsp"
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
                sql = "insert into aves values(?, ?, ?, ?)";
                preparada = conexion.prepareStatement(sql);
                preparada.setString(1, anilla);
                preparada.setString(2, especie);
                preparada.setString(3, lugar);
                preparada.setString(4, fecha);
                preparada.executeUpdate();
                // Creamos objeto Ave con los valores de los campos del formulario
                ave = new Ave();
                ave.setAnilla(anilla);
                ave.setEspecie(especie);
                ave.setLugar(lugar);
                ave.setFecha(fecha);
                // Pasamos a la respuesta el Ave insertada
                request.setAttribute("ave", ave);
                // Url de alta de ave
                url = "JSP/alta.jsp";
            } catch (SQLException e) {
                // Comprobamos el error SQL
                if (e.getErrorCode() == 1062) {
                    // Pasamos a la respuesta el error de primary key duplicada
                    request.setAttribute("error", "La anilla " + anilla + " ya ha sido creada");
                } else {
                    // Pasamos a la respuesta error desconocido
                    request.setAttribute("error", "Error desconocido en la inserci&oacute;n");
                }
                e.getStackTrace();
                // Url de error
                url = "JSP/error.jsp";
            } catch (Exception ex) {
                // Pasamos a la respuesta el error de campo anilla o fecha
                request.setAttribute("error", ex.getMessage());
                ex.getStackTrace();
                // Url de error
                url = "JSP/error.jsp";
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
