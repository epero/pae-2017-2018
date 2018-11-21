package ihm.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import biz.factory.BizFactory;
import biz.pdc.PersonneContactDto;
import biz.ucc.PersonneContactUcc;
import util.AppContext.DependanceInjection;

public class PersonneContactServiceImpl implements PersonneContactService {

  @DependanceInjection
  UtilService utilService;
  @DependanceInjection
  BizFactory factory;
  @DependanceInjection
  PersonneContactUcc personneContactUcc;

  private Genson genson = new GensonBuilder().useIndentation(true)
      .useDateFormat(new SimpleDateFormat("dd-MM-yyyy")).create();

  @Override
  public void creerPersonneContact(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String json = req.getParameter("newPersonneContact");
    PersonneContactDto newPersonneContact = factory.getPersonneContactVide();

    genson.deserializeInto(json, newPersonneContact);
    PersonneContactDto personneContactDb =
        personneContactUcc.creerPersonneContact(newPersonneContact);
    if (personneContactDb != null) {
      json = genson.serialize(personneContactDb);
      resp.setContentType("application/json");
      resp.getOutputStream().write(json.getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

  @Override
  public void visualiserPersonnesContact(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    int idEntreprise = Integer.parseInt(req.getParameter("idEntreprise"));
    List<PersonneContactDto> personnes =
        personneContactUcc.visualiserPersonnesContact(idEntreprise);
    if (personnes != null) {
      String json = genson.serialize(personnes);
      resp.setContentType("application/json");
      resp.getOutputStream().write(json.getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

  @Override
  public void visualiserResponsables(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    List<PersonneContactDto> responsables = personneContactUcc.visualiserResponsablesStage();
    if (responsables != null) {
      resp.setContentType("application/json");
      resp.getOutputStream()
          .write(genson.serialize(responsables).getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

}
