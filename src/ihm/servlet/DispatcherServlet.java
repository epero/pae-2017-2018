package ihm.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;
import biz.user.UserDto;
import exceptions.BizException;
import ihm.services.ContactService;
import ihm.services.EntrepriseService;
import ihm.services.PersonneContactService;
import ihm.services.StageService;
import ihm.services.UserService;
import ihm.services.UtilService;
import util.AppContext.DependanceInjection;
import util.MonLogger;

public class DispatcherServlet extends DefaultServlet {

  // Nécessaire pour la sérialisation, pas besoin de modifier ce numéro dans notre cas
  private static final long serialVersionUID = 1L;

  @DependanceInjection
  private transient UtilService utilService;
  @DependanceInjection
  private transient ContactService contactService;
  @DependanceInjection
  private transient EntrepriseService entrepriseService;
  @DependanceInjection
  private transient PersonneContactService personneContactService;
  @DependanceInjection
  private transient StageService stageService;
  @DependanceInjection
  private transient UserService userService;
  @DependanceInjection
  private transient MonLogger monLogger;


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    try {
      if (req.getRequestURI().equals("/")) {
        Files.copy(new File("inc/header.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/navbar.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/_login.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/_signup.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/_dashboard.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/_entreprises.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/_fatalerror.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/_donneesPerso.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/prefooter.html").toPath(), resp.getOutputStream());
        Files.copy(new File("inc/footer.html").toPath(), resp.getOutputStream());
      } else {
        super.doGet(req, resp);
      }
    } catch (ServletException ex) {
      utilService.renvoyerCodeErr(resp, ex, 500);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html; charset=UTF-8");
    String action = req.getParameter("action");
    req.setAttribute("currentUser", utilService.getCurrentUser(req));
    monLogger.getMonLog().finest("action : " + action);
    try {
      if (req.getAttribute("currentUser") == null) {
        switch (action) {
          case "signin":
            userService.signin(req, resp);
            return;
          case "signup":
            userService.signup(req, resp);
            return;
          case "whoami":
            utilService.whoami(req, resp);
            return;
          default:
            break;
        }
      } else {
        switch (action) {
          case "whoami":
            utilService.whoami(req, resp);
            return;
          case "logout":
            utilService.logout(req, resp);
            return;
          case "visualiserEntreprises":
            entrepriseService.visualiserEntreprises(req, resp);
            return;
          case "getEntreprise":
            entrepriseService.getEntreprise(req, resp);
            return;
          case "visualiserPersonneContact":
            personneContactService.visualiserPersonnesContact(req, resp);
            return;
          case "creerPersonneContact":
            personneContactService.creerPersonneContact(req, resp);
            return;
          case "visualiserContacts":
            contactService.visualiserContacts(req, resp);
            return;
          case "creerContact":
            contactService.creerContactAsStud(req, resp);
            return;
          case "modifierEtatContact":
            contactService.modifierEtatContactsAsStud(req, resp);
            return;
          case "getInfosPersoUser":
            userService.getInfosPersoUser(req, resp);
            return;
          case "setInfosPersoUser":
            userService.setInfosPersoUser(req, resp);
            return;
          case "setMdpUser":
            userService.setMdpUser(req, resp);
            return;
          case "fillChartStudent":
            userService.fillChartStudentAsStudent(req, resp);
            return;
          case "confirmDataStage":
            stageService.confirmDataStageAsStud(req, resp);
            return;
          case "visualiserStage":
            stageService.visualiserStageAsStud(req, resp);
            return;
          case "insererEntreprise":
            entrepriseService.insererEntreprise(req, resp);
            return;
          default:
            break;
        }
        if (((UserDto) req.getAttribute("currentUser")).getEstAdmin()) {
          switch (action) {
            case "visualiserEntreprisesAsProf":
              entrepriseService.visualiserEntreprisesAsProf(req, resp);
              return;
            case "fillChartTeacher":
              userService.fillChartTeacher(req, resp);
              return;
            case "visualiserResponsables":
              personneContactService.visualiserResponsables(req, resp);
              return;
            case "visualiserStudCurYear":
              userService.visualiserStudCurYear(req, resp);
              return;
            case "visualiserContactsAsProf":
              contactService.visualiserContactsAsProf(req, resp);
              return;
            case "modifierEtatContactAsProf":
              contactService.modifierEtatContactsAsProf(req, resp);
              return;
            case "fillChartStudentAsProf":
              userService.fillChartStudentAsProf(req, resp);
              return;
            case "confirmDataStageAsProf":
              stageService.confirmDataStageAsProf(req, resp);
              return;
            case "visualiserStageAsProf":
              stageService.visualiserStageAsProf(req, resp);
              return;
            case "blacklistEntreprise":
              entrepriseService.blacklistEntreprise(req, resp);
              return;
            case "creerContactAsProf":
              contactService.creerContactAsProf(req, resp);
              return;
            case "fillSelectAnneeAcademique":
              entrepriseService.fillSelectAnneeAcademique(req, resp);
              return;
            case "fusionnerDeuxEntreprises":
              entrepriseService.fusionnerDeuxEntreprises(req, resp);
              return;
            default:
              break;
          }
        }
      }
    } catch (BizException ex) {
      utilService.renvoyerCodeErr(resp, ex, 422);
      return;
    } catch (Exception ex) {
      utilService.renvoyerCodeErr(resp, ex, 500);
      return;
    }
    utilService.renvoyerCodeErrEtMess(resp, "Action Interdite", 401);
  }
}
