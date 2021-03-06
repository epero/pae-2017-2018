package ihm.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import biz.entreprise.EntrepriseDto;
import biz.factory.BizFactory;
import biz.ucc.EntrepriseUcc;
import exceptions.OptimisticLockException;
import util.AppContext.DependanceInjection;

public class EntrepriseServiceImpl implements EntrepriseService {

  @DependanceInjection
  UtilService utilService;
  @DependanceInjection
  BizFactory factory;
  @DependanceInjection
  EntrepriseUcc entrepriseUcc;

  private Genson genson = new GensonBuilder().useIndentation(true)
      .useDateFormat(new SimpleDateFormat("dd-MM-yyyy")).create();

  @Override
  public void blacklistEntreprise(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String idEntreprise = req.getParameter("entreprise");
    String numVersionEntreprise = req.getParameter("numVersionEntreprise");
    EntrepriseDto entreprise = factory.getEntrepriseVide();
    entreprise.setIdEntreprise(Integer.parseInt(idEntreprise));
    entreprise.setNumVersion(Integer.parseInt(numVersionEntreprise));

    EntrepriseDto entrepriseAUpdate;
    try {
      entrepriseAUpdate = entrepriseUcc.blacklistEntreprise(Integer.parseInt(idEntreprise),
          Integer.parseInt(numVersionEntreprise));

      if (entrepriseAUpdate != null) {
        resp.setContentType("application/json");
        resp.getOutputStream()
            .write(genson.serialize(entrepriseAUpdate).getBytes(Charset.forName("UTF-8")));
      } else {
        utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
      }
    } catch (OptimisticLockException ex) {
      utilService.renvoyerCodeErr(resp, ex, 409);
    }
  }

  @Override
  public void fillSelectAnneeAcademique(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    List<String> anneesAcademiques = entrepriseUcc.getAnneesAcademiques();

    if (anneesAcademiques != null) {
      resp.setContentType("application/json");
      resp.getOutputStream()
          .write(genson.serialize(anneesAcademiques).getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }

  }

  @Override
  public void getEntreprise(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    int idEntreprise = Integer.parseInt(req.getParameter("idEntreprise"));
    EntrepriseDto entrepriseDto = entrepriseUcc.getEntreprise(idEntreprise);
    if (entrepriseDto != null) {
      resp.setContentType("application/json");
      resp.getOutputStream()
          .write(genson.serialize(entrepriseDto).getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

  @Override
  public void insererEntreprise(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String json = req.getParameter("newEntreprise");
    EntrepriseDto newEntreprise = factory.getEntrepriseVide();
    genson.deserializeInto(json, newEntreprise);
    EntrepriseDto entrepriseDb = entrepriseUcc.insertEntreprise(newEntreprise);
    if (entrepriseDb != null) {
      json = genson.serialize(entrepriseDb);
      resp.setContentType("application/json");
      resp.getOutputStream().write(json.getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }

  }

  @Override
  public void visualiserEntreprises(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    List<EntrepriseDto> entreprises = entrepriseUcc.visualiserEntreprises();
    if (entreprises != null) {
      resp.setContentType("application/json");
      resp.getOutputStream()
          .write(genson.serialize(entreprises).getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

  @Override
  public void visualiserEntreprisesAsProf(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String anneeAcademique = req.getParameter("anneeAcademique");
    List<HashMap<String, Object>> entreprises =
        entrepriseUcc.visualiserEntreprisesAsProf(anneeAcademique);

    if (entreprises != null) {
      resp.setContentType("application/json");
      resp.getOutputStream()
          .write(genson.serialize(entreprises).getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }

  }

  @Override
  public void fusionnerDeuxEntreprises(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    // entreprise qui sera fusionnée
    int idEntreprise1 = Integer.parseInt(req.getParameter("idEntreprise1"));
    int numVersEntr1 = Integer.parseInt(req.getParameter("numVersionEntr1"));
    // entreprise qui sera conservée
    int idEntreprise2 = Integer.parseInt(req.getParameter("idEntreprise2"));
    int numVersEntr2 = Integer.parseInt(req.getParameter("numVersionEntr2"));

    try {
      EntrepriseDto entrepriseRestante = entrepriseUcc.fusionnerEntreprise1AvecEntreprise2(
          idEntreprise1, numVersEntr1, idEntreprise2, numVersEntr2);
      if (entrepriseRestante != null) {
        resp.setContentType("application/json");
        resp.getOutputStream()
            .write(genson.serialize(entrepriseRestante).getBytes(Charset.forName("UTF-8")));
      } else {
        utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
      }
    } catch (OptimisticLockException ex) {
      utilService.renvoyerCodeErr(resp, ex, 409);
    }
  }

}
