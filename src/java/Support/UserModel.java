/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Support;

import Excepcions.UserExcepcion;
import Model.Users;
import org.hibernate.HibernateException;
import DB.HibernateUtil;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Jorge Remón
 */
public class UserModel {

    public void editarNombreUsuario(String email, String nuevoNombre, String nuevoApellido) throws UserExcepcion {
        Session sesionHibernate = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction tx = null;
            tx = sesionHibernate.beginTransaction();
            Query q = sesionHibernate.createSQLQuery("UPDATE USERS SET First_Name = :nuevoNombre, Last_Name= :nuevoApellido WHERE Email = :email");
            q.setParameter("nuevoNombre", nuevoNombre);
            q.setParameter("nuevoApellido", nuevoApellido);
            q.setParameter("email", email);
            q.executeUpdate();
            tx.commit();
        } catch (HibernateException ex) {
            throw new UserExcepcion("Error al editar el nombre del usuario: " + ex.getCause().getLocalizedMessage());
        } finally {
            if (sesionHibernate.isOpen()) {
                sesionHibernate.close();
            }
        }
    }

    public void editarRol(int nuevoRol, String email) throws UserExcepcion {
        Session sesionHibernate = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction tx = null;
            tx = sesionHibernate.beginTransaction();
            Query q = sesionHibernate.createSQLQuery("UPDATE USERS SET Is_Admin = :nuevoRol WHERE Email = :email");
            q.setParameter("nuevoRol", nuevoRol);
            q.executeUpdate();
            tx.commit();
        } catch (HibernateException ex) {
            throw new UserExcepcion("Error al editar el rol del usuario: " + ex.getCause().getLocalizedMessage());
        } finally {
            if (sesionHibernate.isOpen()) {
                sesionHibernate.close();
            }
        }
    }

    public void insertarUsuario(String emailUsuario, String nombre, String apellido, String contrasenna) throws UserExcepcion {
        Session sesionHibernate = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction tx = null;
            tx = sesionHibernate.beginTransaction();
            Query q = sesionHibernate.createSQLQuery("CALL ADD_USER(:emailUsuario,:nombre,:apellido,:contrasenna)");
            q.setParameter("emailUsuario", emailUsuario);
            q.setParameter("nombre", nombre);
            q.setParameter("apellido", apellido);
            q.setParameter("contrasenna", contrasenna);
            q.executeUpdate();
            tx.commit();
        } catch (HibernateException ex) {
            throw new UserExcepcion("Error al agregar el usuario: " + ex.getCause().getLocalizedMessage());
        } finally {
            if (sesionHibernate.isOpen()) {
                sesionHibernate.close();
            }
        }
    }

    public int validarUsuario(String emailUsuario, String contrasenna) throws UserExcepcion {
        int validacion = 0;
        Session sesionHibernate = HibernateUtil.getSessionFactory().openSession();
        try {
            SQLQuery q = sesionHibernate.createSQLQuery("CALL VALIDATE_USER(:emailUsuario, :contrasenna)");
            q.setParameter("emailUsuario", emailUsuario);
            q.setParameter("contrasenna", contrasenna);
            q.executeUpdate();
            validacion = ((BigInteger) q.uniqueResult()).intValue();
        } catch (HibernateException ex) {
            throw new UserExcepcion("Error al validar el usuario: " + ex.getCause().getLocalizedMessage());
        } finally {
            if (sesionHibernate.isOpen()) {
                sesionHibernate.close();
            }
        }
        return validacion;
    }

    public Users getDatosUsuario(String emailUsuario) throws UserExcepcion {
        Users usuario = new Users();
        Session sesionHibernate = HibernateUtil.getSessionFactory().openSession();
        try {
            SQLQuery q = sesionHibernate.createSQLQuery("SELECT * FROM USERS WHERE Email = :emailUsuario").addEntity(Users.class);
            q.setParameter("emailUsuario", emailUsuario);
            usuario = (Users) q.uniqueResult();

        } catch (HibernateException ex) {
            throw new UserExcepcion("Error al obtener los datos del usuario: " + ex.getCause().getLocalizedMessage());

        } finally {
            if (sesionHibernate.isOpen()) {
                sesionHibernate.close();
            }
        }
        return usuario;
    }

    public List<Users> getListaUsuarios() throws UserExcepcion {
        List<Users> usuarios;
        Session sesionHibernate = HibernateUtil.getSessionFactory().openSession();
        try {
            SQLQuery q = sesionHibernate.createSQLQuery("SELECT * FROM USERS ").addEntity(Users.class);
            usuarios = q.list();
        } catch (HibernateException ex) {
            throw new UserExcepcion("Error al obtener el listado de Usuarios: " + ex.getCause().getLocalizedMessage());
        } finally {
            if (sesionHibernate.isOpen()) {
                sesionHibernate.close();
            }
        }
        return usuarios;
    }

    public void eliminarUsuario(String email) throws UserExcepcion {
        Session sesionHibernate = HibernateUtil.getSessionFactory().openSession();
        try {
            Transaction tx = null;
            tx = sesionHibernate.beginTransaction();
            Query q = sesionHibernate.createSQLQuery("DELETE FROM USERS WHERE Email = :email");
            q.setParameter("email", email);
            q.executeUpdate();
            tx.commit();
        } catch (HibernateException ex) {
            throw new UserExcepcion("Error al eliminar el usuario: " + ex.getCause().getLocalizedMessage());
        } finally {
            if (sesionHibernate.isOpen()) {
                sesionHibernate.close();
            }
        }
    }

}
