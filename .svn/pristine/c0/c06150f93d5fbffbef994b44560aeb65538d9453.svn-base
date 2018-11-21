package ihm.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import biz.factory.BizFactory;
import biz.stage.StageDto;
import biz.ucc.StageUcc;
import biz.user.UserDto;
import exceptions.OptimisticLockException;
import util.AppContext;
import util.AppContext.DependanceInjection;
import util.Util;

public class StageServiceImpl implements StageService {

  @DependanceInjection
  UtilService utilService;
  @DependanceInjection
  BizFactory factory;
  @DependanceInjection
  StageUcc stageUcc;
  @DependanceInjection
  AppContext appContext;

  private Genson genson = new GensonBuilder().useIndentation(true)
      .useDateFormat(new SimpleDateFormat("dd-MM-yyyy")).create();

  private Genson geStage = new GensonBuilder().useIndentation(true).useClassMetadata(true)
      .useRuntimeType(true).exclude("dateSignature").create();

  private Genson geVisualiserStage = new GensonBuilder().useIndentation(true).exclude("utilisateur")
      .exclude("utilisateurDto").create();

  @Override
  public void confirmDataStageAsStud(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String json = req.getParameter("selectedStud");
    Map<String, Object> jsonMap = genson.deserialize(json, Map.class);
    if (!utilService.verifStud(req, resp, Integer.parseInt("" + jsonMap.get("idUtilisateur")))) {
      return;
    }
    confirmDataStage(req, resp, jsonMap);
  }

  @Override
  public void confirmDataStageAsProf(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String json = req.getParameter("selectedStud");
    Map<String, Object> jsonMap = genson.deserialize(json, Map.class);
    confirmDataStage(req, resp, jsonMap);
  }

  private void confirmDataStage(HttpServletRequest req, HttpServletResponse resp,
      Map<String, Object> jsonMap) throws IOException {
    String json = req.getParameter("stage");
    StageDto stageDto = factory.getStageVide();

    geStage.deserializeInto(json, stageDto);
    stageDto.setDateSignature(Util.jsonToLocalDate(json, "dateSignature"));
    StageDto stageDb;

    try {
      stageDb =
          stageUcc.confirmDataStage(stageDto, Integer.parseInt(req.getParameter("idContactAcc")),
              Integer.parseInt(req.getParameter("numVersContactAcc")),
              Integer.parseInt(jsonMap.get("idUtilisateur") + ""),
              Integer.parseInt(jsonMap.get("numVersion") + ""));
    } catch (OptimisticLockException ex) {
      Object object = ex.getObjetEnDb();
      if (object.getClass() == appContext.getClassValueProp("biz.user.UserDto")) {
        UserDto userDb = (UserDto) ex.getObjetEnDb();
        utilService.renvoyerCodeErrEtMessEtObj(resp, ex, 409, userDb, genson);
      } else {
        utilService.renvoyerCodeErr(resp, ex, 409);
      }
      return;
    }

    if (stageDb != null) {
      json = genson.serialize(stageDb.getUtilisateurDto());
      resp.setContentType("application/json");
      resp.getOutputStream().write(json.getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

  @Override
  public void visualiserStageAsStud(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    StageDto stage =
        stageUcc.visualiserStage(((UserDto) req.getAttribute("currentUser")).getIdUtilisateur());

    if (stage != null) {
      resp.setContentType("application/json");
      resp.getOutputStream()
          .write(geVisualiserStage.serialize(stage).getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

  @Override
  public void visualiserStageAsProf(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    UserDto selectedStud = factory.getUserVide();
    selectedStud.setIdUtilisateur(Integer.parseInt(req.getParameter("selectedStud")));
    StageDto stage = stageUcc.visualiserStage(selectedStud.getIdUtilisateur());

    if (stage != null) {
      resp.setContentType("application/json");
      resp.getOutputStream()
          .write(geVisualiserStage.serialize(stage).getBytes(Charset.forName("UTF-8")));
    } else {
      utilService.renvoyerCodeErrEtMess(resp, "Erreur de programmation", 500);
    }
  }

}
