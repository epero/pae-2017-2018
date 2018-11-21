package ihm.services;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface StageService {

  /**
   * Permet de confirmer les données d'un stage.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void confirmDataStageAsStud(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

  /**
   * Permet de confirmer les données d'un stage en tant que professeur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void confirmDataStageAsProf(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

  /**
   * Permet de visualiser un stage.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void visualiserStageAsStud(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;

  /**
   * Permet de visualiser un stage en tant que professeur.
   * 
   * @param req la requête de la Servlet
   * @param resp la réponse de la Servlet
   * @throws IOException une exception
   */
  public void visualiserStageAsProf(HttpServletRequest req, HttpServletResponse resp)
      throws IOException;



}
