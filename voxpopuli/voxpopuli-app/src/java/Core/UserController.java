/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import DB.HibernateUtil;
import Excepcions.UserExcepcion;
import Model.Users;
import Support.UserModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;

@ManagedBean
@ViewScoped
public class UserController implements Serializable {

    private final HttpSession sesionHttp;

    private String email;
    private String firstName;
    private String lastName;
    private String PWord;
    private boolean isAdmin;
    private boolean validPassword;

    public UserController() {
        sesionHttp = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    public void mostrarInicio() throws UserExcepcion {
        try {
            sesionHttp.setAttribute("MOD_LOGIN_ACCION_IMEC_USER", "USER_START");
            sesionHttp.setAttribute("MOD_LOGIN_TITLE", "Home");
            UserModel userModel = new UserModel();
            int validacion = userModel.validarUsuario(this.email, this.PWord);
            if (validacion == 1) { //existe
                Users userObject = userModel.getDatosUsuario(this.email);
                sesionHttp.setAttribute("MOD_LOGIN_OBJECT_CRUD_USER", userObject);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/VoxPopuli/homePage.do");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales invalidos", "Invalid password or email."));
            }
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Users> getListaUsuarios() throws UserExcepcion {
        List<Users> listaUsuarios = null;
        try {
            UserModel userModel = new UserModel();
            listaUsuarios = userModel.getListaUsuarios();
        } catch (UserExcepcion ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaUsuarios;
    }

    public Users getUsuario(String email) throws UserExcepcion {
        Users persona = null;
        try {
            UserModel userModel = new UserModel();
            persona = userModel.getDatosUsuario(this.email);
        } catch (UserExcepcion ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return persona;
    }

    public void mostrarFrmUsuarios() {
        try {
            sesionHttp.setAttribute("MOD_LOGIN_ACCION_IMEC_USER", "LOGIN_MANTENIMIENTO_USUARIO");
            sesionHttp.setAttribute("MOD_LOGIN_TITLE", "Usuarios dentro del sistema");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/VoxPopuli/Contenido/Usuario/frmUsuarios.do");
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarFrmInsertarUsuario() throws UserExcepcion {
        try {
            sesionHttp.setAttribute("MOD_LOGIN_ACCION_IMEC_USER", "USER_INSERT");
            sesionHttp.setAttribute("MOD_LOGIN_TITLE", "Crear Usuario");
            sesionHttp.setAttribute("MOD_LOGIN_OBJECT_CRUD_USER", null);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/VoxPopuli/registroUsuario.do");
        } catch (IOException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMostrarEditarUsuario(Users usuario) throws IOException, UserExcepcion {
        sesionHttp.setAttribute("MOD_LOGIN_TITLE", "Editar datos de usuario");
        sesionHttp.setAttribute("MOD_LOGIN_ACCION_IMEC_USER", "USER_EDIT");
        sesionHttp.setAttribute("MOD_LOGIN_OBJECT_CRUD_USER", usuario);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/VoxPopuli/Contenido/Usuario/frmMantenimientoIMECUsuarios.do");
    }

    public String getTituloPagina() {
        String tituloPagina = "";
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null) {
            tituloPagina = (String) sesionHttp.getAttribute("MOD_LOGIN_TITLE");
        }
        return tituloPagina;
    }

    public String getAccion() {
        String accion = "";
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null) {
            accion = (String) sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER");
        }
        return accion;
    }

    public void insertarUsuario() throws UserExcepcion, IOException {
        try {
            UserModel userModel = new UserModel();
            userModel.insertarUsuario(this.email, this.firstName, this.lastName, this.PWord);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/VoxPopuli/inicioSesion.do");
        } catch (UserExcepcion ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarNombreUsuario() throws UserExcepcion {
        try {
            UserModel userModel = new UserModel();
            userModel.editarNombreUsuario(((Users) sesionHttp.getAttribute("MOD_LOGIN_OBJECT_CRUD_USER")).getEmail(), this.firstName, this.lastName);
            this.mostrarFrmUsuarios();
        } catch (UserExcepcion ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMostrarFrmEliminarUsuario(Users usuario) {
        UserModel userModel = new UserModel();
        try {
            userModel.eliminarUsuario(usuario.getEmail());
            this.mostrarFrmUsuarios();
        } catch (UserExcepcion ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     public void mostrarSalir() throws IOException, UserExcepcion {
        sesionHttp.setAttribute("MOD_LOGIN_TITLE", null);
        sesionHttp.setAttribute("MOD_LOGIN_ACCION_IMEC_USER", null);
        sesionHttp.setAttribute("MOD_LOGIN_OBJECT_CRUD_USER", null);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/VoxPopuli/inicioSesion.do");
    }
    
    
    public String getEmail() {
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null && (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER").equals("USER_EDIT"))) {
            email = ((Users) sesionHttp.getAttribute("MOD_LOGIN_OBJECT_CRUD_USER")).getEmail();
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null && (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER").equals("USER_EDIT"))) {
            firstName = ((Users) sesionHttp.getAttribute("MOD_LOGIN_OBJECT_CRUD_USER")).getFirstName();
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null && (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER").equals("USER_EDIT"))) {
            lastName = ((Users) sesionHttp.getAttribute("MOD_LOGIN_OBJECT_CRUD_USER")).getLastName();
        }
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPWord() {
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null && (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER").equals("USER_EDIT"))) {
            PWord = ((Users) sesionHttp.getAttribute("MOD_LOGIN_OBJECT_CRUD_USER")).getPWord();
        }
        return PWord;
    }

    public void setPWord(String PWord) {
        this.PWord = PWord;
    }

    public boolean getIsAdmin() {
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null && (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER").equals("USER_EDIT"))) {
            isAdmin = ((Users) sesionHttp.getAttribute("MOD_LOGIN_OBJECT_CRUD_USER")).isIsAdmin();
        }
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getValidPassword() {
        if (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER") != null && (sesionHttp.getAttribute("MOD_LOGIN_ACCION_IMEC_USER").equals("USER_EDIT"))) {
            validPassword = ((Users) sesionHttp.getAttribute("MOD_LOGIN_OBJECT_CRUD_USER")).isValidPassword();
        }
        return validPassword;
    }

    public void setValidPassword(boolean validPassword) {
        this.validPassword = validPassword;
    }

    public void SalirCrearUsuario() throws IOException, UserExcepcion {
        sesionHttp.setAttribute("MOD_LOGIN_TITLE", null);
        sesionHttp.setAttribute("MOD_LOGIN_ACCION_IMEC_USER", null);
        sesionHttp.setAttribute("MOD_LOGIN_OBJECT_CRUD_USER", null);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/VoxPopuli/inicioSesion.do");
    }
    
}
