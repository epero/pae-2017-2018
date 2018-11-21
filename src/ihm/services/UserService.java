package ihm.services;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

  /**
   * Renvoie les informations nécessaires pour remplir le graphique étudiant.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void fillChartStudentAsStudent(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

  /**
   * Renvoie les informations nécessaires pour remplir le graphique étudiant en tant que professeur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void fillChartStudentAsProf(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;


  /**
   * Renvoie les informations nécessaires pour remplir le graphique professeur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void fillChartTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException;

  /**
   * Permet d'obtenir les informations personnnelles de l'utilisateur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void getInfosPersoUser(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;


  /**
   * Modifie les informations personnelles de l'utilisateur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void setInfosPersoUser(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

  /**
   * Modifie le mot de passe de l'utilisateur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void setMdpUser(HttpServletRequest req, HttpServletResponse resp) throws IOException;

  /**
   * Connecte un utilisateur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void signin(HttpServletRequest req, HttpServletResponse resp) throws IOException;

  /**
   * Inscrit un utilisateur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void signup(HttpServletRequest req, HttpServletResponse resp) throws IOException;

  /**
   * Permet de visualiser les utilisateurs de l'année académique courante.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void visualiserStudCurYear(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;


}
