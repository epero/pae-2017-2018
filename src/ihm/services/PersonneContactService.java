package ihm.services;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PersonneContactService {

  /**
   * Crée une personne de contact.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void creerPersonneContact(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

  /**
   * Permet de visualiser les personnes de contact.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void visualiserPersonnesContact(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

  /**
   * Permet de visualiser les responsables.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void visualiserResponsables(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

}
