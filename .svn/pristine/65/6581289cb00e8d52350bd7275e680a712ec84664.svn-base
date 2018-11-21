package dal.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import biz.stage.StageDto;
import exceptions.FatalException;
import util.AppContext.DependanceInjection;
import util.MonLogger;
import util.Util;

public class StageDaoImpl extends DaoGenerique<StageDto> implements StageDao {

  @DependanceInjection
  private EntrepriseDao entrepriseDao;

  @DependanceInjection
  private PersonneContactDao personneContactDao;

  @DependanceInjection
  private MonLogger monLogger;

  @Override
  public StageDto insertStage(StageDto stage) {
    return super.insert(stage);
  }

  public StageDto updateStage(StageDto stageDto) {
    return super.update(stageDto);

  }

  @Override
  public StageDto getStage(int idUtilisateur) {
    PreparedStatement ps = dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_get_stage_with_user_id"));
    setPreparedStatement(ps, idUtilisateur);
    StageDto stage;
    List<StageDto> stageDtos;
    try (ResultSet rs = ps.executeQuery()) {
      stage = (StageDto) factory.getStageVide();
      stageDtos = setResultSet(rs);
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur lors de l'accès à la DB");
    }
    if (stageDtos == null || stageDtos.isEmpty()) {
      return null;
    }
    stage = stageDtos.get(0);
    stage.setEntrepriseDto(entrepriseDao.getEntreprise(stage.getEntreprise()));
    stage.setResponsableDto(personneContactDao.getPersonneContact(stage.getResponsable()));
    return stage;

  }

  @Override
  public List<StageDto> listerStagesPourEntreprise(int idEntreprise) {
    List<StageDto> listeStages;
    PreparedStatement ps = dalBackendServices
        .getPreparedStatement(appContext.getValueProp("query_lister_stages_entreprises"));
    setPreparedStatement(ps, idEntreprise);
    try (ResultSet rs = ps.executeQuery()) {
      listeStages = setResultSet(rs);
    } catch (SQLException ex) {
      monLogger.getMonLog().fine(Util.stackTraceToString(ex));
      throw new FatalException("Erreur accès DB");
    }
    return listeStages;
  }
}
